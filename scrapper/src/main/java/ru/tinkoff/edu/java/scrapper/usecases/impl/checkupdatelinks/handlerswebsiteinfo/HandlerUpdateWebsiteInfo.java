package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo;

import java.util.Optional;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface HandlerUpdateWebsiteInfo {
    Optional<LinkUpdateRequest> updateWebsiteInfo(WebsiteInfo websiteInfo);
}
