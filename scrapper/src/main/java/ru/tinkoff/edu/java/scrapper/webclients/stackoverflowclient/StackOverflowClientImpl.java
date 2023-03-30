package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowInfoResponse;

public class StackOverflowClientImpl implements StackOverflowClient {
    private String baseURL;
    public StackOverflowClientImpl(){
        this("https://api.stackexchange.com");
    }
    public StackOverflowClientImpl(String baseURL){
        this.baseURL = baseURL;
    }
    @Override
    public StackOverflowInfoResponse getUpdateInfo(StackOverflowInfo stackOverflowInfo) {
        WebClient webClient = WebClient.create(baseURL);
        StackoverflowResponse stackoverflowResponse = webClient
                .get().uri("/2.3/posts/{idAnswer}?order=desc&sort=activity&site=stackoverflow", 285177)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackoverflowResponse.class).block();
        return stackoverflowResponse != null && stackoverflowResponse.getItems().length >= 1? stackoverflowResponse.getItems()[0] : null;
    }
}
