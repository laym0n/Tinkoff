package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.List;
import java.util.Optional;

public interface HandlerUpdateWebsiteInfo {
    Optional<List<LinkUpdateRequest>> updateWebsiteInfo(WebsiteInfo websiteInfo);
}
