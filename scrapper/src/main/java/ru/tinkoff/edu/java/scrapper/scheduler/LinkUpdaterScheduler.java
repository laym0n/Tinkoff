package ru.tinkoff.edu.java.scrapper.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.usecases.CheckUpdateLinksUseCase;
import ru.tinkoff.edu.java.scrapper.webclients.bot.BotWebClient;

import java.util.List;
import java.util.logging.Logger;

@Component
public class LinkUpdaterScheduler {
    private static Logger log = Logger.getLogger(LinkUpdaterScheduler.class.getName());
    private CheckUpdateLinksUseCase checkUpdateLinksUseCase;
    private BotWebClient botWebClient;
    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    void update(){
        log.info("Check update link");
        List<LinkUpdateRequest> requests = checkUpdateLinksUseCase.checkUpdateLinks();
        for (LinkUpdateRequest request : requests){
            botWebClient.sendLinkUpdateRequest(request);
        }
    }
}
