package ru.tinkoff.edu.java.scrapper.webclients.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

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
