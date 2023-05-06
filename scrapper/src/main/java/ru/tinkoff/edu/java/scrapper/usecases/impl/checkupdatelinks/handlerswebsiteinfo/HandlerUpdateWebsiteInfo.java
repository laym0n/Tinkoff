package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

public interface HandlerUpdateWebsiteInfo {
    Optional<LinkUpdateRequest> updateWebsiteInfo(WebsiteInfo websiteInfo);
}
