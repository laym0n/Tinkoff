package ru.tinkoff.edu.java.scrapper.dataaccess;

import java.util.List;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface CheckUpdateLinksDAService {
    List<WebsiteInfo> getListWebsiteInfoByLastCheckTime(int count);
}
