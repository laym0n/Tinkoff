package parserservice.chainresponsibilityparser.parserstrategies;

import parserservice.dto.GitHubLinkInfo;

public class GitHubParserStrategy extends ParserStrategy<GitHubLinkInfo> {
    private static final int MIN_COUNT_OF_BLOCS_IN_LINK = 5;
    private static final int NUMBER_BLOCK_WITH_USER_NAME = 3;
    private static final int NUMBER_BLOCK_WITH_REPOSITORY_NAME = 4;

    public GitHubParserStrategy() {
        super("github.com");
    }

    @Override
    public boolean canParse(String path) {
        return super.canParse(path) && path.split("/").length >= MIN_COUNT_OF_BLOCS_IN_LINK;
    }

    @Override
    public GitHubLinkInfo parse(String path) {
        String[] separatedPath = path.split("/");
        return new GitHubLinkInfo(separatedPath[NUMBER_BLOCK_WITH_USER_NAME],
            separatedPath[NUMBER_BLOCK_WITH_REPOSITORY_NAME]);
    }
}
