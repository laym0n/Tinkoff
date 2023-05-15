package parserservice.chainresponsibilityparser.parserstrategies;

import parserservice.dto.StackOverflowLinkInfo;

public class StackOverflowParserStrategy extends ParserStrategy<StackOverflowLinkInfo> {
    private final static int NUMBER_BLOCK_WITH_BLOCK_ID = 4;

    public StackOverflowParserStrategy() {
        super("stackoverflow.com");
    }

    @Override
    public boolean canParse(String path) {
        boolean result = super.canParse(path);
        if (!result) {
            return result;
        }
        try {
            Integer.parseInt(path.split("/")[NUMBER_BLOCK_WITH_BLOCK_ID]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public StackOverflowLinkInfo parse(String path) {
        String[] separatedPath = path.split("/");
        return new StackOverflowLinkInfo(Integer.parseInt(separatedPath[NUMBER_BLOCK_WITH_BLOCK_ID]));
    }
}
