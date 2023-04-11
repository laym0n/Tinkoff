package ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsitefactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.webclients.WebsiteInfoWebClient;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfoFactory;
import ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsiteimpl.WebsiteInfoWebClientChainImpl;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

@Component
public class CheckerOfWebsiteInfoFactoryImpl implements CheckerOfWebsiteInfoFactory {
    private GitHubClient gitHubClient;
    private StackOverflowClient stackOverflowClient;
    @Autowired
    public CheckerOfWebsiteInfoFactoryImpl(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient) {
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public WebsiteInfoWebClient getCheckerOfWebsiteInfo() {
        WebsiteInfoWebClient firstWebsiteInfoWebClient =
                new WebsiteInfoWebClientChainImpl(gitHubClient, null);
        WebsiteInfoWebClient secondWebsiteInfoWebClient =
                new WebsiteInfoWebClientChainImpl(stackOverflowClient, firstWebsiteInfoWebClient);
        return secondWebsiteInfoWebClient;
    }
}
