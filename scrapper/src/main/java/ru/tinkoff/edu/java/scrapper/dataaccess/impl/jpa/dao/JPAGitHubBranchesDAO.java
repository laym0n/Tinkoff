package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import java.util.Collection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubBranchEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubBranchPrimaryKey;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAGitHubBranchesDAO extends JPADAO {
    @Transactional
    public void addAll(Collection<GitHubBranchEntity> newBranches, int idWebsiteInfo) {
        for (GitHubBranchEntity newBranch : newBranches) {
            newBranch.getPrimaryKey().setGitHubSiteId(idWebsiteInfo);
            entityManager.persist(newBranch);
        }
    }

    public void removeAll(Collection<GitHubBranchPrimaryKey> idsForRemove) {
        Query queryForRemove = entityManager
            .createQuery(
                "delete from GitHubBranchEntity ghb "
                    + "where ghb.primaryKey = :primaryKey"
            );
        for (GitHubBranchPrimaryKey primaryKey : idsForRemove) {
            queryForRemove.setParameter("primaryKey", primaryKey).executeUpdate();
        }
    }
}
