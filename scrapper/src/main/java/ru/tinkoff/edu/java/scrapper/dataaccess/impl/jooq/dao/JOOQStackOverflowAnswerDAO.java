package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.StackOverflowAnswerRecord;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQStackOverflowAnswerDAO extends JOOQDAO {
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.StackOverflowAnswer stackOverflowAnswer =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.StackOverflowAnswer.STACK_OVERFLOW_ANSWER;

    public void addAll(Collection<StackOverflowAnswer> newAnswers, int idWebsiteInfo) {
        context.batchInsert(
            getRecordsAnswersFromBLLAnswers(newAnswers, idWebsiteInfo)
        ).execute();
    }

    public void removeAll(Collection<StackOverflowAnswer> answersForRemove, int idWebsiteInfo) {
        context.batchDelete(
            getRecordsAnswersFromBLLAnswers(answersForRemove, idWebsiteInfo)
        ).execute();
    }

    public Collection<StackOverflowAnswer> findAll(int idStackOverflowInfo) {
        List<StackOverflowAnswerRecord> loadedAnswers = context
            .selectFrom(stackOverflowAnswer)
            .where(stackOverflowAnswer.WEBSITE_INFO_ID.eq(idStackOverflowInfo))
            .fetchInto(StackOverflowAnswerRecord.class);
        return getBLLAnswersFromRecordsAnswers(loadedAnswers);
    }

    public void updateLastEditForAll(List<StackOverflowAnswer> answers, int idWebsiteInfo) {
        context.batchUpdate(
            getRecordsAnswersFromBLLAnswers(answers, idWebsiteInfo)
        ).execute();
    }

    private List<StackOverflowAnswer> getBLLAnswersFromRecordsAnswers(
        List<StackOverflowAnswerRecord> answers
    ) {
        return answers.stream()
            .map(answerRecord ->
                new StackOverflowAnswer(
                    answerRecord.getId(),
                    answerRecord.getUserName(),
                    OffsetDateTime.of(answerRecord.getLastEditDateTime(), ZoneOffset.MIN)
                )
            ).toList();
    }

    private List<StackOverflowAnswerRecord> getRecordsAnswersFromBLLAnswers(
        Collection<StackOverflowAnswer> answers,
        int idWebsiteInfo
    ) {
        return answers.stream()
            .map(answer -> {
                StackOverflowAnswerRecord newRecord = context.newRecord(stackOverflowAnswer);
                newRecord.setUserName(answer.getUserName());
                newRecord.setId(answer.getIdAnswer());
                newRecord.setWebsiteInfoId(idWebsiteInfo);
                newRecord.setLastEditDateTime(answer.getLastEditDate().toLocalDateTime());
                return newRecord;
            }).toList();
    }
}
