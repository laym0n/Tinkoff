package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubBranchEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubCommitEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.WebsiteInfoTypeEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubBranchPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubCommitPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;


@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@AllArgsConstructor
public class JPAGitHubInfoDAO extends JPADAO {
    private static final String NAME_OF_PARAMETER_FOR_WEBSITE_ID = "id";
    private static final String NAME_OF_PARAMETER_FOR_LAST_CHECK_UPDATE = "lastCheckUpdate";
    private static final String NAME_OF_PARAMETER_FOR_LAST_ACTIVITY = "lastActiveTime";
    private static final String NAME_OF_PARAMETER_FOR_REPOSITORY_NAME = "repositoryName";
    private static final String NAME_OF_PARAMETER_FOR_USER_NAME = "userName";
    private JPAGitHubBranchesDAO branchesDAO;
    private JPAGitHubCommitDAO commitDAO;

    public Optional<Integer> findIdByUserNameAndRepositoryName(String userName, String repositoryName) {
        Query query = entityManager
            .createQuery("select ghi.id from GitHubInfoEntity ghi "
                + "where ghi.repositoryName = :repositoryName and ghi.userName = :userName"
            );
        query.setParameter(NAME_OF_PARAMETER_FOR_REPOSITORY_NAME, repositoryName);
        query.setParameter(NAME_OF_PARAMETER_FOR_USER_NAME, userName);
        List<Integer> ids = query.getResultList();
        return Optional.ofNullable((ids.size()) == 0 ? null : ids.get(0));
    }

    @Transactional
    public void add(GitHubInfoEntity newGitHubInfo) {
        WebsiteInfoTypeEntity type = entityManager
            .createQuery(
                "select wsit from WebsiteInfoTypeEntity wsit "
                    + "where wsit.name = 'GitHub'",
                WebsiteInfoTypeEntity.class
            ).getSingleResult();
        newGitHubInfo.setTypeOfWebsite(type);
        entityManager.persist(newGitHubInfo);
        branchesDAO.addAll(newGitHubInfo.getBranches(), newGitHubInfo.getId());
        commitDAO.addAll(newGitHubInfo.getCommits(), newGitHubInfo.getId());
    }

    public void remove(int idWebsiteInfo) {
        entityManager.createQuery("delete from GitHubInfoEntity ghi where ghi.id = :id")
                        .setParameter(NAME_OF_PARAMETER_FOR_WEBSITE_ID, idWebsiteInfo).executeUpdate();
    }

    public GitHubInfoEntity getById(int idGitHubInfo) {
        return entityManager
            .createQuery(
                "select ghi from GitHubInfoEntity ghi where ghi.id = :id",
                GitHubInfoEntity.class)
            .setParameter(NAME_OF_PARAMETER_FOR_WEBSITE_ID, idGitHubInfo).getSingleResult();
    }

    @Transactional
    public void applyChanges(ResultOfCompareGitHubInfo changes) {
        Collection<GitHubBranchPrimaryKey> idsForRemoveBranches =
                Arrays.stream(changes.getDroppedBranches())
                        .map(i -> new GitHubBranchPrimaryKey(i.getBranchName(), changes.getIdWebsiteInfo())).toList();
        branchesDAO.removeAll(idsForRemoveBranches);

        Collection<GitHubCommitPrimaryKey> idsForRemoveCommits = Arrays.stream(changes.getDroppedCommits())
                .map(i -> new GitHubCommitPrimaryKey(i.getSha(), changes.getIdWebsiteInfo())).toList();
        commitDAO.removeAll(idsForRemoveCommits);

        Arrays.stream(changes.getAddedBranches()).map(GitHubBranchResponse::getGitHubBranch)
                .forEach(i -> entityManager.persist(new GitHubBranchEntity(i, changes.getIdWebsiteInfo())));

        Arrays.stream(changes.getPushedCommits()).map(GitHubCommitResponse::getGitHubCommit)
                .forEach(i -> entityManager.persist(new GitHubCommitEntity(i, changes.getIdWebsiteInfo())));

        updateLastEditAndLastCheckUpdate(changes.getLastActivityDate(), changes.getIdWebsiteInfo());

        entityManager.flush();
        entityManager.detach(entityManager.getReference(GitHubInfoEntity.class, changes.getIdWebsiteInfo()));
    }

    private void updateLastEditAndLastCheckUpdate(Optional<OffsetDateTime> lastActivityDate, int idWebsiteInfo) {
        Query queryForUpdateGitHubInfo;
        if (lastActivityDate.isPresent()) {
            queryForUpdateGitHubInfo = entityManager
                    .createQuery("update GitHubInfoEntity ghi set ghi.lastActiveTime = :lastActiveTime, "
                        + "ghi.lastCheckUpdate = :lastCheckUpdate where ghi.id = :id")
                    .setParameter(NAME_OF_PARAMETER_FOR_LAST_ACTIVITY,
                        Timestamp.valueOf(lastActivityDate.get().toLocalDateTime()));
        } else {
            queryForUpdateGitHubInfo = entityManager.createQuery("update WebsiteInfoEntity wi "
                + "set wi.lastCheckUpdate = :lastCheckUpdate "
                + "where wi.id = :id");
        }
        queryForUpdateGitHubInfo
                .setParameter(NAME_OF_PARAMETER_FOR_WEBSITE_ID, idWebsiteInfo)
                .setParameter(NAME_OF_PARAMETER_FOR_LAST_CHECK_UPDATE,
                    Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime()))
                .executeUpdate();
    }

    public GitHubLinkInfo getLinkInfoById(int idWebsiteInfo) {
        Object[] results = entityManager.createQuery(
            "select ghi.userName, ghi.repositoryName from GitHubInfoEntity ghi "
                + "where ghi.id = :id", Object[].class)
            .setParameter(NAME_OF_PARAMETER_FOR_WEBSITE_ID, idWebsiteInfo)
            .getSingleResult();
        return new GitHubLinkInfo((String) results[0], (String) results[1]);
    }
}
