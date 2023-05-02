package ru.tinkoff.edu.java.bot.telegrambotimpl;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ParsedCommand {
    long chatId;
    Command command = Command.UKNOWN;
    String text = "";

    public ParsedCommand(Command command, String text) {
        this.command = command;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParsedCommand that = (ParsedCommand) o;
        return getChatId() == that.getChatId()
            && getCommand() == that.getCommand()
            && getText().equals(that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId(), getCommand(), getText());
    }
}
