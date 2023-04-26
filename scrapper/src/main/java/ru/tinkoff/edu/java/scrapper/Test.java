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
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        jdbcTemplate.query("SELECT wi.id, wit.name " +
//                        "FROM website_info wi " +
//                        "JOIN website_info_type wit ON wi.type_id = wit.id " +
//                        "ORDER BY wi.last_update_date_time ASC, wi.id ASC " +
//                        "LIMIT ?;",
//                (rs, rowNum) -> chainWebsiteInfoDAO.loadWebsiteInfo(rs.getString("name"), rs.getInt("id")),
//                5);
    }
}
