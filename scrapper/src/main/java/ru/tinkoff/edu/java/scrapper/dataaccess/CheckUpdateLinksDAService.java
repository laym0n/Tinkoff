package ru.tinkoff.edu.java.scrapper.dataaccess;

import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.List;

public interface CheckUpdateLinksDAService {
    List<WebsiteInfo> getListWebsiteInfoByLastCheckTime(int count);
    WebsiteInfo getWebsiteInfoByLastCheckTime();
}
