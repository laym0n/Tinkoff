package ParserService.ChainResponsibilityParser;

import ParserService.ChainResponsibilityParser.ParserStrategies.ParserStrategy;
import ParserService.DAL.GitHubInfoRepository;
import ParserService.DTO.GitHubInfo;
import ParserService.Parser;

public class GitHubParser extends AbstractParser<GitHubInfo> {
    private GitHubInfoRepository gitHubInfoRepository;

    public GitHubParser(ParserStrategy<GitHubInfo> parserStrategy, Parser nextParser, GitHubInfoRepository gitHubInfoRepository) {
        super(parserStrategy, nextParser);
        this.gitHubInfoRepository = gitHubInfoRepository;
    }

    @Override
    void saveResult(GitHubInfo info) {
        gitHubInfoRepository.save(info);
    }
}
