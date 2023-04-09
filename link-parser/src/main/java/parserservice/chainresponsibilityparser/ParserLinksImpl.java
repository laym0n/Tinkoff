package parserservice.chainresponsibilityparser;

import parserservice.chainresponsibilityparser.parserstrategies.ParserStrategy;
import parserservice.ParserLinks;
import parserservice.dto.LinkInfo;

public class ParserLinksImpl implements ParserLinks {
    private ParserStrategy parserStrategy;
    private ParserLinks nextParserLinks;

    public ParserLinksImpl(ParserStrategy parserStrategy, ParserLinks nextParserLinks) {
        this.parserStrategy = parserStrategy;
        this.nextParserLinks = nextParserLinks;
    }

    public ParserLinksImpl(ParserStrategy parserStrategy) {
        this(parserStrategy, null);
    }

    @Override
    public LinkInfo parse(String path) {
        if(!parserStrategy.canParse(path)){
            return (nextParserLinks != null? nextParserLinks.parse(path) : null);
        }
        LinkInfo result = parserStrategy.parse(path);
        return result;
    }
    public void setNextParser(ParserLinks nextParserLinks) {
        this.nextParserLinks = nextParserLinks;
    }
}
