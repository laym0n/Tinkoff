package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.CheckUpdateLinksDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.CheckUpdateLinksUseCase;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.sendupdaterequeststrategy.SendLinkUpdateRequestStrategy;

@Component
public class CheckUpdateLinksUseCaseImpl implements CheckUpdateLinksUseCase {
    private static Logger log = Logger.getLogger(CheckUpdateLinksUseCaseImpl.class.getName());
    @Value("${checker-update.count-sites-for-check-per-iteration}")
    private int countSites;
    private CheckUpdateLinksDAService checkUpdateLinksDAService;
    private HandlerUpdateWebsiteInfo handlerUpdateWebsiteInfo;
    private SendLinkUpdateRequestStrategy sendLinkUpdateRequestStrategy;

    public CheckUpdateLinksUseCaseImpl(CheckUpdateLinksDAService checkUpdateLinksDAService,
            HandlerUpdateWebsiteInfo handlerUpdateWebsiteInfo,
            SendLinkUpdateRequestStrategy sendLinkUpdateRequestStrategy) {
        this.checkUpdateLinksDAService = checkUpdateLinksDAService;
        this.handlerUpdateWebsiteInfo = handlerUpdateWebsiteInfo;
        this.sendLinkUpdateRequestStrategy = sendLinkUpdateRequestStrategy;
    }

    @Override
    public void checkUpdateLinks() {
        List<WebsiteInfo> websiteInfos = checkUpdateLinksDAService.getListWebsiteInfoByLastCheckTime(countSites);
        for (WebsiteInfo savedWebsiteInfo: websiteInfos) {
            try {
                Optional<LinkUpdateRequest> optionalLinkUpdateRequest = handlerUpdateWebsiteInfo
                        .updateWebsiteInfo(savedWebsiteInfo);
                if (optionalLinkUpdateRequest.isPresent()) {
                    sendLinkUpdateRequestStrategy.sendLinkUpdateRequest(optionalLinkUpdateRequest.get());
                }
            } catch (Exception ex) {
                log.log(Level.INFO, ex.getMessage());
            }
        }
    }
}
