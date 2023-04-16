import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.*;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.*;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.HandlerUpdateStackOverflowInfoChain;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.stackoverflow.CompareStackOverflowInfoStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.stackoverflow.StackOverflowBuilderLinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HandlerUpdateStackOverflowInfoChainTest {
    @Test
    public void handleNotUpdatedLinkTest(){
        //Assign
        final int idWebSiteInfo = 100;
        int[] chatIds = IntStream.of(1, 2, 3, 4).toArray();
        final OffsetDateTime lastCheckUpdateDate = OffsetDateTime.now();

        Set<StackOverflowComment> comments = new HashSet<>();
        comments.add(new StackOverflowComment(1, "user1"));
        comments.add(new StackOverflowComment(2, "user2"));
        comments.add(new StackOverflowComment(3, "user3"));

        Set<StackOverflowAnswer> answers = new HashSet<>();
        answers.add(new StackOverflowAnswer(1, "user1", lastCheckUpdateDate));
        answers.add(new StackOverflowAnswer(2, "user2", lastCheckUpdateDate));
        answers.add(new StackOverflowAnswer(3, "user3", lastCheckUpdateDate));

        StackOverflowLinkInfo expectedLinkInfo = new StackOverflowLinkInfo(12345);
        StackOverflowInfo argumentGitHubInfo = new StackOverflowInfo(idWebSiteInfo, lastCheckUpdateDate,
                expectedLinkInfo, comments, answers);

        UpdateWebsiteInfoDAService<StackOverflowInfo, StackOverflowResponse> mockDAService = mock(UpdateWebsiteInfoDAService.class);
        when(mockDAService.getAllChatIdWithTrackedIdWebsiteInfo(eq(idWebSiteInfo)))
                .thenReturn(chatIds.clone());

        StackOverflowAnswerResponse[] answersResponse = Stream.of(
                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user3"),
                        lastCheckUpdateDate, lastCheckUpdateDate, 3),
                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user2"),
                        lastCheckUpdateDate, lastCheckUpdateDate, 2),
                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user1"),
                        lastCheckUpdateDate, lastCheckUpdateDate, 1)
        ).toArray(StackOverflowAnswerResponse[]::new);
        StackOverflowCommentResponse[] commentsResponse = Stream.of(
                new StackOverflowCommentResponse(new StackOverflowUserResponse("user1"), lastCheckUpdateDate, 1),
                new StackOverflowCommentResponse(new StackOverflowUserResponse("user3"), lastCheckUpdateDate, 3),
                new StackOverflowCommentResponse(new StackOverflowUserResponse("user2"), lastCheckUpdateDate, 2)
        ).toArray(StackOverflowCommentResponse[]::new);
        StackOverflowResponse valueForReturn = new StackOverflowResponse(
                new StackOverflowAnswersResponse(answersResponse),
                new StackOverflowCommentsResponse(commentsResponse)
        );
        StackOverflowClient mockStackOverflowClient = mock(StackOverflowClient.class);
        when(mockStackOverflowClient.getStackOverflowResponse(eq(expectedLinkInfo)))
                .thenReturn(valueForReturn);

        HandlerUpdateStackOverflowInfoChain sut = new HandlerUpdateStackOverflowInfoChain(mockDAService,
                new CompareStackOverflowInfoStrategy(), new StackOverflowBuilderLinkUpdateRequest(), mockStackOverflowClient);

        //Action
        Optional<LinkUpdateRequest> optionalResultFromSUT = sut.updateWebsiteInfo(argumentGitHubInfo);

        //Assert
        assertFalse(optionalResultFromSUT.isPresent(), ()->"LinkUpdateRequest must be null. Description of LinkUpdateRequest is " +
                optionalResultFromSUT.get().getDescription());
        ArgumentCaptor<ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse>> argumentOfApplyChanges =
                ArgumentCaptor.forClass(ResultOfCompareWebsiteInfo.class);
        verify(mockDAService).applyChanges(argumentOfApplyChanges.capture());
        ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse> compareResult = argumentOfApplyChanges.getValue();
        assertFalse(compareResult.isDifferent, ()->"Input data is equal for load data but compareResult.isDifferent is True");
    }
    @ParameterizedTest
    @ArgumentsSource(DataArgumentsProvider.class)
    public void handleUpdatedLinkTest(StackOverflowResponse responseForReturnFromWebClient,
                                      StackOverflowInfo argumentForSUT,
                                      LinkUpdateRequest expectedResultRequest,
                                      ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse> expectedResultOfCompare,
                                      int[] chatIds){
        //Assign
        UpdateWebsiteInfoDAService<StackOverflowInfo, StackOverflowResponse> mockDAService = mock(UpdateWebsiteInfoDAService.class);
        when(mockDAService.getAllChatIdWithTrackedIdWebsiteInfo(eq(argumentForSUT.getId())))
                .thenReturn(chatIds.clone());

        StackOverflowClient mockGitHubClient = mock(StackOverflowClient.class);
        when(mockGitHubClient.getStackOverflowResponse(eq(argumentForSUT.getLinkInfo())))
                .thenReturn(responseForReturnFromWebClient);

        HandlerUpdateStackOverflowInfoChain sut = new HandlerUpdateStackOverflowInfoChain(mockDAService,
                new CompareStackOverflowInfoStrategy(), new StackOverflowBuilderLinkUpdateRequest(), mockGitHubClient);

        //Action
        Optional<LinkUpdateRequest> optionalResultFromSUT = sut.updateWebsiteInfo(argumentForSUT);

        //Assert
        assertTrue(optionalResultFromSUT.isPresent(), ()->"LinkUpdateRequest must be null");
        LinkUpdateRequest resultFromSUT = optionalResultFromSUT.get();
        assertEquals(expectedResultRequest.getDescription(), resultFromSUT.getDescription(),
                ()->"Expected description is " + expectedResultRequest.getDescription() +
                " but result description is " + resultFromSUT.getDescription());
        assertTrue(Arrays.equals(expectedResultRequest.getTgChatIds(), resultFromSUT.getTgChatIds()),
                ()->"Expected chat Ids is " + expectedResultRequest.getTgChatIds() +
                        " but result chat Ids is " + resultFromSUT.getTgChatIds());
        assertEquals(expectedResultRequest.getUri(), resultFromSUT.getUri(),
                ()->"Expected URI is " + expectedResultRequest.getUri() +
                        " but result URI is " + resultFromSUT.getUri());

        ArgumentCaptor<ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse>> argumentOfApplyChanges =
                ArgumentCaptor.forClass(ResultOfCompareWebsiteInfo.class);
        verify(mockDAService).applyChanges(argumentOfApplyChanges.capture());
        ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse> compareResultFromSUT =
                argumentOfApplyChanges.getValue();
        assertTrue(compareResultFromSUT.isDifferent,
                ()->"Input data is not equal for load data but compareResult.isDifferent is False");
        assertEquals(expectedResultOfCompare, compareResultFromSUT,
                ()->"Expected result is " + expectedResultOfCompare +
                        " but result is " + compareResultFromSUT);
    }
    static class DataArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            Arguments firstArguments;
            {
                final int idWebSiteInfo = 100;
                final OffsetDateTime creationDateForAnswersAndComments = OffsetDateTime.now();

                final OffsetDateTime lastCheckUpdateDate = OffsetDateTime.now();
                int[] chatIdsForFirstTest = {1, 2, 3, 4};

                StackOverflowAnswerResponse[] answersResponse = Stream.of(
                        new StackOverflowAnswerResponse(new StackOverflowUserResponse("user1"), creationDateForAnswersAndComments,
                                creationDateForAnswersAndComments, 1),
                        new StackOverflowAnswerResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments,
                                creationDateForAnswersAndComments, 2),
                        new StackOverflowAnswerResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments,
                                creationDateForAnswersAndComments, 3))
                        .toArray(StackOverflowAnswerResponse[]::new);
                StackOverflowCommentResponse[] commentsResponses = Stream.of(
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user1"), creationDateForAnswersAndComments, 1),
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments, 2),
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments, 3)
                        ).toArray(StackOverflowCommentResponse[]::new);
                StackOverflowResponse valueForReturn =
                        new StackOverflowResponse(new StackOverflowAnswersResponse(answersResponse),
                                new StackOverflowCommentsResponse(commentsResponses));

                Set<StackOverflowComment> commentsForArgumentForSUT = Stream.of(
                        new StackOverflowComment(5, "user5"),
                        new StackOverflowComment(1, "user1"),
                        new StackOverflowComment(4, "user4")
                ).collect(Collectors.toSet());
                Set<StackOverflowAnswer> answersForArgumentForSUT = Stream.of(
                        new StackOverflowAnswer(5, "user5", creationDateForAnswersAndComments),
                        new StackOverflowAnswer(4, "user4", creationDateForAnswersAndComments),
                        new StackOverflowAnswer(1, "user1", creationDateForAnswersAndComments)
                ).collect(Collectors.toSet());
                StackOverflowLinkInfo linkInfoForSUT = new StackOverflowLinkInfo(12345);
                StackOverflowInfo argumentForSUT = new StackOverflowInfo(idWebSiteInfo, lastCheckUpdateDate, linkInfoForSUT,
                        commentsForArgumentForSUT, answersForArgumentForSUT);

                Set<StackOverflowComment> commentsForResultOfCompare = Stream.of(
                        new StackOverflowComment(5, "user5"),
                        new StackOverflowComment(4, "user4")
                ).collect(Collectors.toSet());
                Set<StackOverflowAnswer> answersForResultOfCompare = Stream.of(
                        new StackOverflowAnswer(5, "user5", creationDateForAnswersAndComments),
                        new StackOverflowAnswer(4, "user4", creationDateForAnswersAndComments)
                ).collect(Collectors.toSet());
                StackOverflowLinkInfo linkInfoForResultOfCompare = new StackOverflowLinkInfo(12345);
                StackOverflowInfo expectedUniqueWebsiteInfo = new StackOverflowInfo(idWebSiteInfo, lastCheckUpdateDate,
                        linkInfoForResultOfCompare, commentsForResultOfCompare, answersForResultOfCompare);
                StackOverflowAnswerResponse[] answersResponseForCompareResult = Stream.of(
                                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments,
                                        creationDateForAnswersAndComments, 2),
                                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments,
                                        creationDateForAnswersAndComments, 3))
                        .toArray(StackOverflowAnswerResponse[]::new);
                StackOverflowCommentResponse[] commentsResponsesCompareResult = Stream.of(
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments, 2),
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments, 3)
                ).toArray(StackOverflowCommentResponse[]::new);
                StackOverflowResponse expectedUniqueLoadedInfo =
                        new StackOverflowResponse(new StackOverflowAnswersResponse(answersResponseForCompareResult),
                                new StackOverflowCommentsResponse(commentsResponsesCompareResult));
                ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse> expectedResultOfCompare =
                        new ResultOfCompareWebsiteInfo<>(true, expectedUniqueWebsiteInfo, expectedUniqueLoadedInfo);

                StringBuilder stringBuilderForDescription = new StringBuilder();
                stringBuilderForDescription.append("Ссылка " + linkInfoForSUT.getPath() + " получила обновление:\n");
                for(StackOverflowAnswer answer : expectedResultOfCompare.uniqueSavedData.getAnswers()){
                    stringBuilderForDescription.append("Ответ от пользователя " + answer.getUserName() + " удален\n");
                }
                for(StackOverflowComment comment : expectedResultOfCompare.uniqueSavedData.getComments()){
                    stringBuilderForDescription.append("Комментарий от пользователя " + comment.getUserName() + " удален\n");
                }
                for(StackOverflowAnswerResponse answer : expectedResultOfCompare.uniqueLoadedData.getAnswers().getItems()){
                    stringBuilderForDescription.append("Ответ от пользователя " + answer.getOwner().getName() + " добавлен\n");
                }
                for(StackOverflowCommentResponse comment : expectedResultOfCompare.uniqueLoadedData.getComments().getItems()){
                    stringBuilderForDescription.append("Комментарий от пользователя " + comment.getOwner().getName() + " добавлен\n");
                }
                String description = stringBuilderForDescription.toString();
                LinkUpdateRequest expectedResultRequest = new LinkUpdateRequest(0, URI.create(linkInfoForSUT.getPath()), description, chatIdsForFirstTest.clone());

                firstArguments = Arguments.of(valueForReturn, argumentForSUT, expectedResultRequest,
                        expectedResultOfCompare, chatIdsForFirstTest.clone());
            }
            return Stream.of(
                    firstArguments);
        }
    }
}
