package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.factoryimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.HandlerUpdateWebsiteInfoFactory;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.HandlerUpdateGitHubInfoChain;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.HandlerUpdateStackOverflowInfoChain;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.github.CompareGitHubInfoStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.github.GitHubBuilderLinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.stackoverflow.CompareStackOverflowInfoStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.stackoverflow.StackOverflowBuilderLinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

@Component
@AllArgsConstructor
public class HandlerUpdateWebsiteInfoFactoryImpl implements HandlerUpdateWebsiteInfoFactory {
    private CompareGitHubInfoStrategy compareGitHubInfoStrategy;
    private GitHubBuilderLinkUpdateRequest gitHubBuilderLinkUpdateRequest;
    private UpdateWebsiteInfoDAService<ResultOfCompareGitHubInfo> gitHubDAService;
    private GitHubClient gitHubClient;
    private CompareStackOverflowInfoStrategy compareStackOverflowInfoStrategy;
    private StackOverflowBuilderLinkUpdateRequest stackOverflowBuilderLinkUpdateRequest;
    private UpdateWebsiteInfoDAService<ResultOfCompareStackOverflowInfo> stackOverflowDAService;
    private StackOverflowClient stackOverflowClient;
    @Override
    public HandlerUpdateWebsiteInfo getHandlerUpdateWebsiteInfo() {
        HandlerUpdateGitHubInfoChain gitHubInfoHandler = new HandlerUpdateGitHubInfoChain(gitHubDAService,
                compareGitHubInfoStrategy, gitHubBuilderLinkUpdateRequest, gitHubClient);
        return new HandlerUpdateStackOverflowInfoChain(
                gitHubInfoHandler, stackOverflowDAService, compareStackOverflowInfoStrategy,
                stackOverflowBuilderLinkUpdateRequest, stackOverflowClient
        );
    }
}
