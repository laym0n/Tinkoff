package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies;

import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.WebsiteResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface CompareInfoStrategy<W extends WebsiteInfo, R extends WebsiteResponse> {
    ResultOfCompareWebsiteInfo<W, R> compare(W savedInfo, R loadedResponse);
}
