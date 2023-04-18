package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.*;

public class JDBCStackOverflowInfoDAO extends JDBCDAO{
    private JDBCStackOverflowAnswerDAO answerDAO;
    private JDBCStackOverflowCommentDAO commentDAO;

    public JDBCStackOverflowInfoDAO(DataSource dataSource, JDBCStackOverflowAnswerDAO answerDAO, JDBCStackOverflowCommentDAO commentDAO) {
        super(dataSource);
        this.answerDAO = answerDAO;
        this.commentDAO = commentDAO;
    }

    public String getQueryForFindIdByLinkInfo(StackOverflowLinkInfo linkInfo) {
        return "select website_info_id from stackoverflow_info where answer_id = " + linkInfo.idQuestion();
    }
    public Optional<Integer> findIdByLinkInfo(StackOverflowLinkInfo linkInfo){
        Integer id = jdbcTemplate.queryForObject(getQueryForFindIdByLinkInfo(linkInfo), Integer.class);
        return Optional.ofNullable(id);
    }
    public void add(StackOverflowInfo newStackOverflowInfo){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("answer_id", newStackOverflowInfo.getLinkInfo().idQuestion());
        paramMap.put("last_update_date_time", OffsetDateTime.now());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int websiteInfoId = namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO website_info (type_id, last_update_date_time) " +
                        "VALUES ((select id from website_info_type where name = 'StackOverflow'), " +
                        ":last_update_date_time) RETURNING id;",
                paramMap, Integer.class);
        paramMap.put("website_info_id", websiteInfoId);
        namedParameterJdbcTemplate.update(
                "INSERT INTO stackoverflow_info (website_info_id, answer_id) " +
                        "VALUES (:website_info_id, :answer_id);", paramMap);
        commentDAO.addAll(newStackOverflowInfo.getComments().values(), websiteInfoId);
        answerDAO.addAll(newStackOverflowInfo.getAnswers().values(), websiteInfoId);
    }
    public void applyChanges(ResultOfCompareStackOverflowInfo changes){
        jdbcTemplate.update("UPDATE website_info SET last_update_date_time = ? WHERE id = ?",
                new Object[] {OffsetDateTime.now(), changes.getIdWebsiteInfo()});
        commentDAO.removeAll(Arrays.asList(changes.getDeletedComments()), changes.getIdWebsiteInfo());
        answerDAO.removeAll(Arrays.asList(changes.getDeletedAnswers()), changes.getIdWebsiteInfo());
        commentDAO.addAll(Arrays.stream(changes.getAddedComments())
                .map(StackOverflowCommentResponse::getStackOverflowComment).toList(), changes.getIdWebsiteInfo());
        answerDAO.addAll(Arrays.stream(changes.getAddedAnswers())
                .map(StackOverflowAnswerResponse::getStackOverflowAnswer).toList(), changes.getIdWebsiteInfo());
    }
}
