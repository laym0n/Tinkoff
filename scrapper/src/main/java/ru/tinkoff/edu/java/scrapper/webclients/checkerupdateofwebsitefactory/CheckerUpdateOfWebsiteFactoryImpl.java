package ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsitefactory;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsite;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsiteFactory;
import ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsiteimpl.CheckerUpdateOfWebsiteChainImpl;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

@Component
public class CheckerUpdateOfWebsiteFactoryImpl implements CheckerUpdateOfWebsiteFactory {
    private GitHubClient gitHubClient;
    private StackOverflowClient stackOverflowClient;
    @Autowired
    public CheckerUpdateOfWebsiteFactoryImpl(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient) {
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public CheckerUpdateOfWebsite getCheckerUpdateOfWebsite() {
        CheckerUpdateOfWebsite firstCheckerUpdateOfWebsite =
                new CheckerUpdateOfWebsiteChainImpl(gitHubClient, null);
        CheckerUpdateOfWebsite secondCheckerUpdateOfWebsite =
                new CheckerUpdateOfWebsiteChainImpl(stackOverflowClient, firstCheckerUpdateOfWebsite);
        return secondCheckerUpdateOfWebsite;
    }
}
