package ru.tinkoff.edu.java.bot.telegrambotimpl;

import org.glassfish.grizzly.utils.Pair;
import org.springframework.stereotype.Component;

@Component
public class ParserCommands {
    private static final String PREFIX_FOR_COMMAND = "/";

    public ParsedCommand getParsedCommand(String text) {
        String trimText = "";
        if (text != null) {
            trimText = text.trim();
        }
        ParsedCommand result = new ParsedCommand(Command.UKNOWN, trimText);

        if ("".equals(trimText)) {
            return result;
        }
        Pair<String, String> commandAndText = getDelimitedCommandFromText(trimText);
        if (isCommand(commandAndText.getFirst())) {
            String commandForParse = cutCommandFromFullText(commandAndText.getFirst());
            Command commandFromText = getCommandFromText(commandForParse);
            result.setText(commandAndText.getSecond());
            result.setCommand(commandFromText);

        }
        return result;
    }

    private String cutCommandFromFullText(String text) {
        return text.substring(1);
    }

    private Command getCommandFromText(String text) {
        String upperCaseText = text.toUpperCase().trim();
        Command command = Command.UKNOWN;
        try {
            command = Command.valueOf(upperCaseText);
        } catch (IllegalArgumentException e) {

        }
        return command;
    }

    private Pair<String, String> getDelimitedCommandFromText(String trimText) {
        Pair<String, String> commandText;
        if (trimText.contains(" ")) {
            int indexOfSpace = trimText.indexOf(" ");
            commandText = new Pair(trimText.substring(0, indexOfSpace), trimText.substring(indexOfSpace + 1).trim());
        } else {
            commandText = new Pair(trimText, "");
        }
        return commandText;
    }

    private boolean isCommand(String text) {
        return text.startsWith(PREFIX_FOR_COMMAND);
    }
}
