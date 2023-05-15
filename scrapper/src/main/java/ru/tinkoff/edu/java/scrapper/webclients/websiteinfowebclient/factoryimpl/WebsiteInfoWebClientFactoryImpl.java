package ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.factoryimpl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClient;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClientFactory;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.impl.chainresponsobility.GitHubChainWebClient;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.impl.chainresponsobility.StackOverflowChainWebClient;

public class WebsiteInfoWebClientFactoryImpl implements WebsiteInfoWebClientFactory {
    private GitHubClient gitHubClient;
    private StackOverflowClient stackOverflowClient;

    @Autowired
    public WebsiteInfoWebClientFactoryImpl(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient) {
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public WebsiteInfoWebClient getWebsiteInfoWebClient() {
        WebsiteInfoWebClient gitHubChainWebClient =
                new GitHubChainWebClient(null, gitHubClient);
        WebsiteInfoWebClient stackOverflowChainWebClient =
                new StackOverflowChainWebClient(gitHubChainWebClient, stackOverflowClient);
        return stackOverflowChainWebClient;
    }
}
