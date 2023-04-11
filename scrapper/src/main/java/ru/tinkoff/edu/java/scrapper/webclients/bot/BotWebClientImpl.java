package ru.tinkoff.edu.java.scrapper.webclients.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.github.GitHubInfoResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BotWebClientImpl implements BotWebClient {
    private  WebClient webClient;
    public BotWebClientImpl(){
        this("https://api.github.com");
    }
    @Autowired
    public BotWebClientImpl(@Value("#{botInfo.path}") String baseURL){
        webClient = WebClient.create(baseURL);
    }

    @Override
    public void sendLinkUpdateRequest(LinkUpdateRequest request) {
        webClient.post()
                .uri("/updates")
                .body(Mono.just(request), LinkUpdateRequest.class)
                .exchangeToMono(clientResponse -> Mono.empty());
    }

}
