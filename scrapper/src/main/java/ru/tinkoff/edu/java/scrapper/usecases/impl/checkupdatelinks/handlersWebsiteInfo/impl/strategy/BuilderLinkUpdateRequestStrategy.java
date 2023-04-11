package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategy;

import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public abstract class BuilderLinkUpdateRequestStrategy<T extends WebsiteInfo> {
    public abstract LinkUpdateRequest buildLinkUpdateRequest(ResultOfCompareWebsiteInfo<T> changes);
}
