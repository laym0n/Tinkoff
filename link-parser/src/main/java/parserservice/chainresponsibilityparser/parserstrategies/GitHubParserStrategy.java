package parserservice.chainresponsibilityparser.parserstrategies;

import parserservice.dto.GitHubLinkInfo;

public class GitHubParserStrategy extends ParserStrategy<GitHubLinkInfo> {
    public GitHubParserStrategy() {
        super("github.com");
    }

    @Override
    public boolean canParse(String path) {
        return super.canParse(path) && path.split("/").length >= 5;
    }

    @Override
    public GitHubLinkInfo parse(String path) {
        String[] separatedPath = path.split("/");
        return new GitHubLinkInfo(separatedPath[3], separatedPath[4]);
    }
}
