package ru.tinkoff.edu.java.scrapper;

import parserservice.dto.GitHubInfo;
import parserservice.dto.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubInfoResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowInfoResponse;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClientImpl;

import java.util.logging.Logger;

public class Test {
    private static Logger log = Logger.getLogger(Test.class.getName());
    public static void main(String[] args){
        GitHubInfoResponse gitHubInfoResponse = new GitHubClientImpl().getUpdateInfo(new GitHubInfo("drownedtears", "forum"));
        log.info(gitHubInfoResponse::toString);

        StackOverflowInfoResponse stackOverflowInfoResponse = new StackOverflowClientImpl().getUpdateInfo(new StackOverflowInfo(285177));
        log.info(stackOverflowInfoResponse::toString);
    }
}
