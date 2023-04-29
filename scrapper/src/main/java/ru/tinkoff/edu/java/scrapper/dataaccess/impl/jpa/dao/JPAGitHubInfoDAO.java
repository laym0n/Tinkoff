package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubBranchEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubCommitEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.WebsiteInfoTypeEntity;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.*;


@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAGitHubInfoDAO extends JPADAO {
    public Optional<Integer> findIdByUserNameAndRepositoryName(String userName, String repositoryName){
        Query query = entityManager.createQuery("select ghi.id from GitHubInfoEntity ghi " +
                "where ghi.repositoryName = :repositoryName and ghi.userName = :userName");
        query.setParameter("repositoryName", repositoryName);
        query.setParameter("userName", userName);
        List<Integer> ids = query.getResultList();
        return Optional.ofNullable((ids.size()) == 0? null : ids.get(0));
    }
    @Transactional
    public void add(GitHubInfoEntity newGitHubInfo){
        WebsiteInfoTypeEntity type = entityManager.createQuery("select wsit from WebsiteInfoTypeEntity wsit " +
                "where wsit.name = 'GitHub'", WebsiteInfoTypeEntity.class).getSingleResult();
        newGitHubInfo.setTypeOfWebsite(type);
        entityManager.persist(newGitHubInfo);
        for(GitHubCommitEntity commit : newGitHubInfo.getCommits()){
            commit.getPrimaryKey().setGitHubInfoId(newGitHubInfo.getId());
            entityManager.persist(commit);
        }
        for(GitHubBranchEntity branch : newGitHubInfo.getBranches()){
            branch.getPrimaryKey().setGitHubSiteId(newGitHubInfo.getId());
            entityManager.persist(branch);
        }
    }
    public void remove(int idWebsiteInfo){
        entityManager.createQuery("delete from GitHubInfoEntity ghi where ghi.id = :id")
                        .setParameter("id", idWebsiteInfo).executeUpdate();
    }
    public GitHubInfoEntity getById(int idGitHubInfo){
        return entityManager.createQuery("select ghi from GitHubInfoEntity ghi where ghi.id = :id", GitHubInfoEntity.class)
                .setParameter("id", idGitHubInfo).getSingleResult();
    }
    @Transactional
    public void applyChanges(ResultOfCompareGitHubInfo changes){
        Query queryForRemoveBranches = entityManager.createQuery("delete from GitHubBranchEntity ghb " +
                "where ghb.id.gitHubSiteId = :id " +
                "and ghb.id.name = :branchName");
        for(GitHubBranch branchForRemove : changes.getDroppedBranches()){
            queryForRemoveBranches.setParameter("id", changes.getIdWebsiteInfo());
            queryForRemoveBranches.setParameter("branchName", branchForRemove.getBranchName());
            queryForRemoveBranches.executeUpdate();
        }
        Query queryForRemoveCommits = entityManager.createQuery("delete from GitHubCommitEntity ghc " +
                "where ghc.primaryKey.gitHubInfoId = :id " +
                "and ghc.primaryKey.sha = :sha");
        for(GitHubCommit commitForRemove : changes.getDroppedCommits()){
            queryForRemoveCommits.setParameter("id", changes.getIdWebsiteInfo());
            queryForRemoveCommits.setParameter("sha", commitForRemove.getSha());
            queryForRemoveCommits.executeUpdate();
        }

        Arrays.stream(changes.getAddedBranches()).map(GitHubBranchResponse::getGitHubBranch)
                .forEach(i-> entityManager.persist(new GitHubBranchEntity(i, changes.getIdWebsiteInfo())));

        Arrays.stream(changes.getPushedCommits()).map(GitHubCommitResponse::getGitHubCommit)
                .forEach(i->entityManager.persist(new GitHubCommitEntity(i, changes.getIdWebsiteInfo())));

        Query queryForUpdateGitHubInfo;
        if(changes.getLastActivityDate().isPresent()){
            queryForUpdateGitHubInfo = entityManager
                    .createQuery("update GitHubInfoEntity ghi set ghi.lastActiveTime = :lastActiveTime, " +
                            "ghi.lastCheckUpdate = :lastCheckUpdate " +
                            "where ghi.id = :id")
                    .setParameter("lastActiveTime", Timestamp.valueOf(changes.getLastActivityDate().get().toLocalDateTime()));
        }
        else{
            queryForUpdateGitHubInfo = entityManager.createQuery("update WebsiteInfoEntity wi " +
                    "set wi.lastCheckUpdate = :lastCheckUpdate " +
                    "where wi.id = :id");
        }
        queryForUpdateGitHubInfo
                .setParameter("id", changes.getIdWebsiteInfo())
                .setParameter("lastCheckUpdate", Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime()))
                .executeUpdate();
        entityManager.flush();
        entityManager.detach(entityManager.getReference(GitHubInfoEntity.class, changes.getIdWebsiteInfo()));
    }

    public GitHubLinkInfo getLinkInfoById(int idWebsiteInfo) {
        Object[] results = entityManager.createQuery("select ghi.userName, ghi.repositoryName from GitHubInfoEntity ghi " +
                "where ghi.id = :id", Object[].class).setParameter("id", idWebsiteInfo)
                .getSingleResult();
        return new GitHubLinkInfo((String) results[0], (String)results[1]);
    }
}