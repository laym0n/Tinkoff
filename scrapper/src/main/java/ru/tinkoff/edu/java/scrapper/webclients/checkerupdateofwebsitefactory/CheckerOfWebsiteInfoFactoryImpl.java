package ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsitefactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfoFactory;
import ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsiteimpl.CheckerOfWebsiteInfoChainImpl;
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
    public CheckerOfWebsiteInfo getCheckerOfWebsiteInfo() {
        CheckerOfWebsiteInfo firstCheckerOfWebsiteInfo =
                new CheckerOfWebsiteInfoChainImpl(gitHubClient, null);
        CheckerOfWebsiteInfo secondCheckerOfWebsiteInfo =
                new CheckerOfWebsiteInfoChainImpl(stackOverflowClient, firstCheckerOfWebsiteInfo);
        return secondCheckerOfWebsiteInfo;
    }
}
