package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategy.BuilderLinkUpdateRequestStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategy.CompareInfoStrategy;
import ru.tinkoff.edu.java.scrapper.webclients.WebsiteInfoWebClient;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class HandlerUpdateWebsiteInfoImplChain<T extends WebsiteInfo> implements HandlerUpdateWebsiteInfo {
    private HandlerUpdateWebsiteInfo nextHandler;
    private WebsiteInfoWebClient websiteInfoWebClient;
    private UpdateWebsiteInfoDAService<T> daService;
    private CompareInfoStrategy<T> compareInfoStrategy;
    private BuilderLinkUpdateRequestStrategy<T> builderLinkUpdateRequestStrategy;
    @Override
    public Optional<List<LinkUpdateRequest>> updateWebsiteInfo(WebsiteInfo savedWebsiteInfo) {
        if(!compareInfoStrategy.canCompare(savedWebsiteInfo))
            return nextHandler.updateWebsiteInfo(savedWebsiteInfo);
        WebsiteInfo loadedWebsiteInfo = websiteInfoWebClient.getWebSiteInfoByLinkInfo(savedWebsiteInfo.getLinkInfo());
        ResultOfCompareWebsiteInfo<T> resultOfCompare = compareInfoStrategy
                .compare(savedWebsiteInfo, loadedWebsiteInfo);
        if(resultOfCompare.isDifferent){
            daService.applyChanges(resultOfCompare);
            LinkUpdateRequest linkUpdateRequest = builderLinkUpdateRequestStrategy
                    .buildLinkUpdateRequest(resultOfCompare);

        }
        return Optional.empty();
    }

}
