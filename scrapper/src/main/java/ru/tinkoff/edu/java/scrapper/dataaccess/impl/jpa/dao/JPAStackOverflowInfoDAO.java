package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.*;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowAnswerPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowCommentPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.*;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAStackOverflowInfoDAO extends JPADAO {
    private JPAStackOverflowAnswerDAO answerDAO;
    private JPAStackOverflowCommentDAO commentDAO;

    public JPAStackOverflowInfoDAO(JPAStackOverflowAnswerDAO answerDAO, JPAStackOverflowCommentDAO commentDAO) {
        this.answerDAO = answerDAO;
        this.commentDAO = commentDAO;
    }

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

        answerDAO.addAll(newStackOverflowInfo.getAnswers(), newStackOverflowInfo.getId());

        commentDAO.addAll(newStackOverflowInfo.getComments(), newStackOverflowInfo.getId());
    }
    @Transactional
    public void applyChanges(ResultOfCompareStackOverflowInfo changes){
        removeComments(changes.getDeletedComments(), changes.getIdWebsiteInfo());

        removeAnswers(changes.getDeletedAnswers(), changes.getIdWebsiteInfo());

        updateAnswers(changes.getEditedAnswers(), changes.getIdWebsiteInfo());

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
    private void removeComments(StackOverflowComment[] deletedComments, int idWebsiteInfo){
        Collection<StackOverflowCommentPrimaryKey> idsForRemoveComments =
                Arrays.stream(deletedComments)
                        .map(i-> new StackOverflowCommentPrimaryKey(i.getIdComment(), idWebsiteInfo))
                        .toList();
        commentDAO.removeAll(idsForRemoveComments);
    }
    private void removeAnswers(StackOverflowAnswer[] deletedAnswers, int idWebsiteInfo){
        Collection<StackOverflowAnswerPrimaryKey> idsForRemoveAnswers =
                Arrays.stream(deletedAnswers)
                        .map(i-> new StackOverflowAnswerPrimaryKey(i.getIdAnswer(), idWebsiteInfo))
                        .toList();
        answerDAO.removeAll(idsForRemoveAnswers);
    }
    private void updateAnswers(StackOverflowAnswerResponse[] editedAnswers, int idWebsiteInfo){
        Collection<StackOverflowAnswerEntity> updatedAnswers = Arrays.stream(editedAnswers)
                .map(StackOverflowAnswerResponse::getStackOverflowAnswer)
                .map(i->new StackOverflowAnswerEntity(i, idWebsiteInfo))
                .toList();
        answerDAO.update(updatedAnswers);
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
