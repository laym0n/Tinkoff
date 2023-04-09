package ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.StackOverflowLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow.StackOverflowAnswersResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow.StackOverflowCommentsResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow.StackoverflowQuestionsResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackoverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowUser;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public StackoverflowInfo getStackoverflowInfo(StackOverflowLinkInfo linkInfo) {
        StackoverflowQuestionsResponse stackoverflowQuestionsResponse =
                getStackoverflowQuestionsResponse(linkInfo);
        if(stackoverflowQuestionsResponse.getItems().equals(null) || stackoverflowQuestionsResponse.getItems().length == 0)
            new RuntimeException(linkInfo.getDescriptionOfParsedLink() +
                    " don't exist");
        StackOverflowAnswersResponse answersResponse = getStackOverflowAnswersResponse(linkInfo);
        StackOverflowCommentsResponse commentsResponse = getStackOverflowCommentsResponse(linkInfo);

        StackoverflowInfo result = getStackOverflowInfo(linkInfo, stackoverflowQuestionsResponse,
                answersResponse, commentsResponse);

        return result;
    }
    private StackoverflowQuestionsResponse getStackoverflowQuestionsResponse(StackOverflowLinkInfo linkInfo){
        StackoverflowQuestionsResponse stackoverflowQuestionsResponse = webClient
                .get()
                .uri("/2.3/questions/{idAnswer}?order=desc&sort=activity&site=stackoverflow",
                        linkInfo.idQuestion())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackoverflowQuestionsResponse.class).block();
        return stackoverflowQuestionsResponse;
    }
    private StackOverflowAnswersResponse getStackOverflowAnswersResponse(StackOverflowLinkInfo linkInfo){
        StackOverflowAnswersResponse answersResponse = webClient
                .get()
                .uri("/2.3/questions/{idAnswer}/answers?order=desc&sort=activity&site=stackoverflow",
                        linkInfo.idQuestion())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackOverflowAnswersResponse.class).block();
        return answersResponse;
    }
    private StackOverflowCommentsResponse getStackOverflowCommentsResponse(StackOverflowLinkInfo linkInfo){
        StackOverflowCommentsResponse commentsResponse = webClient
                .get()
                .uri("/2.3/questions/{idAnswer}/comments?order=desc&sort=activity&site=stackoverflow",
                        linkInfo.idQuestion())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StackOverflowCommentsResponse.class).block();
        return commentsResponse;
    }

    private StackoverflowInfo getStackOverflowInfo(StackOverflowLinkInfo linkInfo,
                                                   StackoverflowQuestionsResponse stackoverflowQuestionsResponse,
                                                   StackOverflowAnswersResponse answersResponse,
                                                   StackOverflowCommentsResponse commentsResponse){
        Set<StackOverflowComment> comments = Arrays.stream(commentsResponse.items())
                .map(comment -> {
                    StackOverflowUser user = new StackOverflowUser(comment.owner().name());
                    StackOverflowComment stackOverflowComment = new StackOverflowComment(user,
                            comment.idComment());
                    return stackOverflowComment;
                }).collect(Collectors.toSet());
        Set<StackOverflowAnswer> answers = Arrays.stream(answersResponse.items())
                .map(answer->{
                    StackOverflowUser user = new StackOverflowUser(answer.owner().name());

                    StackOverflowAnswer result = new StackOverflowAnswer(user, answer.answerId(),
                            Optional.of(answer.lastEditDate()).orElse(answer.creationDate()));
                    return result;
                }).collect(Collectors.toSet());
        StackoverflowInfo result = new StackoverflowInfo(linkInfo, comments, answers);
        return result;
    }
    @Override
    public WebsiteInfo getWebSiteInfoByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof StackOverflowLinkInfo))
            return null;
        return getStackoverflowInfo((StackOverflowLinkInfo) linkInfo);
    }
}
