package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackoverflowInfo;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfo;

public interface StackOverflowClient extends CheckerOfWebsiteInfo {
    StackoverflowInfo getStackoverflowInfo(StackOverflowLinkInfo stackOverflowInfo);
}
