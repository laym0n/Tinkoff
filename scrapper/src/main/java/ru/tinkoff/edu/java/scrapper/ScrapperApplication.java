package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.*;

import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({ApplicationConfig.class, ParserLinksConfiguration.class, CheckerUpdateConfiguration.class,
        JDBCAccessConfiguration.class, JPAAccessConfiguration.class})
public class ScrapperApplication {
    private static Logger log = Logger.getLogger(ScrapperApplication.class.getName());
    public static void main(String[] args) {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        log.info(config::toString);
    }
}
