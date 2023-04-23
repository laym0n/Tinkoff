package ru.tinkoff.edu.java.scrapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClientImpl;

import javax.sql.DataSource;

public class Test {
    public static void main(String[] args){
        GitHubClient client = new GitHubClientImpl();
        GitHubLinkInfo linkInfo = new GitHubLinkInfo("laym0n", "Tinkoff");
        var ans = client.getGitHubResponse(linkInfo);
        linkInfo = new GitHubLinkInfo("drownedtears", "forum");
    }
}
