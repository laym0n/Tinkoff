package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import java.util.Collection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowCommentEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowCommentPrimaryKey;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAStackOverflowCommentDAO extends JPADAO {
    @Transactional
    public void addAll(Collection<StackOverflowCommentEntity> newComments, int idWebsiteInfo) {
        for (StackOverflowCommentEntity newComment : newComments) {
            newComment.getPrimaryKey().setWebsiteInfoId(idWebsiteInfo);
            entityManager.persist(newComment);
        }
    }

    public void removeAll(Collection<StackOverflowCommentPrimaryKey> idsForRemove) {
        Query queryForRemove = entityManager.createQuery("delete from StackOverflowCommentEntity soc "
            + "where soc.primaryKey = :primaryKey");
        for (StackOverflowCommentPrimaryKey idForRemove : idsForRemove) {
            queryForRemove
                    .setParameter("primaryKey", idForRemove)
                    .executeUpdate();
        }
    }
}
