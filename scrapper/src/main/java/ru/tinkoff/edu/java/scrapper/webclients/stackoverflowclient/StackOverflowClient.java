package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import parserservice.dto.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowInfoResponse;

public interface StackOverflowClient {
    StackOverflowInfoResponse getUpdateInfo(StackOverflowInfo stackOverflowInfo);
}
