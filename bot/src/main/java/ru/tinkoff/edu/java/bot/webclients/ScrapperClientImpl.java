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

@Component
public class ScrapperClientImpl implements ScrapperClient {
    private static final String HEADER_NAME_OF_TG_CHAT_ID = "Tg-Chat-Id";
    private static final String LINK_FOR_REST_TO_CHAT_CONTROLLER = "/tg-chat/{idChat}";
    private static final String LINK_FOR_REST_TO_LINK_CONTROLLER = "/links";
    private String baseURL;

    public ScrapperClientImpl(@Value("#{@scrapperInfo.pathForRequests}") String baseURL) {
        this.baseURL = baseURL;
    }

    @Override
    public LinkResponse addLink(long idChat, AddLinkRequest addLinkRequest) {
        WebClient webClient = WebClient.create(baseURL);
        return webClient
                .post()
                .uri(LINK_FOR_REST_TO_LINK_CONTROLLER)
                .body(Mono.just(addLinkRequest), AddLinkRequest.class)
                .header(HEADER_NAME_OF_TG_CHAT_ID, Long.valueOf(idChat).toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(LinkResponse.class)
                        .flatMap(error ->
                            Mono.error(new RuntimeException(error.getExceptionMessage())))
                )
                .bodyToMono(LinkResponse.class).block();
    }

    @Override
    public LinkResponse removeLink(long idChat, RemoveLinkRequest removeLinkRequest) {
        WebClient webClient = WebClient.create(baseURL);
        return webClient
                .method(HttpMethod.DELETE).uri(LINK_FOR_REST_TO_LINK_CONTROLLER)
                .body(Mono.just(removeLinkRequest), RemoveLinkRequest.class)
                .header(HEADER_NAME_OF_TG_CHAT_ID, Long.toString(idChat))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                    response -> response
                        .bodyToMono(LinkResponse.class)
                        .flatMap(error ->
                            Mono.error(new RuntimeException(error.getExceptionMessage()))
                        )
                )
                .bodyToMono(LinkResponse.class).block();
    }

    @Override
    public ListLinksResponse allLinksFromChat(long idChat) {
        WebClient webClient = WebClient.create(baseURL);
        return webClient
                .get().uri(LINK_FOR_REST_TO_LINK_CONTROLLER)
                .header(HEADER_NAME_OF_TG_CHAT_ID, Long.valueOf(idChat).toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                    response ->
                        response
                            .bodyToMono(ListLinksResponse.class)
                            .flatMap(error ->
                                Mono.error(new RuntimeException(error.getExceptionMessage()))
                            )
                )
                .bodyToMono(ListLinksResponse.class).block();
    }

    @Override
    public void registryChat(long idChat) {
        WebClient webClient = WebClient.create(baseURL);
        webClient
                .post()
                .uri(LINK_FOR_REST_TO_CHAT_CONTROLLER, (int) idChat)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST)) {
                        return clientResponse.bodyToMono(ApiErrorResponse.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error.exceptionMessage())));
                    }
                    return Mono.empty();
                })
                .block();
    }

    @Override
    public void removeChat(long idChat) {
        WebClient webClient = WebClient.create(baseURL);
        webClient
                .delete()
                .uri(LINK_FOR_REST_TO_CHAT_CONTROLLER, idChat)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST)) {
                        return clientResponse.bodyToMono(ApiErrorResponse.class)
                                .flatMap(error ->
                                    Mono.error(new RuntimeException(error.exceptionMessage()))
                                );
                    }
                    return Mono.empty();
                })
                .block();
    }
}
