package ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface WebsiteInfoWebClient {
    WebsiteInfo getWebSiteInfoByLinkInfo(LinkInfo linkInfo);
}
