package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.configuration.CheckerUpdateConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.JDBCAccessConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.ParserLinksConfiguration;

import java.util.logging.Logger;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ParserLinksConfiguration.class, CheckerUpdateConfiguration.class,
        JDBCAccessConfiguration.class})
public class ScrapperApplication {
    private static Logger log = Logger.getLogger(ScrapperApplication.class.getName());
    public static void main(String[] args) {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        log.info(config::toString);
    }
}
