package ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo;

import lombok.Getter;
import lombok.Setter;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import java.time.OffsetDateTime;
import java.util.Optional;

@Getter
@Setter
public final class ResultOfCompareGitHubInfo extends ResultOfCompareWebsiteInfo{
    private GitHubCommit[] droppedCommits;
    private GitHubCommitResponse[] pushedCommits;
    private GitHubBranch[] droppedBranches;
    private GitHubBranchResponse[] addedBranches;
    private Optional<OffsetDateTime> lastActivityDate;
    private GitHubLinkInfo linkInfo = null;

    public ResultOfCompareGitHubInfo(boolean isDifferent, GitHubLinkInfo linkInfo, int idWebsiteInfo, GitHubCommit[] droppedCommits, GitHubCommitResponse[] pushedCommits, GitHubBranch[] droppedBranches, GitHubBranchResponse[] addedBranches, Optional<OffsetDateTime> lastActivityDate) {
        super(isDifferent, idWebsiteInfo);
        this.droppedCommits = droppedCommits;
        this.pushedCommits = pushedCommits;
        this.droppedBranches = droppedBranches;
        this.addedBranches = addedBranches;
        this.lastActivityDate = lastActivityDate;
        this.linkInfo = linkInfo;
    }

    public ResultOfCompareGitHubInfo(int idWebsiteInfo, GitHubLinkInfo linkInfo) {
        super(idWebsiteInfo);
        this.linkInfo = linkInfo;
    }
}
