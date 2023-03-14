package ParserService.DTO;

public class GitHubInfo {
    private int id;
    private String userName;
    private String repositoryName;

    public GitHubInfo() {
    }

    public GitHubInfo(String userName, String repositoryName) {
        this.userName = userName;
        this.repositoryName = repositoryName;
    }

    public GitHubInfo(int id, String userName, String repositoryName) {
        this.id = id;
        this.userName = userName;
        this.repositoryName = repositoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
