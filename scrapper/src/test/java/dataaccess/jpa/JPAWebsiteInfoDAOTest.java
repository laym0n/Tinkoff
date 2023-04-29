package dataaccess.jpa;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.WebsiteInfoEntity;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JPAWebsiteInfoDAOTest extends JPAIntegrationEnvironment {
    @Autowired
    private JPAWebsiteInfoDAO SUT;
    @Autowired
    private JPAChainWebsiteInfoDAO chainWebsiteInfoDAO;
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderValidLoadTheEarliestWebsiteInfos.class)
    @Transactional
    @Rollback
    public void validLoadTheEarliestWebsiteInfosTest(List<WebsiteInfo> websiteInfosForLoad, List<WebsiteInfoEntity> expectedInfos){
        //Assign
        for(int i = 0;i < websiteInfosForLoad.size();i++){
            chainWebsiteInfoDAO.create(websiteInfosForLoad.get(i));
            if(i < expectedInfos.size())
                expectedInfos.get(i).setId(websiteInfosForLoad.get(i).getId());
        }

        //Action

        List<WebsiteInfoEntity> resultFromSUT = SUT.loadWebsiteInfoWithTheEarliestUpdateTime(expectedInfos.size());

        //Assert
        assertEquals(expectedInfos.size(), resultFromSUT.size(), ()-> "Expected and loaded size of list must be equal");
        for(int i = 0;i < expectedInfos.size();i++){
            assertEquals(expectedInfos.get(i), resultFromSUT.get(i));
        }
        assertEquals(expectedInfos, resultFromSUT,
                ()->"Expected: " + expectedInfos +
                "\nLoaded: " + resultFromSUT);
    }
    static class ArgumentsProviderValidLoadTheEarliestWebsiteInfos implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            Arguments firstArguments;
            {
                final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now();
                final OffsetDateTime lastActiveTimeForGitHubInfo = OffsetDateTime.now();

                Map<String, GitHubCommit> commitsForFirst = Map.of(
                        "first", new GitHubCommit("first"),
                        "second", new GitHubCommit("second")
                );
                Map<String, GitHubBranch> branchesForFirst = Map.of(
                        "hw1", new GitHubBranch("hw1"),
                        "hw2", new GitHubBranch("hw2")
                );
                GitHubLinkInfo firstLinkInfo = new GitHubLinkInfo("first", "first");
                GitHubInfo firstWebsiteInfo = new GitHubInfo(0, OffsetDateTime.now(), firstLinkInfo, branchesForFirst,
                        commitsForFirst, lastActiveTimeForGitHubInfo);

                Map<Integer, StackOverflowComment> commentsForFirst = Map.of(
                        1, new StackOverflowComment(1),
                        2, new StackOverflowComment(2)
                );
                Map<Integer, StackOverflowAnswer> answersForFirst = Map.of(
                        10, new StackOverflowAnswer(10, "test1", lastEditDateOfAnswers),
                        11, new StackOverflowAnswer(11, "test2", lastEditDateOfAnswers)
                );
                StackOverflowLinkInfo secondLinkInfo = new StackOverflowLinkInfo(100);
                StackOverflowInfo secondWebsiteInfo = new StackOverflowInfo(0, OffsetDateTime.now(),
                        secondLinkInfo, commentsForFirst, answersForFirst);

                Map<String, GitHubCommit> commitsForThird = Map.of(
                        "third", new GitHubCommit("third"),
                        "fourth", new GitHubCommit("fourth")
                );
                Map<String, GitHubBranch> branchesForThird = Map.of(
                        "hw3", new GitHubBranch("hw3"),
                        "hw4", new GitHubBranch("hw4")
                );
                GitHubLinkInfo thirdLinkInfo = new GitHubLinkInfo("third", "third");
                GitHubInfo thirdWebsiteInfo = new GitHubInfo(0, OffsetDateTime.now(), thirdLinkInfo, branchesForThird,
                        commitsForThird, lastActiveTimeForGitHubInfo);
                List<WebsiteInfo> websiteInfosForLoad = List.of(
                        firstWebsiteInfo, secondWebsiteInfo, thirdWebsiteInfo
                );


                Map<String, GitHubCommit> commitsForFirstExpected = Map.of(
                        "first", new GitHubCommit("first"),
                        "second", new GitHubCommit("second")
                );
                Map<String, GitHubBranch> branchesForFirstExpected = Map.of(
                        "hw1", new GitHubBranch("hw1"),
                        "hw2", new GitHubBranch("hw2")
                );
                GitHubLinkInfo firstLinkInfoExpected = new GitHubLinkInfo("first", "first");
                GitHubInfo firstWebsiteInfoExpected = new GitHubInfo(0, OffsetDateTime.now(), firstLinkInfoExpected,
                        branchesForFirstExpected,
                        commitsForFirstExpected, lastActiveTimeForGitHubInfo);
                GitHubInfoEntity firstExpectedWebsiteEntity = new GitHubInfoEntity(firstWebsiteInfoExpected);

                Map<Integer, StackOverflowComment> commentsForFirstExpected = Map.of(
                        1, new StackOverflowComment(1),
                        2, new StackOverflowComment(2)
                );
                Map<Integer, StackOverflowAnswer> answersForFirstExpected = Map.of(
                        10, new StackOverflowAnswer(10, "test1", lastEditDateOfAnswers),
                        11, new StackOverflowAnswer(11, "test2", lastEditDateOfAnswers)
                );
                StackOverflowLinkInfo secondLinkInfoExpected = new StackOverflowLinkInfo(100);
                StackOverflowInfo secondWebsiteInfoExpected = new StackOverflowInfo(0, OffsetDateTime.now(),
                        secondLinkInfoExpected, commentsForFirstExpected, answersForFirstExpected);
                StackOverflowInfoEntity secondExpectedWebsiteEntity = new StackOverflowInfoEntity(secondWebsiteInfoExpected);

                List<WebsiteInfoEntity> expectedWebsiteInfos = List.of(firstExpectedWebsiteEntity, secondExpectedWebsiteEntity);

                firstArguments = Arguments.of(websiteInfosForLoad, expectedWebsiteInfos);
            }
            Arguments secondArguments;
            {
                final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now();
                final OffsetDateTime lastActiveTimeForGitHubInfo = OffsetDateTime.now();

                Map<String, GitHubCommit> commitsForFirst = Map.of(
                        "1", new GitHubCommit("1"),
                        "2", new GitHubCommit("2")
                );
                Map<String, GitHubBranch> branchesForFirst = Map.of(
                        "1", new GitHubBranch("1"),
                        "2", new GitHubBranch("2")
                );
                GitHubLinkInfo firstLinkInfo = new GitHubLinkInfo("1", "1");
                GitHubInfo firstWebsiteInfo = new GitHubInfo(0, OffsetDateTime.now(), firstLinkInfo, branchesForFirst,
                        commitsForFirst, lastActiveTimeForGitHubInfo);

                Map<Integer, StackOverflowComment> commentsForFirst = Map.of(
                        1, new StackOverflowComment(1),
                        2, new StackOverflowComment(2)
                );
                Map<Integer, StackOverflowAnswer> answersForFirst = Map.of(
                        1, new StackOverflowAnswer(1, "1", lastEditDateOfAnswers),
                        2, new StackOverflowAnswer(2, "2", lastEditDateOfAnswers)
                );
                StackOverflowLinkInfo secondLinkInfo = new StackOverflowLinkInfo(1);
                StackOverflowInfo secondWebsiteInfo = new StackOverflowInfo(0, OffsetDateTime.now(),
                        secondLinkInfo, commentsForFirst, answersForFirst);

                Map<String, GitHubCommit> commitsForThird = Map.of(
                        "3", new GitHubCommit("3"),
                        "4", new GitHubCommit("4")
                );
                Map<String, GitHubBranch> branchesForThird = Map.of(
                        "3", new GitHubBranch("3"),
                        "4", new GitHubBranch("4")
                );
                GitHubLinkInfo thirdLinkInfo = new GitHubLinkInfo("2", "2");
                GitHubInfo thirdWebsiteInfo = new GitHubInfo(0, OffsetDateTime.now(), thirdLinkInfo, branchesForThird,
                        commitsForThird, lastActiveTimeForGitHubInfo);
                List<WebsiteInfo> websiteInfosForLoad = List.of(
                        firstWebsiteInfo, secondWebsiteInfo, thirdWebsiteInfo
                );


                Map<String, GitHubCommit> commitsForFirstExpected = Map.of(
                        "1", new GitHubCommit("1"),
                        "2", new GitHubCommit("2")
                );
                Map<String, GitHubBranch> branchesForFirstExpected = Map.of(
                        "1", new GitHubBranch("1"),
                        "2", new GitHubBranch("2")
                );
                GitHubLinkInfo firstLinkInfoExpected = new GitHubLinkInfo("1", "1");
                GitHubInfo firstWebsiteInfoExpected = new GitHubInfo(0, OffsetDateTime.now(), firstLinkInfoExpected,
                        branchesForFirstExpected,
                        commitsForFirstExpected, lastActiveTimeForGitHubInfo);
                GitHubInfoEntity firstExpectedWebsiteEntity = new GitHubInfoEntity(firstWebsiteInfoExpected);

                Map<Integer, StackOverflowComment> commentsForFirstExpected = Map.of(
                        1, new StackOverflowComment(1),
                        2, new StackOverflowComment(2)
                );
                Map<Integer, StackOverflowAnswer> answersForFirstExpected = Map.of(
                        1, new StackOverflowAnswer(1, "1", lastEditDateOfAnswers),
                        2, new StackOverflowAnswer(2, "2", lastEditDateOfAnswers)
                );
                StackOverflowLinkInfo secondLinkInfoExpected = new StackOverflowLinkInfo(1);
                StackOverflowInfo secondWebsiteInfoExpected = new StackOverflowInfo(0, OffsetDateTime.now(),
                        secondLinkInfoExpected, commentsForFirstExpected, answersForFirstExpected);
                StackOverflowInfoEntity secondExpectedWebsiteEntity = new StackOverflowInfoEntity(secondWebsiteInfoExpected);

                Map<String, GitHubCommit> expectedCommitsForThird = Map.of(
                        "3", new GitHubCommit("3"),
                        "4", new GitHubCommit("4")
                );
                Map<String, GitHubBranch> expectedBranchesForThird = Map.of(
                        "3", new GitHubBranch("3"),
                        "4", new GitHubBranch("4")
                );
                GitHubLinkInfo expectedThirdLinkInfo = new GitHubLinkInfo("2", "2");
                GitHubInfo expectedThirdWebsiteInfo = new GitHubInfo(0, OffsetDateTime.now(), expectedThirdLinkInfo,
                        expectedBranchesForThird,
                        expectedCommitsForThird, lastActiveTimeForGitHubInfo);
                GitHubInfoEntity thirdExpectedWebsiteEntity = new GitHubInfoEntity(expectedThirdWebsiteInfo);
                List<WebsiteInfoEntity> expectedWebsiteInfos = List.of(firstExpectedWebsiteEntity, secondExpectedWebsiteEntity,
                        thirdExpectedWebsiteEntity);

                secondArguments = Arguments.of(websiteInfosForLoad, expectedWebsiteInfos);
            }
            return Stream.of(
                    firstArguments,
                    secondArguments
            );
        }
    }
}
