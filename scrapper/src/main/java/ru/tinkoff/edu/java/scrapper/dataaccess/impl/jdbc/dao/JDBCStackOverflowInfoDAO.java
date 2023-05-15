package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCStackOverflowInfoDAO extends JDBCDAO {
    private static final String COLUMN_NAME_FOR_WEBSITE_ID = "website_info_id";
    private static final String COLUMN_NAME_FOR_QUESTION_ID = "question_id";
    private static final String COLUMN_NAME_FOR_LAST_UPDATE = "last_update_date_time";
    private JDBCStackOverflowAnswerDAO answerDAO;
    private JDBCStackOverflowCommentDAO commentDAO;

    public JDBCStackOverflowInfoDAO(DataSource dataSource,
            JDBCStackOverflowAnswerDAO answerDAO,
            JDBCStackOverflowCommentDAO commentDAO) {
        super(dataSource);
        this.answerDAO = answerDAO;
        this.commentDAO = commentDAO;
    }

    public Optional<Integer> findIdByLinkInfo(StackOverflowLinkInfo linkInfo) {
        List<Integer> ids = jdbcTemplate
            .queryForList(
                "select website_info_id from stackoverflow_info where question_id = ?",
                Integer.class,
                linkInfo.idQuestion()
            );
        return Optional.ofNullable((ids.isEmpty() ? null : ids.get(0)));
    }

    public void add(StackOverflowInfo newStackOverflowInfo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(COLUMN_NAME_FOR_QUESTION_ID, newStackOverflowInfo.getLinkInfo().idQuestion());
        paramMap.put(COLUMN_NAME_FOR_LAST_UPDATE, OffsetDateTime.now());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int websiteInfoId = namedParameterJdbcTemplate
            .queryForObject(
                "INSERT INTO website_info (type_id, last_update_date_time) "
                    + "VALUES ((select id from website_info_type where name = 'StackOverflow'), "
                    + ":last_update_date_time) RETURNING id;",
                paramMap,
                Integer.class
            );
        paramMap.put(COLUMN_NAME_FOR_WEBSITE_ID, websiteInfoId);
        namedParameterJdbcTemplate.update(
            "INSERT INTO stackoverflow_info (website_info_id, question_id) "
                + "VALUES (:website_info_id, :question_id);",
            paramMap
        );
        commentDAO.addAll(newStackOverflowInfo.getComments().values(), websiteInfoId);
        answerDAO.addAll(newStackOverflowInfo.getAnswers().values(), websiteInfoId);
        newStackOverflowInfo.setId(websiteInfoId);
    }

    public void applyChanges(ResultOfCompareStackOverflowInfo changes) {
        jdbcTemplate.update("UPDATE website_info SET last_update_date_time = ? WHERE id = ?",
                new Object[] {OffsetDateTime.now(), changes.getIdWebsiteInfo()});

        commentDAO.removeAll(Arrays.asList(changes.getDeletedComments()), changes.getIdWebsiteInfo());
        answerDAO.removeAll(Arrays.asList(changes.getDeletedAnswers()), changes.getIdWebsiteInfo());

        commentDAO.addAll(Arrays.stream(changes.getAddedComments())
                .map(StackOverflowCommentResponse::getStackOverflowComment).toList(),
                    changes.getIdWebsiteInfo());

        answerDAO.addAll(Arrays.stream(changes.getAddedAnswers())
                .map(StackOverflowAnswerResponse::getStackOverflowAnswer).toList(),
                    changes.getIdWebsiteInfo());

        answerDAO.updateLastEditForAll(Arrays.stream(changes.getEditedAnswers())
                .map(StackOverflowAnswerResponse::getStackOverflowAnswer).toList(),
                    changes.getIdWebsiteInfo());
    }

    public StackOverflowInfo getById(int idStackOverflowInfo) {
        StackOverflowInfo result = jdbcTemplate
            .queryForObject(
                "select * from website_info "
                    + " join stackoverflow_info ON stackoverflow_info.website_info_id = website_info.id "
                    + "where website_info.id = ? ",
                new Object[]{idStackOverflowInfo},
                (rs, rowNum) -> {
                    int id = rs.getInt(COLUMN_NAME_FOR_WEBSITE_ID);
                    OffsetDateTime lastUpdateTime = rs.getObject(COLUMN_NAME_FOR_LAST_UPDATE, OffsetDateTime.class);
                    int idQuestion = rs.getInt(COLUMN_NAME_FOR_QUESTION_ID);
                    StackOverflowLinkInfo linkInfo = new StackOverflowLinkInfo(idQuestion);
                    return new StackOverflowInfo(id, lastUpdateTime, linkInfo);
                });
        Map<Integer, StackOverflowComment> comments = commentDAO.findAll(idStackOverflowInfo)
            .stream().collect(Collectors.toMap(StackOverflowComment::getIdComment, i -> i));
        Map<Integer, StackOverflowAnswer> answers = answerDAO.findAll(idStackOverflowInfo)
            .stream().collect(Collectors.toMap(StackOverflowAnswer::getIdAnswer, i -> i));
        result.setAnswers(answers);
        result.setComments(comments);
        return result;
    }

    public StackOverflowLinkInfo loadLinkInfo(int idWebsiteInfo) {
        int idAnswer = jdbcTemplate
            .queryForObject(
                "select soi.question_id from stackoverflow_info soi "
                    + "where soi.website_info_id = ?",
                Integer.class,
                idWebsiteInfo);
        return new StackOverflowLinkInfo(idAnswer);
    }

    public void remove(int idWebsiteInfo) {
        jdbcTemplate.update("delete from website_info where id = ?", idWebsiteInfo);
    }
}
