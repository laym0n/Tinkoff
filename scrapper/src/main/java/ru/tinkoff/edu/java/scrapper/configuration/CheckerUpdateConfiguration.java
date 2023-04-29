package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.HandlerUpdateWebsiteInfoFactory;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClient;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClientFactory;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.factoryimpl.WebsiteInfoWebClientFactoryImpl;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

@ConfigurationProperties
public class CheckerUpdateConfiguration {
    @Bean
    public WebsiteInfoWebClientFactory checkerUpdateOfWebsiteFactory(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient){
        return new WebsiteInfoWebClientFactoryImpl(gitHubClient, stackOverflowClient);
    }
    @Bean
    public WebsiteInfoWebClient checkerUpdateOfWebsite(WebsiteInfoWebClientFactory websiteInfoWebClientFactory){
        return websiteInfoWebClientFactory.getWebsiteInfoWebClient();
    }
    @Bean
    public HandlerUpdateWebsiteInfo handlerUpdateWebsiteInfo(HandlerUpdateWebsiteInfoFactory factory){
        return factory.getHandlerUpdateWebsiteInfo();
    }
}
