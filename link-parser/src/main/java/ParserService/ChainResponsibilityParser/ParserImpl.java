package ParserService.ChainResponsibilityParser;

import ParserService.ChainResponsibilityParser.ParserStrategies.ParserStrategy;
import ParserService.WebsiteInfoRepository;
import ParserService.Parser;

public class ParserImpl<T>  implements Parser {
    private ParserStrategy<T> parserStrategy;
    private Parser nextParser;
    private WebsiteInfoRepository<T> websiteInfoRepository;

    public ParserImpl(ParserStrategy<T> parserStrategy, Parser nextParser, WebsiteInfoRepository<T> websiteInfoRepository) {
        this.parserStrategy = parserStrategy;
        this.nextParser = nextParser;
        this.websiteInfoRepository = websiteInfoRepository;
    }

    @Override
    public void parse(String path) {
        if(!parserStrategy.canParse(path)){
            if (nextParser != null)
                nextParser.parse(path);
            return;
        }
        T result = parserStrategy.parse(path);
        websiteInfoRepository.save(result);
    }
    public void setNextParser(Parser nextParser) {
        this.nextParser = nextParser;
    }
}
