package ru.tinkoff.edu.java.bot.telegrambotimpl;

public enum Command {
    START("/start - зарегистрировать пользователя\n"),
    HELP("/help - вывести окно с командами\n"),
    TRACK("/track {ссылка} - начать отслеживание ссылки\n"),
    UNTRACK("/untrack {ссылка} - прекратить отслеживание ссылки\n"),
    LIST("/list - показать список отслеживаемых ссылок\n"),
    UKNOWN("");
    public final String description;

    Command(String description) {
        this.description = description;
    }

    public String getComandSyntax() {
        return description.substring(0, description.indexOf(" "));
    }

    public String getComandDescription() {
        return description.substring(description.indexOf("-") + 2);
    }
}
