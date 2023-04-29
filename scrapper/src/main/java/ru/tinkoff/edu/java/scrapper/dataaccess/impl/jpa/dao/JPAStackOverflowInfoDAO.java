package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.*;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowAnswerPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAStackOverflowInfoDAO extends JPADAO {
    public Optional<Integer> findIdByLinkInfo(StackOverflowLinkInfo linkInfo){
        Query query = entityManager.createQuery("select soi.id from StackOverflowInfoEntity soi " +
                "where soi.questionId = :questionId");
        query.setParameter("questionId", linkInfo.idQuestion());
        List<Integer> ids = query.getResultList();
        return Optional.ofNullable((ids.size()) == 0? null : ids.get(0));
    }
    @Transactional
    public void add(StackOverflowInfoEntity newStackOverflowInfo){
        WebsiteInfoTypeEntity type = entityManager.createQuery("select wsit from WebsiteInfoTypeEntity wsit " +
                "where wsit.name = 'StackOverflow'", WebsiteInfoTypeEntity.class).getSingleResult();
        newStackOverflowInfo.setTypeOfWebsite(type);
        entityManager.persist(newStackOverflowInfo);
        for(StackOverflowAnswerEntity answer : newStackOverflowInfo.getAnswers()){
            answer.getPrimaryKey().setWebsiteInfoId(newStackOverflowInfo.getId());
            entityManager.persist(answer);
        }
        for(StackOverflowCommentEntity comment : newStackOverflowInfo.getComments()){
            comment.getPrimaryKey().setWebsiteInfoId(newStackOverflowInfo.getId());
            entityManager.persist(comment);
        }
    }
    public void applyChanges(ResultOfCompareStackOverflowInfo changes){
        Query queryForDeleteAnswers = entityManager.createQuery("delete from StackOverflowAnswerEntity soa " +
            "where soa.primaryKey.id = :answerId " +
            "and soa.primaryKey.websiteInfoId = :websiteInfoId")
                .setParameter("websiteInfoId", changes.getIdWebsiteInfo());
        for(StackOverflowAnswer deletedAnswer : changes.getDeletedAnswers()){
            queryForDeleteAnswers.setParameter("answerId", deletedAnswer.getIdAnswer());
            queryForDeleteAnswers.executeUpdate();
        }
        Query queryForDeleteComments = entityManager.createQuery("delete from StackOverflowCommentEntity stc " +
                "where stc.primaryKey.websiteInfoId = :websiteInfoId " +
                "and stc.primaryKey.id = :commentId")
                .setParameter("websiteInfoId", changes.getIdWebsiteInfo());
        for(StackOverflowComment deletedComment : changes.getDeletedComments()){
            queryForDeleteComments.setParameter("commentId", deletedComment.getIdComment());
            queryForDeleteComments.executeUpdate();
        }

        for(StackOverflowAnswerResponse editedAnswer : changes.getEditedAnswers()){
            StackOverflowAnswerEntity newSavedAnswer = new StackOverflowAnswerEntity(
                    new StackOverflowAnswerPrimaryKey(editedAnswer.getAnswerId(), changes.getIdWebsiteInfo()),
                    editedAnswer.getStackOverflowAnswer().getUserName(),
                    Timestamp.valueOf(editedAnswer.getLastEditDate().toLocalDateTime())
            );
            entityManager.merge(newSavedAnswer);
        }


        Arrays.stream(changes.getAddedAnswers()).map(StackOverflowAnswerResponse::getStackOverflowAnswer)
                .forEach(i-> entityManager.persist(new StackOverflowAnswerEntity(i, changes.getIdWebsiteInfo())));

        Arrays.stream(changes.getAddedComments()).map(StackOverflowCommentResponse::getStackOverflowComment)
                .forEach(i->entityManager.persist(new StackOverflowCommentEntity(i, changes.getIdWebsiteInfo())));

        entityManager.createQuery("update WebsiteInfoEntity wi " +
                    "set wi.lastCheckUpdate = :lastCheckUpdate " +
                    "where wi.id = :id")
                .setParameter("id", changes.getIdWebsiteInfo())
                .setParameter("lastCheckUpdate", Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime()))
                .executeUpdate();
        entityManager.flush();
        entityManager.detach(entityManager.getReference(StackOverflowInfoEntity.class, changes.getIdWebsiteInfo()));
    }
    public StackOverflowInfoEntity getById(int idStackOverflowInfo){
        return entityManager.find(StackOverflowInfoEntity.class, idStackOverflowInfo);
    }

    public StackOverflowLinkInfo loadLinkInfo(int idWebsiteInfo) {
        int idQuestion = entityManager.createQuery("select soi.questionId from StackOverflowInfoEntity soi " +
                "where soi.id = :id", Integer.class).setParameter("id", idWebsiteInfo).getSingleResult();
        return new StackOverflowLinkInfo(idQuestion);
    }

    public void remove(int idWebsiteInfo) {
        entityManager.createQuery("delete From StackOverflowInfoEntity soi where soi.id = :id")
                .setParameter("id", idWebsiteInfo).executeUpdate();
    }
}
