package parserservice.dto;

import java.util.Objects;

public record GitHubInfo(String userName, String repositoryName) implements WebsiteInfo{

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitHubInfo that = (GitHubInfo) o;
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
    public String getDescriptionOfParsedWebsite() {
        return "GitHub repository with repository name " + repositoryName
                + " of user with username " + userName;
    }
}
