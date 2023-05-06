package parserservice.dto;

import java.util.Objects;

public record GitHubLinkInfo(String userName, String repositoryName) implements LinkInfo {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitHubLinkInfo that = (GitHubLinkInfo) o;
        return userName.equals(that.userName) && repositoryName.equals(that.repositoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, repositoryName);
    }

    @Override
    public String getPath() {
        return "https://github.com/" + userName + "/" + repositoryName;
    }

    @Override
    public String getDescriptionOfParsedLink() {
        return "GitHub repository with repository name " + repositoryName
                + " of user with username " + userName;
    }
}
