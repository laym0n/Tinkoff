import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import parserservice.ParserLinks;
import parserservice.dto.GitHubInfo;
import parserservice.dto.StackOverflowInfo;
import parserservice.dto.WebsiteInfo;
import parserservice.factories.factoryimpl.ParserLinksFactoryImpl;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

public class ChainParserLinksTest {
    @ParameterizedTest
    @ArgumentsSource(ValidLinksArgumentsProvider.class)
    public void parseValidLinksTest(String path, WebsiteInfo expectedResultWebsiteInfo){
        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();

        WebsiteInfo websiteInfoFromParser = parserLinks.parse(path);

        assertEquals(expectedResultWebsiteInfo, websiteInfoFromParser,
                ()-> "The result of parse path " + path + " is " + websiteInfoFromParser.toString() +
                        " but expected result is " + expectedResultWebsiteInfo.toString());
    }
    @ParameterizedTest
    @ArgumentsSource(InvalidLinksArgumentsProvider.class)
    public void parseInvalidLinksTest(String path){
        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();

        WebsiteInfo websiteInfoFromParser = parserLinks.parse(path);

        assertNull(websiteInfoFromParser,
                ()-> "The result of parse path " + path + " is " + websiteInfoFromParser.toString() +
                        " but expected result is null");
    }
    static class ValidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://github.com/sanyarnd/tinkoff-java-course-2022/",
                            new GitHubInfo("sanyarnd", "tinkoff-java-course-2022")),
                    Arguments.of("https://github.com/spring-projects/spring-framework",
                            new GitHubInfo("spring-projects", "spring-framework")),
                    Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
                            new StackOverflowInfo(1642028)),
                    Arguments.of( "https://stackoverflow.com/questions/59427642/how-to-update-sqlite/59429952#59429952",
                            new StackOverflowInfo(59427642))
            );
        }
    }
    static class InvalidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://github1.com/spring-projects/spring-framework"),
                    Arguments.of("htts://github.com/spring-projects/spring-framework"),
                    Arguments.of("")
            );
        }
    }
}
