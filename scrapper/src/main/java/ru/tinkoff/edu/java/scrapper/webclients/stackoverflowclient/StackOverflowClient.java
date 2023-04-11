package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackoverflowInfo;
import ru.tinkoff.edu.java.scrapper.webclients.WebsiteInfoWebClient;

public interface StackOverflowClient extends WebsiteInfoWebClient {
    StackoverflowInfo getStackoverflowInfo(StackOverflowLinkInfo stackOverflowInfo);
}
