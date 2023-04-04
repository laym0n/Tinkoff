package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.StackOverflowInfo;
import parserservice.dto.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowInfoResponse;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsite;

import java.time.OffsetDateTime;

@Component
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
                .get()
                .uri("/2.3/posts/{idAnswer}?order=desc&sort=activity&site=stackoverflow", stackOverflowInfo.idAnswer())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackoverflowResponse.class).block();
        if(stackoverflowResponse.getItems().equals(null) || stackoverflowResponse.getItems().length == 0)
            new RuntimeException(stackOverflowInfo.getDescriptionOfParsedWebsite() +
                    " don't exist");
        return stackoverflowResponse != null && stackoverflowResponse.getItems().length >= 1? stackoverflowResponse.getItems()[0] : null;
    }

    @Override
    public OffsetDateTime checkUpdateOfWebsite(WebsiteInfo websiteInfo) {
        if(!(websiteInfo instanceof StackOverflowInfo))
            return null;
        StackOverflowInfo stackOverflowInfo = (StackOverflowInfo) websiteInfo;
        StackOverflowInfoResponse stackOverflowInfoResponse = getUpdateInfo(stackOverflowInfo);
        return stackOverflowInfoResponse.getLastEditDate();
    }
}
