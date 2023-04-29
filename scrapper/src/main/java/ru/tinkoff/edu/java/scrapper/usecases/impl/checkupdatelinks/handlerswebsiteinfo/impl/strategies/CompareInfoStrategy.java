package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies;

import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.WebsiteResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface CompareInfoStrategy<W extends WebsiteInfo, R extends WebsiteResponse, C extends ResultOfCompareWebsiteInfo> {
    C compare(W savedInfo, R loadedResponse);
}
