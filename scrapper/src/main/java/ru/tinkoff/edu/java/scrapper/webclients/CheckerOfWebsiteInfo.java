package ru.tinkoff.edu.java.scrapper.webclients;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface CheckerOfWebsiteInfo {
    WebsiteInfo getWebSiteInfoByLinkInfo(LinkInfo linkInfo);
}
