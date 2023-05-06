package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;

public interface StackOverflowClient {
    StackOverflowResponse getStackOverflowResponse(StackOverflowLinkInfo stackOverflowInfo);
}
