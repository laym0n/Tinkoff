package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswersResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentsResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowQuestionsResponse;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {
    private WebClient webClient;
    public StackOverflowClientImpl(){
        this("https://api.stackexchange.com");
    }
    public StackOverflowClientImpl(String baseURL){
        webClient = WebClient.create(baseURL);
    }
    @Override
    public StackOverflowResponse getStackOverflowResponse(StackOverflowLinkInfo linkInfo) {
        StackOverflowCommentsResponse comments = getStackOverflowCommentsResponse(linkInfo);
        StackOverflowAnswersResponse answers = getStackOverflowAnswersResponse(linkInfo);

        StackOverflowResponse result = new StackOverflowResponse(answers, comments);

        return result;
    }
    private StackOverflowQuestionsResponse getStackoverflowQuestionsResponse(StackOverflowLinkInfo linkInfo){
        StackOverflowQuestionsResponse stackoverflowQuestionsResponse = webClient
                .get()
                .uri("/2.3/questions/{idAnswer}?order=desc&sort=activity&site=stackoverflow",
                        linkInfo.idQuestion())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackOverflowQuestionsResponse.class).block();
        return stackoverflowQuestionsResponse;
    }
    private StackOverflowAnswersResponse getStackOverflowAnswersResponse(StackOverflowLinkInfo linkInfo){
        StackOverflowAnswersResponse answersResponse = webClient
                .get()
                .uri("/2.3/questions/{ids}/answers?order=desc&sort=activity&site=stackoverflow",
                        linkInfo.idQuestion())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackOverflowAnswersResponse.class).block();
        return answersResponse;
    }
    private StackOverflowCommentsResponse getStackOverflowCommentsResponse(StackOverflowLinkInfo linkInfo){
        StackOverflowCommentsResponse commentsResponse = webClient
                .get()
                .uri("/2.3/questions/{idAnswer}/comments?order=desc&sort=creation&site=stackoverflow",
                        linkInfo.idQuestion())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackOverflowCommentsResponse.class).block();
        return commentsResponse;
    }
}
