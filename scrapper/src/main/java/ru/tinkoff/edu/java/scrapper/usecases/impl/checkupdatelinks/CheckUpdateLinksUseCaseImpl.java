package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.CheckUpdateLinksDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.CheckUpdateLinksUseCase;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.HandlerUpdateWebsiteInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component

public class CheckUpdateLinksUseCaseImpl implements CheckUpdateLinksUseCase {
    @Value("${checker-update.count-sites-for-check-per-iteration}")
    private int countSites;
    private CheckUpdateLinksDAService checkUpdateLinksDAService;
    private HandlerUpdateWebsiteInfo handlerUpdateWebsiteInfo;

    public CheckUpdateLinksUseCaseImpl(CheckUpdateLinksDAService checkUpdateLinksDAService, HandlerUpdateWebsiteInfo handlerUpdateWebsiteInfo) {
        this.checkUpdateLinksDAService = checkUpdateLinksDAService;
        this.handlerUpdateWebsiteInfo = handlerUpdateWebsiteInfo;
    }

    @Override
    public List<LinkUpdateRequest> checkUpdateLinks() {
        List<LinkUpdateRequest> result = new ArrayList<>();
        List<WebsiteInfo> websiteInfos = checkUpdateLinksDAService.getListWebsiteInfoByLastCheckTime(countSites);
        for (WebsiteInfo savedWebsiteInfo: websiteInfos) {
            Optional<LinkUpdateRequest> optionalLinkUpdateRequest = handlerUpdateWebsiteInfo
                    .updateWebsiteInfo(savedWebsiteInfo);
            if(optionalLinkUpdateRequest.isPresent()){
                result.add(optionalLinkUpdateRequest.get());
            }
        }
        return result;
    }
}
