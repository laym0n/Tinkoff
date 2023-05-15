package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.GithubBranchRecord;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQGitHubBranchesDAO extends JOOQDAO {
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.GithubBranch githubBranch =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.GithubBranch.GITHUB_BRANCH;

    public void addAll(Collection<GitHubBranch> newBranches, int idWebsiteInfo) {
        context.batchInsert(
            getRecordsBranchFromBLLBranches(newBranches, idWebsiteInfo)
        ).execute();
    }

    public void removeAll(Collection<GitHubBranch> branchesForRemove, int idWebsiteInfo) {
        context.batchDelete(
            getRecordsBranchFromBLLBranches(branchesForRemove, idWebsiteInfo)
        ).execute();
    }

    public Collection<GitHubBranch> findAll(int idGitHubInfo) {
        List<GithubBranchRecord> loadedRecords = context
            .selectFrom(githubBranch)
            .where(githubBranch.WEBSITE_INFO_ID.eq(idGitHubInfo))
            .fetchInto(GithubBranchRecord.class);
        return getBLLBranchesFromRecordsBranch(loadedRecords);
    }

    private List<GitHubBranch> getBLLBranchesFromRecordsBranch(
        List<GithubBranchRecord> branches
    ) {
        return branches.stream()
            .map(branchRecord -> new GitHubBranch(branchRecord.getName()))
            .toList();
    }

    private List<GithubBranchRecord> getRecordsBranchFromBLLBranches(
        Collection<GitHubBranch> branches,
        int idWebsiteInfo
    ) {
        return branches.stream()
            .map(branch -> {
                GithubBranchRecord branchRecord = context.newRecord(githubBranch);
                branchRecord.setName(branch.getBranchName());
                branchRecord.setWebsiteInfoId(idWebsiteInfo);
                return branchRecord;
            }).toList();
    }
}
