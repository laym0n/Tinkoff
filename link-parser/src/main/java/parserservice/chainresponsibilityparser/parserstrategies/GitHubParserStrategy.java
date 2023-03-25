package parserservice.chainresponsibilityparser.parserstrategies;

import parserservice.dto.GitHubInfo;

public class GitHubParserStrategy extends ParserStrategy<GitHubInfo> {
    public GitHubParserStrategy() {
        super("github.com");
    }

    @Override
    public boolean canParse(String path) {
        return super.canParse(path) && path.split("/").length >= 5;
    }

    @Override
    public GitHubInfo parse(String path) {
        String[] separatedPath = path.split("/");
        return new GitHubInfo(separatedPath[3], separatedPath[4]);
    }
}
