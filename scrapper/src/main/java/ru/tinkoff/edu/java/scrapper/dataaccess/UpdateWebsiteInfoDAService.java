package ru.tinkoff.edu.java.scrapper.dataaccess;

import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface UpdateWebsiteInfoDAService<T extends WebsiteInfo> {
    void applyChanges(ResultOfCompareWebsiteInfo<T> changes);
}
