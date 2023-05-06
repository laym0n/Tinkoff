package parserservice.chainresponsibilityparser.parserstrategies;

import parserservice.dto.StackOverflowLinkInfo;

public class StackOverflowParserStrategy extends ParserStrategy<StackOverflowLinkInfo> {
    public StackOverflowParserStrategy() {
        super("stackoverflow.com");
    }

    @Override
    public boolean canParse(String path) {
        boolean result = super.canParse(path);
        if(!result)
            return result;
        try{
            Integer.parseInt(path.split("/")[4]);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public StackOverflowLinkInfo parse(String path) {
        String[] separatedPath = path.split("/");
        return new StackOverflowLinkInfo(Integer.parseInt(separatedPath[4]));
    }
}
