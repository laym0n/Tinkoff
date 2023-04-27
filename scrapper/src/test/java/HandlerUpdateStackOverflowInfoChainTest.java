import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.*;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.HandlerUpdateStackOverflowInfoChain;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.stackoverflow.CompareStackOverflowInfoStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.stackoverflow.StackOverflowBuilderLinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ContextConfiguration
public class HandlerUpdateStackOverflowInfoChainTest {
    @Test
    public void handleNotUpdatedLinkTest(){
        //Assign
        final int idWebSiteInfo = 100;
        int[] chatIds = IntStream.of(1, 2, 3, 4).toArray();
        final OffsetDateTime lastCheckUpdateDate = OffsetDateTime.now();

        Map<Integer, StackOverflowComment> commentsForArgumentForSUT = Stream.of(
                new StackOverflowComment(1),
                new StackOverflowComment(2),
                new StackOverflowComment(3)
        ).collect(Collectors.toMap(i->i.getIdComment(), i->i));
        Map<Integer, StackOverflowAnswer> answersForArgumentForSUT = Stream.of(
                new StackOverflowAnswer(1, "user1", lastCheckUpdateDate),
                new StackOverflowAnswer(2, "user2", lastCheckUpdateDate),
                new StackOverflowAnswer(3, "user3", lastCheckUpdateDate)
        ).collect(Collectors.toMap(i->i.getIdAnswer(), i->i));
        StackOverflowLinkInfo linkInfoForArgumentForSUT = new StackOverflowLinkInfo(12345);
        StackOverflowInfo argumentForSUT = new StackOverflowInfo(idWebSiteInfo, lastCheckUpdateDate,
                linkInfoForArgumentForSUT, commentsForArgumentForSUT, answersForArgumentForSUT);

        UpdateWebsiteInfoDAService<ResultOfCompareStackOverflowInfo> mockDAService = mock(UpdateWebsiteInfoDAService.class);
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
        when(mockStackOverflowClient.getStackOverflowResponse(eq(linkInfoForArgumentForSUT)))
                .thenReturn(valueForReturn);

        HandlerUpdateStackOverflowInfoChain sut = new HandlerUpdateStackOverflowInfoChain(mockDAService,
                new CompareStackOverflowInfoStrategy(), new StackOverflowBuilderLinkUpdateRequest(), mockStackOverflowClient);

        //Action
        Optional<LinkUpdateRequest> optionalResultFromSUT = sut.updateWebsiteInfo(argumentForSUT);

        //Assert
        assertFalse(optionalResultFromSUT.isPresent(), ()->"LinkUpdateRequest must be null. Description of LinkUpdateRequest is " +
                optionalResultFromSUT.get().getDescription());
        verify(mockDAService,never()).applyChanges(any());
    }
    @ParameterizedTest
    @ArgumentsSource(DataArgumentsProvider.class)
    public void handleUpdatedLinkTest(StackOverflowResponse responseForReturnFromWebClient,
                                      StackOverflowInfo argumentForSUT,
                                      LinkUpdateRequest expectedResultRequest,
                                      ResultOfCompareStackOverflowInfo expectedResultOfCompare,
                                      int[] chatIds){
        //Assign
        UpdateWebsiteInfoDAService<ResultOfCompareStackOverflowInfo> mockDAService = mock(UpdateWebsiteInfoDAService.class);
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
        assertTrue(Arrays.equals(expectedResultRequest.getTgChatIds(), resultFromSUT.getTgChatIds()),
                ()->"Expected chat Ids is " + expectedResultRequest.getTgChatIds() +
                        " but result chat Ids is " + resultFromSUT.getTgChatIds());
        assertEquals(expectedResultRequest.getUri(), resultFromSUT.getUri(),
                ()->"Expected URI is " + expectedResultRequest.getUri() +
                        " but result URI is " + resultFromSUT.getUri());

        ArgumentCaptor<ResultOfCompareStackOverflowInfo> argumentOfApplyChanges =
                ArgumentCaptor.forClass(ResultOfCompareStackOverflowInfo.class);
        verify(mockDAService).applyChanges(argumentOfApplyChanges.capture());
        ResultOfCompareStackOverflowInfo compareResultFromSUT =
                argumentOfApplyChanges.getValue();
        assertTrue(compareResultFromSUT.isDifferent(),
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
                final OffsetDateTime lastEditDateForSavedAnswer = OffsetDateTime.now().minusDays(5);
                final OffsetDateTime lastEditDateForLoadedAnswer = OffsetDateTime.now();


                final OffsetDateTime lastCheckUpdateDate = OffsetDateTime.now();
                int[] chatIdsForFirstTest = {1, 2, 3, 4};

                StackOverflowAnswerResponse[] answersResponsesForResponseFromWebClient = Stream.of(
                        new StackOverflowAnswerResponse(new StackOverflowUserResponse("user1"), creationDateForAnswersAndComments,
                                creationDateForAnswersAndComments, 1),
                        new StackOverflowAnswerResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments,
                                creationDateForAnswersAndComments, 2),
                        new StackOverflowAnswerResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments,
                                creationDateForAnswersAndComments, 3),
                        new StackOverflowAnswerResponse(new StackOverflowUserResponse("user10"),
                                lastEditDateForSavedAnswer,
                                lastEditDateForLoadedAnswer, 10)
                        ).toArray(StackOverflowAnswerResponse[]::new);
                StackOverflowCommentResponse[] commentResponsesForResponseFromWebClient = Stream.of(
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user1"), creationDateForAnswersAndComments, 1),
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments, 2),
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments, 3)
                        ).toArray(StackOverflowCommentResponse[]::new);
                StackOverflowResponse responseFromWebClient =
                        new StackOverflowResponse(new StackOverflowAnswersResponse(answersResponsesForResponseFromWebClient),
                                new StackOverflowCommentsResponse(commentResponsesForResponseFromWebClient));

                Map<Integer, StackOverflowComment> commentsForArgumentForSUT = Stream.of(
                        new StackOverflowComment(5),
                        new StackOverflowComment(1),
                        new StackOverflowComment(4)
                ).collect(Collectors.toMap(i->i.getIdComment(), i->i));
                Map<Integer, StackOverflowAnswer> answersForArgumentForSUT = Stream.of(
                        new StackOverflowAnswer(5, "user5", creationDateForAnswersAndComments),
                        new StackOverflowAnswer(4, "user4", creationDateForAnswersAndComments),
                        new StackOverflowAnswer(1, "user1", creationDateForAnswersAndComments),
                        new StackOverflowAnswer(10, "user10", lastEditDateForSavedAnswer)
                ).collect(Collectors.toMap(i->i.getIdAnswer(), i->i));
                StackOverflowLinkInfo linkInfoForSUT = new StackOverflowLinkInfo(12345);
                StackOverflowInfo argumentForSUT = new StackOverflowInfo(idWebSiteInfo, lastCheckUpdateDate, linkInfoForSUT,
                        commentsForArgumentForSUT, answersForArgumentForSUT);

                StackOverflowComment[] deletedCommentsForExpectedResultOfCompare = Stream.of(
                        new StackOverflowComment(5),
                        new StackOverflowComment(4)
                ).toArray(StackOverflowComment[]::new);
                StackOverflowAnswer[] deletedAnswersExpectedForResultOfCompare = Stream.of(
                        new StackOverflowAnswer(5, "user5", creationDateForAnswersAndComments),
                        new StackOverflowAnswer(4, "user4", creationDateForAnswersAndComments)
                ).toArray(StackOverflowAnswer[]::new);
                StackOverflowLinkInfo linkInfoForExpectedResultOfCompare = new StackOverflowLinkInfo(12345);
                StackOverflowAnswerResponse[] addedAnswersResponsesForExpectedCompareResult = Stream.of(
                                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments,
                                        creationDateForAnswersAndComments, 2),
                                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments,
                                        creationDateForAnswersAndComments, 3))
                        .toArray(StackOverflowAnswerResponse[]::new);
                StackOverflowCommentResponse[] addedCommentsResponsesForExpectedCompareResult = Stream.of(
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user2"), creationDateForAnswersAndComments, 2),
                        new StackOverflowCommentResponse(new StackOverflowUserResponse("user3"), creationDateForAnswersAndComments, 3)
                ).toArray(StackOverflowCommentResponse[]::new);
                StackOverflowAnswerResponse[] editedAnswersResponsesForExpectedCompareResult = Stream.of(
                                new StackOverflowAnswerResponse(new StackOverflowUserResponse("user10"),
                                        lastEditDateForSavedAnswer,
                                        lastEditDateForLoadedAnswer, 10)
                        ).toArray(StackOverflowAnswerResponse[]::new);
                ResultOfCompareStackOverflowInfo expectedResultOfCompare = new ResultOfCompareStackOverflowInfo(true, linkInfoForExpectedResultOfCompare,
                        idWebSiteInfo, deletedAnswersExpectedForResultOfCompare, addedAnswersResponsesForExpectedCompareResult,
                        editedAnswersResponsesForExpectedCompareResult, deletedCommentsForExpectedResultOfCompare,
                        addedCommentsResponsesForExpectedCompareResult);

                LinkUpdateRequest expectedResultRequest = new StackOverflowBuilderLinkUpdateRequest().buildLinkUpdateRequest(expectedResultOfCompare, chatIdsForFirstTest);

                firstArguments = Arguments.of(responseFromWebClient, argumentForSUT, expectedResultRequest,
                        expectedResultOfCompare, chatIdsForFirstTest.clone());
            }
            return Stream.of(
                    firstArguments);
        }
    }
}
