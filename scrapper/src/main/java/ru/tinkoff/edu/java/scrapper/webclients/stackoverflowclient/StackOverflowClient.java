package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import parserservice.dto.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowInfoResponse;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsite;

public interface StackOverflowClient extends CheckerUpdateOfWebsite {
    StackOverflowInfoResponse getUpdateInfo(StackOverflowInfo stackOverflowInfo);
}
