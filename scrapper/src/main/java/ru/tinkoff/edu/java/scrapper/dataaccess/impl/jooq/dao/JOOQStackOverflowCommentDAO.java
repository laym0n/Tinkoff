package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.StackOverflowCommentRecord;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQStackOverflowCommentDAO extends JOOQDAO {
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.StackOverflowComment stackOverflowComment =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.StackOverflowComment.STACK_OVERFLOW_COMMENT;

    public void addAll(Collection<StackOverflowComment> newComments, int idWebsiteInfo) {
        context.batchInsert(
            getRecordCommentsFromBLLComments(newComments, idWebsiteInfo)
        ).execute();
    }

    public void removeAll(Collection<StackOverflowComment> commentsForRemove, int idWebsiteInfo) {
        context.batchDelete(
            getRecordCommentsFromBLLComments(commentsForRemove, idWebsiteInfo)
        ).execute();
    }

    public Collection<StackOverflowComment> findAll(int idStackOverflowInfo) {
        List<StackOverflowCommentRecord> loadedComments = context
            .selectFrom(stackOverflowComment)
            .where(stackOverflowComment.WEBSITE_INFO_ID.eq(idStackOverflowInfo))
            .fetchInto(StackOverflowCommentRecord.class);
        return getBLLCommentsFromRecordsComment(loadedComments);
    }

    private List<StackOverflowComment> getBLLCommentsFromRecordsComment(
        List<StackOverflowCommentRecord> commentRecords
    ) {
        return commentRecords.stream()
            .map(commentRecord ->
                new StackOverflowComment(
                    commentRecord.getId()
                )
            ).toList();
    }

    private List<StackOverflowCommentRecord> getRecordCommentsFromBLLComments(
        Collection<StackOverflowComment> comments,
        int idWebsiteInfo
    ) {
        return comments.stream()
            .map(comment -> {
                StackOverflowCommentRecord newRecord = context.newRecord(stackOverflowComment);
                newRecord.setId(comment.getIdComment());
                newRecord.setWebsiteInfoId(idWebsiteInfo);
                return newRecord;
            }).toList();
    }
}
