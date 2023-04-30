package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubCommitEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubCommitPrimaryKey;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAGitHubCommitDAO extends JPADAO {
    public void addAll(Collection<GitHubCommitEntity> newCommits, int idWebsiteInfo){
        for(GitHubCommitEntity newCommit : newCommits){
            newCommit.getPrimaryKey().setGitHubInfoId(idWebsiteInfo);
            entityManager.persist(newCommit);
        }
    }
    public void removeAll(Collection<GitHubCommitPrimaryKey> idsForRemove){
        Query queryForRemoveCommit = entityManager
                .createQuery("delete from GitHubCommitEntity ghc where ghc.primaryKey = :primaryKey");
        for(GitHubCommitPrimaryKey primaryKey : idsForRemove){
            queryForRemoveCommit.setParameter("primaryKey", primaryKey).executeUpdate();
        }
    }
}
