package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategy;

import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public abstract class CompareInfoStrategy<T extends WebsiteInfo> {
    public abstract boolean canCompare(WebsiteInfo savedClass);
    public abstract ResultOfCompareWebsiteInfo<T> compare(WebsiteInfo savedInfo, WebsiteInfo loadedInfo);
}
