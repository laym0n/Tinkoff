package ru.tinkoff.edu.java.bot;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.configuration.MicrometerConfiguration;
import ru.tinkoff.edu.java.bot.configuration.RabbitMQConfiguration;


@SpringBootApplication()
@EnableScheduling
@EnableConfigurationProperties({ApplicationConfig.class, RabbitMQConfiguration.class})
@Import(value = {MicrometerConfiguration.class})
public class BotApplication {
    private static Logger log = Logger.getLogger(BotApplication.class.getName());

    public static void main(String[] args) {
        var ctx = SpringApplication.run(BotApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        log.log(Level.INFO, config::toString);
    }
}
