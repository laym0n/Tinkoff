package ru.tinkoff.edu.java.scrapper.dataaccess;

import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;

public interface UpdateWebsiteInfoDAService<R extends ResultOfCompareWebsiteInfo> {
    void applyChanges(R changes);
    int[] getAllChatIdWithTrackedIdWebsiteInfo(int idWebsiteInfo);
}
