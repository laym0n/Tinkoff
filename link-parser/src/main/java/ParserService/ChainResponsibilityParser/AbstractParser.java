package ParserService.ChainResponsibilityParser;

import ParserService.ChainResponsibilityParser.ParserStrategies.ParserStrategy;
import ParserService.Parser;

public abstract class AbstractParser <T>  implements Parser {
    private ParserStrategy<T> parserStrategy;
    private Parser nextParser;

    public AbstractParser(ParserStrategy<T> parserStrategy, Parser nextParser) {
        this.parserStrategy = parserStrategy;
        this.nextParser = nextParser;
    }

    @Override
    public void parse(String path) {
        if(!parserStrategy.canParse(path)){
            if (nextParser != null)
                nextParser.parse(path);
            return;
        }
        T result = parserStrategy.parse(path);
        saveResult(result);
    }

    abstract void saveResult(T result);
    public void setNextParser(Parser nextParser) {
        this.nextParser = nextParser;
    }
}
