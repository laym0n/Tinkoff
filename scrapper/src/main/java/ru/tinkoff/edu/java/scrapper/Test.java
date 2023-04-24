package ru.tinkoff.edu.java.scrapper;

import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentsResponse;
import ru.tinkoff.edu.java.scrapper.webclients.bot.BotWebClientImpl;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClientImpl;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{OffsetDateTime.now(), Integer.valueOf(1), Integer.valueOf(76095413)});
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new DriverManagerDataSource(
                "jdbc:postgresql://localhost:5433/scrapper",
                "user",
                "pass"
        ));
        jdbcTemplate.batchUpdate("update stack_overflow_answer SET last_edit_date_time = ? where " +
                "website_info_id = ? and id = ?", batchArgs);
    }
}
