package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import java.util.Collection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubCommitEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubCommitPrimaryKey;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAGitHubCommitDAO extends JPADAO {
    @Transactional
    public void addAll(Collection<GitHubCommitEntity> newCommits, int idWebsiteInfo) {
        for (GitHubCommitEntity newCommit : newCommits) {
            newCommit.getPrimaryKey().setGitHubInfoId(idWebsiteInfo);
            entityManager.persist(newCommit);
        }
    }

    public void removeAll(Collection<GitHubCommitPrimaryKey> idsForRemove) {
        Query queryForRemoveCommit = entityManager
                .createQuery("delete from GitHubCommitEntity ghc where ghc.primaryKey = :primaryKey");
        for (GitHubCommitPrimaryKey primaryKey : idsForRemove) {
            queryForRemoveCommit.setParameter("primaryKey", primaryKey).executeUpdate();
        }
    }
}
