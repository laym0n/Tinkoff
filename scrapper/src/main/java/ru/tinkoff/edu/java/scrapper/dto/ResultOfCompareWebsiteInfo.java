package ru.tinkoff.edu.java.scrapper.dto;

import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public class ResultOfCompareWebsiteInfo<T extends WebsiteInfo> {
    public boolean isDifferent;
    public T uniqueSavedData;
    public T uniqueLoadedData;
}
