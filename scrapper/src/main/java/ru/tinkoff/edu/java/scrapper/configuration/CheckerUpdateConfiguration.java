package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.webclients.WebsiteInfoWebClient;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfoFactory;
import ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsitefactory.CheckerOfWebsiteInfoFactoryImpl;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

@ConfigurationProperties
public class CheckerUpdateConfiguration {
    @Bean
    public CheckerOfWebsiteInfoFactory checkerUpdateOfWebsiteFactory(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient){
        return new CheckerOfWebsiteInfoFactoryImpl(gitHubClient, stackOverflowClient);

    }
    @Bean
    public WebsiteInfoWebClient checkerUpdateOfWebsite(CheckerOfWebsiteInfoFactory checkerOfWebsiteInfoFactory){
        return checkerOfWebsiteInfoFactory.getCheckerOfWebsiteInfo();
    }
}
