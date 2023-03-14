package ParserService.ChainResponsibilityParser;

import ParserService.ChainResponsibilityParser.ParserStrategies.ParserStrategy;
import ParserService.DAL.StackOverflowRepository;
import ParserService.DTO.StackOverflowInfo;
import ParserService.Parser;

public class StackOverflowParser extends AbstractParser<StackOverflowInfo> {
    private StackOverflowRepository stackOverflowRepository;

    public StackOverflowParser(ParserStrategy<StackOverflowInfo> parserStrategy, Parser nextParser, StackOverflowRepository stackOverflowRepository) {
        super(parserStrategy, nextParser);
        this.stackOverflowRepository = stackOverflowRepository;
    }

    @Override
    void saveResult(StackOverflowInfo stackOverflowInfo) {
        stackOverflowRepository.save(stackOverflowInfo);
    }
}
