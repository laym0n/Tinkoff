package ru.tinkoff.edu.java.bot.webclients;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;

import java.security.InvalidParameterException;
import java.util.Arrays;

@Component
public class ScrapperClientImpl implements ScrapperClient {
    private String baseURL;
    public ScrapperClientImpl(@Value("#{@scrapperInfo.pathForRequests}") String baseURL){
        this.baseURL = baseURL;
    }

    @Override
    public LinkResponse addLink(long idChat, AddLinkRequest addLinkRequest) {
        WebClient webClient = WebClient.create(baseURL);
        LinkResponse result = webClient
                .post().uri("/links")
                .body(Mono.just(addLinkRequest), AddLinkRequest.class)
                .header("Tg-Chat-Id", Long.valueOf(idChat).toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return response.bodyToMono(LinkResponse.class).flatMap(error -> {
                            return Mono.error(new RuntimeException(error.getExceptionMessage()));
                    });
                })
                .bodyToMono(LinkResponse.class).block();
        return result;
    }

    @Override
    public LinkResponse removeLink(long idChat, RemoveLinkRequest removeLinkRequest) {
        WebClient webClient = WebClient.create(baseURL);
        LinkResponse result = webClient
                .method(HttpMethod.DELETE).uri("/links")
                .body(Mono.just(removeLinkRequest), RemoveLinkRequest.class)
                .header("Tg-Chat-Id", Long.toString(idChat))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return response.bodyToMono(LinkResponse.class).flatMap(error -> {
                            return Mono.error(new RuntimeException(error.getExceptionMessage()));
                    });
                })
                .bodyToMono(LinkResponse.class).block();
        return result;
    }

    @Override
    public ListLinksResponse allLinksFromChat(long idChat) {
        WebClient webClient = WebClient.create(baseURL);
        ListLinksResponse result = webClient
                .get().uri("/links").header("Tg-Chat-Id", Long.valueOf(idChat).toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return response.bodyToMono(ListLinksResponse.class).flatMap(error -> {
                            return Mono.error(new RuntimeException(error.getExceptionMessage()));
                    });
                })
                .bodyToMono(ListLinksResponse.class).block();
        return result;
    }

    @Override
    public void registryChat(long idChat) {
        WebClient webClient = WebClient.create(baseURL);
        webClient
                .post().uri("/tg-chat/{idChat}", (int)idChat)
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST)){
                        return clientResponse.bodyToMono(ApiErrorResponse.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error.exceptionMessage())));
                    }
                    return Mono.empty();
                }).block();
    }

    @Override
    public void removeChat(long idChat) {
        WebClient webClient = WebClient.create(baseURL);
        webClient
                .delete().uri("/tg-chat/{idChat}", idChat)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST)){
                        return clientResponse.bodyToMono(ApiErrorResponse.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error.exceptionMessage())));
                    }
                    return Mono.empty();
                }).block();
    }
}
