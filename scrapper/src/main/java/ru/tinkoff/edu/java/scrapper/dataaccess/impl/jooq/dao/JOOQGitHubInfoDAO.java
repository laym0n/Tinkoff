package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;
import org.jooq.Condition;
import org.jooq.Record2;
import org.jooq.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.GithubInfoRecord;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.WebsiteInfoRecord;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQGitHubInfoDAO extends JOOQDAO {
    private static final String WEBSITE_TYPE = "GitHub";
    private JOOQGitHubBranchesDAO branchesDAO;
    private JOOQGitHubCommitDAO commitDAO;
    private JOOQWebsiteInfoDAO websiteInfoDAO;
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.GithubInfo githubInfo
        = ru.tinkoff.edu.java.scrapper.domain.jooq.tables.GithubInfo.GITHUB_INFO;

    public JOOQGitHubInfoDAO(
        JOOQGitHubBranchesDAO branchesDAO,
        JOOQGitHubCommitDAO commitDAO,
        JOOQWebsiteInfoDAO websiteInfoDAO
    ) {
        this.branchesDAO = branchesDAO;
        this.commitDAO = commitDAO;
        this.websiteInfoDAO = websiteInfoDAO;
    }

    public Optional<Integer> findIdByLinkInfo(GitHubLinkInfo linkInfo) {
        return Optional.ofNullable(
            context.select(githubInfo.WEBSITE_INFO_ID)
                .from(githubInfo)
                .where(new Condition[] {githubInfo.USER_NAME.eq(linkInfo.userName()),
                    githubInfo.REPOSITORY_NAME.eq(linkInfo.repositoryName())})
                .fetchOne(githubInfo.WEBSITE_INFO_ID)
        );
    }

    public void add(GitHubInfo newGitHubInfo) {
        int idNewWebsite = websiteInfoDAO.create(WEBSITE_TYPE);
        GithubInfoRecord githubInfoRecord = context.newRecord(githubInfo);
        githubInfoRecord.setWebsiteInfoId(idNewWebsite);
        githubInfoRecord.setLastActivityDateTime(newGitHubInfo.getLastActiveTime().toLocalDateTime());
        githubInfoRecord.setUserName(newGitHubInfo.getLinkInfo().userName());
        githubInfoRecord.setRepositoryName(newGitHubInfo.getLinkInfo().repositoryName());
        githubInfoRecord.store();

        commitDAO.addAll(newGitHubInfo.getCommits().values(), idNewWebsite);
        branchesDAO.addAll(newGitHubInfo.getBranches().values(), idNewWebsite);
        newGitHubInfo.setId(githubInfoRecord.getWebsiteInfoId());
    }

    public void remove(int idWebsiteInfo) {
        context.delete(githubInfo)
            .where(githubInfo.WEBSITE_INFO_ID.eq(idWebsiteInfo))
            .execute();
    }

    public Optional<GitHubInfo> getById(int idGitHubInfo) {
        Optional<WebsiteInfoRecord> websiteInfoRecord = websiteInfoDAO.findById(idGitHubInfo);
        if (websiteInfoRecord.isEmpty()) {
            return Optional.empty();
        }
        GithubInfoRecord githubInfoRecord = context.fetchOne(githubInfo, githubInfo.WEBSITE_INFO_ID.eq(idGitHubInfo));
        GitHubInfo result = new GitHubInfo(
            idGitHubInfo,
            OffsetDateTime.of(websiteInfoRecord.get().getLastUpdateDateTime(), ZoneOffset.MIN),
            new GitHubLinkInfo(githubInfoRecord.getUserName(), githubInfoRecord.getRepositoryName()),
            OffsetDateTime.of(githubInfoRecord.getLastActivityDateTime(), ZoneOffset.MIN)
        );
        result.setCommitsByCollection(commitDAO.findAll(idGitHubInfo));
        result.setBranchesByCollection(branchesDAO.findAll(idGitHubInfo));
        return Optional.of(result);
    }

    public void applyChanges(ResultOfCompareGitHubInfo changes) {
        websiteInfoDAO.updateLastCheckUpdateForWebsite(changes.getIdWebsiteInfo());
        context.update(githubInfo)
            .set(githubInfo.LAST_ACTIVITY_DATE_TIME, changes.getLastActivityDate().get().toLocalDateTime())
            .execute();

        commitDAO.addAll(Arrays.stream(changes.getPushedCommits())
            .map(GitHubCommitResponse::getGitHubCommit).toList(), changes.getIdWebsiteInfo());
        commitDAO.removeAll(Arrays.asList(changes.getDroppedCommits()), changes.getIdWebsiteInfo());

        branchesDAO.addAll(Arrays.stream(changes.getAddedBranches())
            .map(GitHubBranchResponse::getGitHubBranch).toList(), changes.getIdWebsiteInfo());
        branchesDAO.removeAll(Arrays.asList(changes.getDroppedBranches()), changes.getIdWebsiteInfo());
    }

    public GitHubLinkInfo getLinkInfoById(int idWebsiteInfo) {
        Result<Record2<String, String>> result = context
            .select(githubInfo.USER_NAME, githubInfo.REPOSITORY_NAME)
            .from(githubInfo)
            .where(githubInfo.WEBSITE_INFO_ID.eq(idWebsiteInfo))
            .fetch();
        return new GitHubLinkInfo(
            result.get(0).get(githubInfo.USER_NAME),
            result.get(0).get(githubInfo.REPOSITORY_NAME)
        );
    }
}
