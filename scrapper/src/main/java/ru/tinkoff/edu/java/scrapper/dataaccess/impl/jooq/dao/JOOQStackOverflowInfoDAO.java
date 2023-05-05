package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.StackoverflowInfoRecord;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.WebsiteInfoRecord;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQStackOverflowInfoDAO extends JOOQDAO {
    private static final String WEBSITE_TYPE = "StackOverflow";
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.StackoverflowInfo stackoverflowInfo =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.StackoverflowInfo.STACKOVERFLOW_INFO;

    private JOOQWebsiteInfoDAO websiteInfoDAO;
    private JOOQStackOverflowAnswerDAO answerDAO;
    private JOOQStackOverflowCommentDAO commentDAO;

    public JOOQStackOverflowInfoDAO(
        JOOQWebsiteInfoDAO websiteInfoDAO,
        JOOQStackOverflowAnswerDAO answerDAO,
        JOOQStackOverflowCommentDAO commentDAO
    ) {
        this.websiteInfoDAO = websiteInfoDAO;
        this.answerDAO = answerDAO;
        this.commentDAO = commentDAO;
    }

    public Optional<Integer> findIdByLinkInfo(StackOverflowLinkInfo linkInfo) {
        return Optional.ofNullable(
            context.select(stackoverflowInfo.WEBSITE_INFO_ID)
                .from(stackoverflowInfo)
                .where(stackoverflowInfo.QUESTION_ID.eq(linkInfo.idQuestion()))
                .fetchOne(stackoverflowInfo.WEBSITE_INFO_ID)
        );
    }

    public void add(StackOverflowInfo newStackOverflowInfo) {
        int idNewWebsite = websiteInfoDAO.create(WEBSITE_TYPE);
        StackoverflowInfoRecord stackoverflowInfoRecord = context.newRecord(stackoverflowInfo);
        stackoverflowInfoRecord.setWebsiteInfoId(idNewWebsite);
        stackoverflowInfoRecord.setQuestionId(newStackOverflowInfo.getLinkInfo().idQuestion());
        stackoverflowInfoRecord.store();

        answerDAO.addAll(newStackOverflowInfo.getAnswers().values(), idNewWebsite);
        commentDAO.addAll(newStackOverflowInfo.getComments().values(), idNewWebsite);

        newStackOverflowInfo.setId(stackoverflowInfoRecord.getWebsiteInfoId());
    }

    public Optional<StackOverflowInfo> getById(int idStackOverflowInfo) {
        Optional<WebsiteInfoRecord> websiteInfoRecord = websiteInfoDAO.findById(idStackOverflowInfo);
        if (websiteInfoRecord.isEmpty()) {
            return Optional.empty();
        }
        StackoverflowInfoRecord loadedRecord = context
            .fetchOne(
                stackoverflowInfo,
                stackoverflowInfo.WEBSITE_INFO_ID.eq(idStackOverflowInfo)
            );
        StackOverflowInfo result = new StackOverflowInfo(
            loadedRecord.getWebsiteInfoId(),
            OffsetDateTime.of(websiteInfoRecord.get().getLastUpdateDateTime(), ZoneOffset.MIN),
            new StackOverflowLinkInfo(loadedRecord.getQuestionId())
        );
        result.setAnswersByCollection(answerDAO.findAll(idStackOverflowInfo));
        result.setCommentsByCollection(commentDAO.findAll(idStackOverflowInfo));
        return Optional.of(result);
    }

    public void applyChanges(ResultOfCompareStackOverflowInfo changes) {
        websiteInfoDAO.updateLastCheckUpdateForWebsite(changes.getIdWebsiteInfo());

        answerDAO.addAll(Arrays.stream(changes.getAddedAnswers())
            .map(StackOverflowAnswerResponse::getStackOverflowAnswer).toList(), changes.getIdWebsiteInfo());
        answerDAO.updateLastEditForAll(Arrays.stream(changes.getEditedAnswers())
            .map(StackOverflowAnswerResponse::getStackOverflowAnswer).toList(), changes.getIdWebsiteInfo());
        answerDAO.removeAll(Arrays.asList(changes.getDeletedAnswers()), changes.getIdWebsiteInfo());

        commentDAO.addAll(Arrays.stream(changes.getAddedComments())
            .map(StackOverflowCommentResponse::getStackOverflowComment).toList(), changes.getIdWebsiteInfo());
        commentDAO.removeAll(Arrays.asList(changes.getDeletedComments()), changes.getIdWebsiteInfo());
    }

    public StackOverflowLinkInfo loadLinkInfo(int idWebsiteInfo) {
        Result<Record1<Integer>> result = context
            .select(stackoverflowInfo.QUESTION_ID)
            .from(stackoverflowInfo)
            .where(stackoverflowInfo.WEBSITE_INFO_ID.eq(idWebsiteInfo))
            .fetch();
        return new StackOverflowLinkInfo(result.get(0).get(stackoverflowInfo.QUESTION_ID));
    }

    public void remove(int idWebsiteInfo) {
        context.delete(stackoverflowInfo)
            .where(stackoverflowInfo.WEBSITE_INFO_ID.eq(idWebsiteInfo))
            .execute();
    }
}
