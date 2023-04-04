package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsite;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsiteFactory;
import ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsitefactory.CheckerUpdateOfWebsiteFactoryImpl;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

@ConfigurationProperties
public class CheckerUpdateConfiguration {
    @Bean
    public CheckerUpdateOfWebsiteFactory checkerUpdateOfWebsiteFactory(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient){
        return new CheckerUpdateOfWebsiteFactoryImpl(gitHubClient, stackOverflowClient);

    }
    @Bean
    public CheckerUpdateOfWebsite checkerUpdateOfWebsite(CheckerUpdateOfWebsiteFactory checkerUpdateOfWebsiteFactory){
        return checkerUpdateOfWebsiteFactory.getCheckerUpdateOfWebsite();
    }
}
