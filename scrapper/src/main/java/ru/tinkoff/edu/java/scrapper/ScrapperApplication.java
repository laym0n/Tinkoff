package ru.tinkoff.edu.java.scrapper;

import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.configuration.CheckerUpdateConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.JDBCAccessConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.JOOQAccessConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.JPAAccessConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.ParserLinksConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.RabbitMQConfiguration;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
    ApplicationConfig.class,
    ParserLinksConfiguration.class,
    CheckerUpdateConfiguration.class,
    JDBCAccessConfiguration.class,
    JPAAccessConfiguration.class,
    RabbitMQConfiguration.class,
    JOOQAccessConfiguration.class
})
public class ScrapperApplication {
    private static Logger log = Logger.getLogger(ScrapperApplication.class.getName());

    public static void main(String[] args) {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        log.info(config::toString);
    }
}
