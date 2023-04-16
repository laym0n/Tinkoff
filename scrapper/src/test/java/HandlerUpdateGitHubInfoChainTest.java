import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.*;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.HandlerUpdateGitHubInfoChain;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.github.CompareGitHubInfoStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.github.GitHubBuilderLinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class HandlerUpdateGitHubInfoChainTest {
    @Test
    public void handleNotUpdatedLinkTest(){
        //Assign
        final int idWebSiteInfo = 100;
        int[] chatIds = IntStream.of(1, 2, 3, 4).toArray();
        final OffsetDateTime lastActivityDate = OffsetDateTime.now();
        final OffsetDateTime lastCheckUpdateDate = OffsetDateTime.now();

        Set<GitHubBranch> branches = new HashSet<>();
        branches.add(new GitHubBranch("hw1"));
        branches.add(new GitHubBranch("hw2"));
        branches.add(new GitHubBranch("hw3"));

        Set<GitHubCommit> commits = new HashSet<>();
        commits.add(new GitHubCommit("12345"));
        commits.add(new GitHubCommit("67890"));

        GitHubLinkInfo expectedLinkInfo = new GitHubLinkInfo("drownedtears", "forum");
        GitHubInfo argumentGitHubInfo = new GitHubInfo(idWebSiteInfo, lastCheckUpdateDate,
                expectedLinkInfo, branches, commits,lastActivityDate);

        UpdateWebsiteInfoDAService<GitHubInfo, GitHubResponse> mockDAService = mock(UpdateWebsiteInfoDAService.class);
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
        when(mockGitHubClient.getGitHubResponse(eq(expectedLinkInfo)))
                .thenReturn(valueForReturn);

        HandlerUpdateGitHubInfoChain sut = new HandlerUpdateGitHubInfoChain(mockDAService,
                new CompareGitHubInfoStrategy(), new GitHubBuilderLinkUpdateRequest(), mockGitHubClient);

        //Action
        Optional<LinkUpdateRequest> optionalResultFromSUT = sut.updateWebsiteInfo(argumentGitHubInfo);

        //Assert
        assertFalse(optionalResultFromSUT.isPresent(), ()->"LinkUpdateRequest must be null");
        ArgumentCaptor<ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse>> argumentOfApplyChanges =
                ArgumentCaptor.forClass(ResultOfCompareWebsiteInfo.class);
        verify(mockDAService).applyChanges(argumentOfApplyChanges.capture());
        ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse> compareResult = argumentOfApplyChanges.getValue();
        assertFalse(compareResult.isDifferent, ()->"Input data is equal for load data but compareResult.isDifferent is True");
    }
    @ParameterizedTest
    @ArgumentsSource(DataArgumentsProvider.class)
    public void handleUpdatedLinkTest(GitHubResponse responseForReturnFromWebClient,
                                      GitHubInfo argumentForSUT,
                                      LinkUpdateRequest expectedResultRequest,
                                      ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse> expectedResultOfCompare,
                                      int[] chatIds){
        //Assign
        UpdateWebsiteInfoDAService<GitHubInfo, GitHubResponse> mockDAService = mock(UpdateWebsiteInfoDAService.class);
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
        assertEquals(expectedResultRequest.getDescription(), resultFromSUT.getDescription(),
                ()->"Expected description is " + expectedResultRequest.getDescription() +
                " but result description is " + resultFromSUT.getDescription());
        assertTrue(Arrays.equals(expectedResultRequest.getTgChatIds(), resultFromSUT.getTgChatIds()),
                ()->"Expected chat Ids is " + expectedResultRequest.getTgChatIds() +
                        " but result chat Ids is " + resultFromSUT.getTgChatIds());
        assertEquals(expectedResultRequest.getUri(), resultFromSUT.getUri(),
                ()->"Expected URI is " + expectedResultRequest.getUri() +
                        " but result URI is " + resultFromSUT.getUri());

        ArgumentCaptor<ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse>> argumentOfApplyChanges =
                ArgumentCaptor.forClass(ResultOfCompareWebsiteInfo.class);
        verify(mockDAService).applyChanges(argumentOfApplyChanges.capture());

        ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse> compareResultFromSUT = argumentOfApplyChanges.getValue();
        assertTrue(compareResultFromSUT.isDifferent, ()->"Input data is not equal for load data but compareResult.isDifferent is False");
        boolean asd = expectedResultOfCompare.uniqueLoadedData.getCommits()[0].equals(compareResultFromSUT.uniqueLoadedData.getCommits()[0]);

        boolean asd2 = expectedResultOfCompare.uniqueLoadedData.getCommits()[1].equals(compareResultFromSUT.uniqueLoadedData.getCommits()[1]);
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

                final OffsetDateTime lastActivityDateForSavedForFirstTest = OffsetDateTime.now();
                final OffsetDateTime lastActivityDateForLoadedForFirstTest = OffsetDateTime.now().minusDays(5);
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
                GitHubResponse valueForReturn = new GitHubResponse(new GitHubInfoResponse(lastActivityDateForLoadedForFirstTest),
                        branchResponses, commitResponses);

                Set<GitHubBranch> branches = new HashSet<>();
                branches.add(new GitHubBranch("hw1"));
                branches.add(new GitHubBranch("hw2"));
                branches.add(new GitHubBranch("hw3"));
                Set<GitHubCommit> commits = new HashSet<>();
                commits.add(new GitHubCommit("12345"));
                commits.add(new GitHubCommit("67890"));
                commits.add(new GitHubCommit("qwert"));
                GitHubLinkInfo expectedLinkInfo = new GitHubLinkInfo("drownedtears", "forum");
                GitHubInfo argumentGitHubInfo = new GitHubInfo(idWebSiteInfo, lastCheckUpdateDate,
                        expectedLinkInfo, branches, commits, lastActivityDateForSavedForFirstTest);



                Set<GitHubBranch> uniqueBranches = new HashSet<>();
                uniqueBranches.add(new GitHubBranch("hw1"));
                uniqueBranches.add(new GitHubBranch("hw2"));
                Set<GitHubCommit> uniqueCommits = new HashSet<>();
                uniqueCommits.add(new GitHubCommit("12345"));
                GitHubLinkInfo expectedLinkInfoInExpectedResult = new GitHubLinkInfo("drownedtears", "forum");
                GitHubInfo expectedUniqueGitHubInfo = new GitHubInfo(idWebSiteInfo, lastCheckUpdateDate,
                        expectedLinkInfoInExpectedResult, uniqueBranches, uniqueCommits, lastActivityDateForSavedForFirstTest);
                GitHubBranchResponse[] uniqueBranchResponses = Stream.of(
                        new GitHubBranchResponse("hw4"),
                        new GitHubBranchResponse("hw5")).toArray(GitHubBranchResponse[]::new);
                GitHubCommitResponse[] uniqueCommitResponses = Stream.of(
                                new GitHubCommitResponse("asdfg", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))),
                                new GitHubCommitResponse("zxcvb", new GitHubNestedCommitResponse(new GitHubCommiterResponse(lastCheckUpdateDate))))
                        .toArray(GitHubCommitResponse[]::new);
                GitHubResponse expectedUniqueResponse = new GitHubResponse(new GitHubInfoResponse(lastActivityDateForLoadedForFirstTest),
                        uniqueBranchResponses, uniqueCommitResponses);
                ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse> expectedResultOfCompare =
                        new ResultOfCompareWebsiteInfo<>(true, expectedUniqueGitHubInfo, expectedUniqueResponse);

                StringBuilder stringBuilderForDescription = new StringBuilder();
                stringBuilderForDescription.append("Ссылка " + expectedLinkInfo.getPath() + " получила обновление:\n");
                for(GitHubBranch branch : expectedResultOfCompare.uniqueSavedData.getBranches()){
                    stringBuilderForDescription.append("Ветка " + branch.getBranchName() + " удалена\n");
                }
                for(GitHubCommit commit : expectedResultOfCompare.uniqueSavedData.getCommits()){
                    stringBuilderForDescription.append("Коммит " + commit.getSha() + " удален\n");
                }
                for(GitHubBranchResponse branch : expectedResultOfCompare.uniqueLoadedData.getBranches()){
                    stringBuilderForDescription.append("Ветка " + branch.getName() + " добавлена\n");
                }
                for(GitHubCommitResponse commit : expectedResultOfCompare.uniqueLoadedData.getCommits()){
                    stringBuilderForDescription.append("Коммит " + commit.getSha() + " добавлен\n");
                }
                String description = stringBuilderForDescription.toString();
                LinkUpdateRequest expectedResultRequest = new LinkUpdateRequest(0, URI.create(expectedLinkInfo.getPath()), description, chatIdsForFirstTest.clone());

                firstArguments = Arguments.of(valueForReturn, argumentGitHubInfo, expectedResultRequest,
                        expectedResultOfCompare, chatIdsForFirstTest.clone());
            }
            return Stream.of(
                    firstArguments);
        }
    }
}
