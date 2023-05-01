package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowAnswerEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowAnswerPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAStackOverflowAnswerDAO extends JPADAO {
    @Transactional
    public void addAll(Collection<StackOverflowAnswerEntity> newAnswers, int idWebsiteInfo){
        for(StackOverflowAnswerEntity newAnswer : newAnswers){
            newAnswer.getPrimaryKey().setWebsiteInfoId(idWebsiteInfo);
            entityManager.persist(newAnswer);
        }
    }
    public void removeAll(Collection<StackOverflowAnswerPrimaryKey> idsForRemove){
        Query queryForRemoveAnswer = entityManager.createQuery("delete from StackOverflowAnswerEntity soa " +
                "where soa.primaryKey = :primaryKey");
        for(StackOverflowAnswerPrimaryKey idForRemove : idsForRemove){
            queryForRemoveAnswer
                    .setParameter("primaryKey", idForRemove)
                    .executeUpdate();
        }
    }
    public void update(Collection<StackOverflowAnswerEntity> answersForUpdate){
        for(StackOverflowAnswerEntity editedAnswer : answersForUpdate){
            entityManager.merge(editedAnswer);
        }
    }
}
