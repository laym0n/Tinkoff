package parserservice.chainresponsibilityparser;

import parserservice.chainresponsibilityparser.parserstrategies.ParserStrategy;
import parserservice.Parser;
import parserservice.dto.WebsiteInfo;

public class ParserImpl  implements Parser {
    private ParserStrategy parserStrategy;
    private Parser nextParser;

    public ParserImpl(ParserStrategy parserStrategy, Parser nextParser) {
        this.parserStrategy = parserStrategy;
        this.nextParser = nextParser;
    }

    @Override
    public WebsiteInfo parse(String path) {
        if(!parserStrategy.canParse(path)){
            return (nextParser != null? nextParser.parse(path) : null);
        }
        WebsiteInfo result = parserStrategy.parse(path);
        return result;
    }
    public void setNextParser(Parser nextParser) {
        this.nextParser = nextParser;
    }
}
