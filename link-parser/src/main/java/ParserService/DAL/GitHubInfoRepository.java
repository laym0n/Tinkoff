package ParserService.DAL;
import ParserService.DTO.GitHubInfo;

public interface GitHubInfoRepository {
    public void save(GitHubInfo gitHubInfo);
}
