package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies;

import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public interface BuilderLinkUpdateRequestStrategy<C extends ResultOfCompareWebsiteInfo> {
    LinkUpdateRequest buildLinkUpdateRequest(C changes, int[] chatIds);
}
