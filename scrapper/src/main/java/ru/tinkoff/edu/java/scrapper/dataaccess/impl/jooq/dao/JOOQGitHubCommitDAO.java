package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.GithubCommitRecord;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQGitHubCommitDAO extends JOOQDAO {
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.GithubCommit githubCommit =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.GithubCommit.GITHUB_COMMIT;

    public void addAll(Collection<GitHubCommit> newCommits, int idWebsiteInfo) {
        context.batchInsert(
            getRecordsCommitFromBLLCommits(newCommits, idWebsiteInfo)
        ).execute();
    }

    public void removeAll(Collection<GitHubCommit> commitsForRemove, int idWebsiteInfo) {
        context.batchDelete(
            getRecordsCommitFromBLLCommits(commitsForRemove, idWebsiteInfo)
        ).execute();
    }

    public Collection<GitHubCommit> findAll(int idGitHubInfo) {
        List<GithubCommitRecord> loadedRecords = context
            .selectFrom(githubCommit)
            .where(githubCommit.WEBSITE_INFO_ID.eq(idGitHubInfo))
            .fetchInto(GithubCommitRecord.class);
        return getBLLCommitsFromRecordsCommit(loadedRecords);
    }

    private List<GitHubCommit> getBLLCommitsFromRecordsCommit(
        List<GithubCommitRecord> commits
    ) {
        return commits.stream()
            .map(commitRecord -> new GitHubCommit(commitRecord.getSha()))
            .toList();
    }

    private List<GithubCommitRecord> getRecordsCommitFromBLLCommits(
        Collection<GitHubCommit> commits,
        int idWebsiteInfo
    ) {
        return commits.stream()
            .map(commit -> {
                GithubCommitRecord newRecord = context.newRecord(githubCommit);
                newRecord.setSha(commit.getSha());
                newRecord.setWebsiteInfoId(idWebsiteInfo);
                return newRecord;
            }).toList();
    }
}
