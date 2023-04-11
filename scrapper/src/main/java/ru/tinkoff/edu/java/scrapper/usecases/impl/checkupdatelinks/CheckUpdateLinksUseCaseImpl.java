package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks;

import ru.tinkoff.edu.java.scrapper.dataaccess.CheckUpdateLinksDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.CheckUpdateLinksUseCase;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.HandlerUpdateWebsiteInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckUpdateLinksUseCaseImpl implements CheckUpdateLinksUseCase {
    private int countSites;
    private CheckUpdateLinksDAService checkUpdateLinksDAService;
    private HandlerUpdateWebsiteInfo handlerUpdateWebsiteInfo;
    @Override
    public List<LinkUpdateRequest> checkUpdateLinks() {
        List<LinkUpdateRequest> result = new ArrayList<>();
        List<WebsiteInfo> websiteInfos = checkUpdateLinksDAService.getListWebsiteInfoByLastCheckTime(countSites);
        for (WebsiteInfo savedWebsiteInfo: websiteInfos) {
            Optional<List<LinkUpdateRequest>> optionalLinkUpdateRequest = handlerUpdateWebsiteInfo
                    .updateWebsiteInfo(savedWebsiteInfo);
            if(optionalLinkUpdateRequest.isPresent()){
                result.addAll(optionalLinkUpdateRequest.get());
            }
        }
        return result;
    }
}
