package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClientImpl;

@ConfigurationProperties
public class ClientConfiguration {
    @Bean
    GitHubClient gitHubClient(){
        return new GitHubClientImpl();
    }
    @Bean
    StackOverflowClient stackOverflowClient(){
        return new StackOverflowClientImpl();
    }
}
