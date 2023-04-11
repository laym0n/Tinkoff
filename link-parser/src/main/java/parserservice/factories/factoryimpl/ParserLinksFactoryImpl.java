package parserservice.factories.factoryimpl;

import parserservice.ParserLinks;
import parserservice.chainresponsibilityparser.ParserLinksImpl;
import parserservice.chainresponsibilityparser.parserstrategies.GitHubParserStrategy;
import parserservice.chainresponsibilityparser.parserstrategies.StackOverflowParserStrategy;
import parserservice.factories.ParserLinksFactory;

public class ParserLinksFactoryImpl implements ParserLinksFactory {
    @Override
    public ParserLinks getParserLinks() {
        ParserLinks githubParserLinks = new ParserLinksImpl(new GitHubParserStrategy());
        ParserLinks stackOverflowParserLinks = new ParserLinksImpl(new StackOverflowParserStrategy(), githubParserLinks);
        return stackOverflowParserLinks;
    }
}
