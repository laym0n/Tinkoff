import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import parserservice.chainresponsibilityparser.parserstrategies.StackOverflowParserStrategy;
import parserservice.dto.StackOverflowLinkInfo;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParserLinksStackOverflowLinksTest {
    public ParserLinksStackOverflowLinksTest() {
    }

    @ParameterizedTest
    @ArgumentsSource(ValidLinksArgumentsProvider.class)
    void parseValidLinksTest(String path, StackOverflowLinkInfo expectedResult){
        StackOverflowParserStrategy stackOverflowParserStrategy = new StackOverflowParserStrategy();

        boolean canParseResult = stackOverflowParserStrategy.canParse(path);
        StackOverflowLinkInfo stackOverflowInfoResult = stackOverflowParserStrategy.parse(path);

        assertTrue(canParseResult, ()-> path + " can be parsed by StackOverflowParserStrategy but result of canParse is false");
        assertEquals(expectedResult, stackOverflowInfoResult, ()-> "result of parsing " + path + " don't equal to expected result");
    }
    @ParameterizedTest
    @ArgumentsSource(InvalidLinksArgumentsProvider.class)
    void parseInvalidLinksTest(String path){
        StackOverflowParserStrategy stackOverflowParserStrategy = new StackOverflowParserStrategy();

        boolean canParseResult = stackOverflowParserStrategy.canParse(path);

        assertFalse(canParseResult, ()-> path + " can not be parsed by StackOverflowParserStrategy but result of canParse is true");
    }
    static class ValidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
                            new StackOverflowLinkInfo(1642028)),
                    Arguments.of( "https://stackoverflow.com/questions/59427642/how-to-update-sqlite/59429952#59429952",
                            new StackOverflowLinkInfo(59427642))
            );
        }
    }
    static class InvalidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://stackoverflow.com/search?q=unsupported%20link"),
                    Arguments.of("htts://github.com/spring-projects/spring-framework"),
                    Arguments.of("https://stackoeverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    Arguments.of("https://stacoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    Arguments.of("htts:/stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    Arguments.of("")

            );
        }
    }
}
