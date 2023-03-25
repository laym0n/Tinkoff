package parserservice.chainresponsibilityparser.parserstrategies;

import parserservice.dto.StackOverflowInfo;

public class StackOverflowParserStrategy extends ParserStrategy<StackOverflowInfo> {
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
    public StackOverflowInfo parse(String path) {
        String[] separatedPath = path.split("/");
        return new StackOverflowInfo(Integer.parseInt(separatedPath[4]));
    }
}
