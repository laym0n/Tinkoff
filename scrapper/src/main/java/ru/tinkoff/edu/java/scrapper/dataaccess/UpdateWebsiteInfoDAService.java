package ru.tinkoff.edu.java.scrapper.dataaccess;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.WebsiteResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface UpdateWebsiteInfoDAService<T extends WebsiteInfo, R extends WebsiteResponse> {
    void applyChanges(ResultOfCompareWebsiteInfo<T, R> changes);
    int[] getAllChatIdWithTrackedLinkInfo(LinkInfo linkInfo);
    int[] getAllChatIdWithTrackedIdWebsiteInfo(int idWebsiteInfo);
}
