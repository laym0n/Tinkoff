import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import parserservice.chainresponsibilityparser.parserstrategies.GitHubParserStrategy;
import parserservice.dto.GitHubLinkInfo;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParserLinksGitHubLinksTest {
    public ParserLinksGitHubLinksTest() {
    }

    @ParameterizedTest
    @ArgumentsSource(ValidLinksArgumentsProvider.class)
    void parseValidLinksTest(String path, GitHubLinkInfo expectedResult){
        GitHubParserStrategy gitHubParserStrategy = new GitHubParserStrategy();

        boolean canParseResult = gitHubParserStrategy.canParse(path);
        GitHubLinkInfo gitHubInfoResult = gitHubParserStrategy.parse(path);

        assertTrue(canParseResult, ()-> path + " can be parsed by GitHubParserStrategy but result of canParse is false");
        assertEquals(expectedResult, gitHubInfoResult, ()-> "result of parsing " + path + " don't equal to expected result");
    }
    @ParameterizedTest
    @ArgumentsSource(InvalidLinksArgumentsProvider.class)
    void parseInvalidLinksTest(String path){
        GitHubParserStrategy gitHubParserStrategy = new GitHubParserStrategy();

        boolean canParseResult = gitHubParserStrategy.canParse(path);

        assertFalse(canParseResult, ()-> path + " can not be parsed by GitHubParserStrategy but result of canParse is true");
    }
    static class ValidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://github.com/sanyarnd/tinkoff-java-course-2022/",
                            new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022")),
                    Arguments.of("https://github.com/spring-projects/spring-framework",
                            new GitHubLinkInfo("spring-projects", "spring-framework"))
            );
        }
    }
    static class InvalidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://github1.com/spring-projects/spring-framework"),
                    Arguments.of("htts://github.com/spring-projects/spring-framework"),
                    Arguments.of(""),
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
