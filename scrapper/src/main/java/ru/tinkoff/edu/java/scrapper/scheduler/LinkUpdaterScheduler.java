package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;
import ru.tinkoff.edu.java.scrapper.usecases.CheckUpdateLinksUseCase;

@Component
@AllArgsConstructor
public class LinkUpdaterScheduler {
    private static Logger log = Logger.getLogger(LinkUpdaterScheduler.class.getName());
    private CheckUpdateLinksUseCase checkUpdateLinksUseCase;
    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    void update(){
        log.info("Check update link");
        checkUpdateLinksUseCase.checkUpdateLinks();
    }
}
