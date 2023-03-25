package ru.tinkoff.edu.java.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.util.logging.Level;
import java.util.logging.Logger;


@SpringBootApplication()
@EnableScheduling
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {

    private static Logger log = Logger.getLogger(BotApplication.class.getName());
    public static void main(String[] args) {
        var ctx = SpringApplication.run(BotApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        log.log(Level.INFO, config::toString);
    }
}
