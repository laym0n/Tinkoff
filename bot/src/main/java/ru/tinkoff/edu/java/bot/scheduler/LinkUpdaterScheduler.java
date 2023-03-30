package ru.tinkoff.edu.java.bot.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class LinkUpdaterScheduler {
    private static Logger log = Logger.getLogger(LinkUpdaterScheduler.class.getName());
    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    void update(){
        log.info("Check update link");
    }
}
