import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import ru.tinkoff.edu.java.bot.telegrambotimpl.Command;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParserCommands;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

public class ParserLinksCommandsTest {

    @ParameterizedTest
    @ArgumentsSource(ValidCommandsArgumentsProvider.class)
    public void testParseValidCommandsFromUser(String inputText, ParsedCommand expectedResultParsedCommand){
        ParserCommands parserCommands = new ParserCommands();

        ParsedCommand resultParsedCommand = parserCommands.getParsedCommand(inputText);

        assertEquals(expectedResultParsedCommand, resultParsedCommand,
                ()->"Result of parse valid command (" + inputText + ") is " + resultParsedCommand.toString() + " but expected " + expectedResultParsedCommand.toString());
    }
    @ParameterizedTest
    @ArgumentsSource(InvalidCommandsArgumentsProvider.class)
    public void testParseInvalidCommandsFromUser(String inputText, ParsedCommand expectedResultParsedCommand){
        ParserCommands parserCommands = new ParserCommands();

        ParsedCommand resultParsedCommand = parserCommands.getParsedCommand(inputText);

        assertEquals(expectedResultParsedCommand, resultParsedCommand,
                ()->"Result of parse invalid command (" + inputText + ") is " + resultParsedCommand.toString() + " but expected " + expectedResultParsedCommand.toString());
    }

    static class ValidCommandsArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("/start", new ParsedCommand(Command.START, "")),
                    Arguments.of("/help", new ParsedCommand(Command.HELP, "")),
                    Arguments.of("/list", new ParsedCommand(Command.LIST, "")),
                    Arguments.of("/track", new ParsedCommand(Command.TRACK, "")),
                    Arguments.of("/untrack", new ParsedCommand(Command.UNTRACK, "")),

                    Arguments.of("/start text", new ParsedCommand(Command.START, "text")),
                    Arguments.of("/help text", new ParsedCommand(Command.HELP, "text")),
                    Arguments.of("/list text", new ParsedCommand(Command.LIST, "text")),
                    Arguments.of("/track text", new ParsedCommand(Command.TRACK, "text")),
                    Arguments.of("/untrack text", new ParsedCommand(Command.UNTRACK, "text")),

                    Arguments.of("  /start    text     ", new ParsedCommand(Command.START, "text")),
                    Arguments.of("      /help    text    ", new ParsedCommand(Command.HELP, "text")),
                    Arguments.of("    /list     text   ", new ParsedCommand(Command.LIST, "text")),
                    Arguments.of("   /track       text    ", new ParsedCommand(Command.TRACK, "text")),
                    Arguments.of("   /untrack   text     ", new ParsedCommand(Command.UNTRACK, "text"))
            );
        }
    }
    static class InvalidCommandsArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("/start1", new ParsedCommand(Command.UKNOWN, "")),
                    Arguments.of("/help1", new ParsedCommand(Command.UKNOWN, "")),
                    Arguments.of("/list1", new ParsedCommand(Command.UKNOWN, "")),
                    Arguments.of("/track1", new ParsedCommand(Command.UKNOWN, "")),
                    Arguments.of("/untrack1", new ParsedCommand(Command.UKNOWN, "")),

                    Arguments.of("/start1 text", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("/help1 text", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("/list1 text", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("/track1 text", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("/untrack1 text", new ParsedCommand(Command.UKNOWN, "text")),

                    Arguments.of("  /start1    text     ", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("    /help1   text    ", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("    /list1     text   ", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("   /track1       text    ", new ParsedCommand(Command.UKNOWN, "text")),
                    Arguments.of("   /untrack1   text     ", new ParsedCommand(Command.UKNOWN, "text")),

                    Arguments.of("untrack", new ParsedCommand(Command.UKNOWN, "untrack")),
                    Arguments.of("   untrack1   text     ", new ParsedCommand(Command.UKNOWN, "untrack1   text"))
            );
        }
    }
}
