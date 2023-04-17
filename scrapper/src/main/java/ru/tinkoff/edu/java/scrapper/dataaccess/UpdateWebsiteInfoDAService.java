package ru.tinkoff.edu.java.scrapper.dataaccess;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;

public interface UpdateWebsiteInfoDAService<R extends ResultOfCompareWebsiteInfo> {
    void applyChanges(R changes);
    int[] getAllChatIdWithTrackedLinkInfo(LinkInfo linkInfo);
    int[] getAllChatIdWithTrackedIdWebsiteInfo(int idWebsiteInfo);
}
