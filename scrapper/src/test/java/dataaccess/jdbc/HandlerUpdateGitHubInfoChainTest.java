package dataaccess.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.*;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.HandlerUpdateGitHubInfoChain;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.github.CompareGitHubInfoStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.github.GitHubBuilderLinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;

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
public class HandlerUpdateGitHubInfoChainTest {
    @Test
    public void handleNotUpdatedLinkTest(){
        //Assign
        final int idWebSiteInfo = 100;
        int[] chatIds = IntStream.of(1, 2, 3, 4).toArray();
        final OffsetDateTime lastActivityDate = OffsetDateTime.now();
        final OffsetDateTime lastCheckUpdateDate = OffsetDateTime.now();

        Map<String, GitHubBranch> branchesForArgument = Stream.of(
                new GitHubBranch("hw1"),
                new GitHubBranch("hw2"),
                new GitHubBranch("hw3")
        ).collect(Collectors.toMap(i->i.getBranchName(), i->i));

        Map<String, GitHubCommit> commitsForArgument = Stream.of(
                new GitHubCommit("12345"),
                new GitHubCommit("67890")
        ).collect(Collectors.toMap(i->i.getSha(), i->i));

        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("drownedtears", "forum");
        GitHubInfo argumentGitHubInfo = new GitHubInfo(idWebSiteInfo, lastCheckUpdateDate,
                linkInfoForArgument, branchesForArgument, commitsForArgument ,lastActivityDate);

        UpdateWebsiteInfoDAService<ResultOfCompareGitHubInfo> mockDAService = mock(UpdateWebsiteInfoDAService.class);
        when(mockDAService.getAllChatIdWithTrackedIdWebsiteInfo(eq(idWebSiteInfo)))
                .thenReturn(chatIds.clone());

        GitHubBranchResponse[] branchResponses = Stream.of(new GitHubBranchResponse("hw1"),
                new GitHubBranchResponse("hw2"),
                new GitHubBranchResponse("hw3")).toArray(GitHubBranchResponse[]::new);
        GitHubCommitResponse[] commitResponses = Stream.of(
                new GitHubCommitResponse("12345", new GitHubNestedCommitResponse(new GitHubCommiterResponse(OffsetDateTime.now()))),
                new GitHubCommitResponse("67890", new GitHubNestedCommitResponse(new GitHubCommiterResponse(OffsetDateTime.now()))))
                .toArray(GitHubCommitResponse[]::new);
        GitHubResponse valueForReturn = new GitHubResponse(new GitHubInfoResponse(lastActivityDate),
                branchResponses, commitResponses);
        GitHubClient mockGitHubClient = mock(GitHubClient.class);
        when(mockGitHubClient.getGitHubResponse(eq(linkInfoForArgument)))
                .thenReturn(valueForReturn);

        HandlerUpdateGitHubInfoChain sut = new HandlerUpdateGitHubInfoChain(mockDAService,
                new CompareGitHubInfoStrategy(), new GitHubBuilderLinkUpdateRequest(), mockGitHubClient);

        //Action
        Optional<LinkUpdateRequest> optionalResultFromSUT = sut.updateWebsiteInfo(argumentGitHubInfo);

        //Assert
        assertFalse(optionalResultFromSUT.isPresent(), ()->"LinkUpdateRequest must be null.\n Description of update is "
        + optionalResultFromSUT.get().getDescription());
        verify(mockDAService, never()).applyChanges(any());
    }
    @ParameterizedTest
    @ArgumentsSource(DataArgumentsProvider.class)
    public void handleUpdatedLinkTest(GitHubResponse responseForReturnFromWebClient,
                                      GitHubInfo argumentForSUT,
                                      LinkUpdateRequest expectedResultRequest,
                                      ResultOfCompareGitHubInfo expectedResultOfCompare,
                                      int[] chatIds){
        //Assign
        UpdateWebsiteInfoDAService<ResultOfCompareGitHubInfo> mockDAService = mock(UpdateWebsiteInfoDAService.class);
        when(mockDAService.getAllChatIdWithTrackedIdWebsiteInfo(eq(argumentForSUT.getId())))
                .thenReturn(chatIds.clone());

        GitHubClient mockGitHubClient = mock(GitHubClient.class);
        when(mockGitHubClient.getGitHubResponse(eq(argumentForSUT.getLinkInfo())))
                .thenReturn(responseForReturnFromWebClient);

        HandlerUpdateGitHubInfoChain sut = new HandlerUpdateGitHubInfoChain(mockDAService,
                new CompareGitHubInfoStrategy(), new GitHubBuilderLinkUpdateRequest(), mockGitHubClient);

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

        ArgumentCaptor<ResultOfCompareGitHubInfo> argumentOfApplyChanges =
                ArgumentCaptor.forClass(ResultOfCompareGitHubInfo.class);
        verify(mockDAService).applyChanges(argumentOfApplyChanges.capture());

        ResultOfCompareGitHubInfo compareResultFromSUT = argumentOfApplyChanges.getValue();
        assertTrue(compareResultFromSUT.isDifferent(), ()->"Input data is not equal for load data but compareResult.isDifferent is False");
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

                final OffsetDateTime lastCheckUpdateDate = OffsetDateTime.now();
                int[] chatIdsForFirstTest = {1, 2, 3, 4};

                final OffsetDateTime lastActivityDateForBoth = OffsetDateTime.now();
                GitHubBranchResponse[] branchResponses = Stream.of(
                        new GitHubBranchResponse("hw3"),
                        new GitHubBranchResponse("hw4"),
                        new GitHubBranchResponse("hw5")).toArray(GitHubBranchResponse[]::new);
                GitHubCommitResponse[] commitResponses = Stream.of(
                                new GitHubCommitResponse("qwert", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))),
                                new GitHubCommitResponse("67890", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))),
                                new GitHubCommitResponse("asdfg", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))),
                                new GitHubCommitResponse("zxcvb", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))))
                        .toArray(GitHubCommitResponse[]::new);
                GitHubResponse valueForReturn = new GitHubResponse(new GitHubInfoResponse(lastActivityDateForBoth),
                        branchResponses, commitResponses);

                Map<String, GitHubBranch> branches = Stream.of(
                        new GitHubBranch("hw1"),
                        new GitHubBranch("hw2"),
                        new GitHubBranch("hw3")
                ).collect(Collectors.toMap(i->i.getBranchName(), i->i));
                Map<String, GitHubCommit> commits = Stream.of(
                        new GitHubCommit("12345"),
                        new GitHubCommit("67890"),
                        new GitHubCommit("qwert")
                ).collect(Collectors.toMap(i->i.getSha(), i->i));;
                GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("drownedtears", "forum");
                GitHubInfo argumentGitHubInfo = new GitHubInfo(idWebSiteInfo, lastCheckUpdateDate,
                        linkInfoForArgument, branches, commits, lastActivityDateForBoth);

                GitHubBranch[] expectedDroppedBranches = Stream.of(
                        new GitHubBranch("hw1"),
                        new GitHubBranch("hw2")
                ).toArray(GitHubBranch[]::new);
                GitHubCommit[] expectedDroppedCommits = Stream.of(
                        new GitHubCommit("12345")
                ).toArray(GitHubCommit[]::new);
                GitHubLinkInfo expectedLinkInfoInExpectedResult = new GitHubLinkInfo("drownedtears", "forum");
                GitHubBranchResponse[] expectedPushedBranches = Stream.of(
                        new GitHubBranchResponse("hw4"),
                        new GitHubBranchResponse("hw5")).toArray(GitHubBranchResponse[]::new);
                GitHubCommitResponse[] expectedPushedCommit = Stream.of(
                                new GitHubCommitResponse("asdfg", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))),
                                new GitHubCommitResponse("zxcvb", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))))
                        .toArray(GitHubCommitResponse[]::new);
                ResultOfCompareGitHubInfo expectedResultOfCompare =
                        new ResultOfCompareGitHubInfo(true, expectedLinkInfoInExpectedResult, idWebSiteInfo, expectedDroppedCommits, expectedPushedCommit,
                                expectedDroppedBranches, expectedPushedBranches, Optional.empty());

                LinkUpdateRequest expectedResultRequest = new GitHubBuilderLinkUpdateRequest().buildLinkUpdateRequest(expectedResultOfCompare,
                        chatIdsForFirstTest);

                firstArguments = Arguments.of(valueForReturn, argumentGitHubInfo, expectedResultRequest,
                        expectedResultOfCompare, chatIdsForFirstTest.clone());
            }
            return Stream.of(
                    firstArguments);
        }
    }
}
