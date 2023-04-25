package jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommiterResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubNestedCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JPAGitHubDAOTest extends JPAIntegrationEnvironment {
    @Autowired
    public JDBCGitHubInfoDAO SUT;
    @Test
    @Rollback
    @Transactional
    public void validAddTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now().minusDays(5);
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        GitHubInfo argumentForSUT = new GitHubInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        "hw1", new GitHubBranch("hw1"),
                        "hw2", new GitHubBranch("hw2")
                ),
                Map.of(
                        "12345", new GitHubCommit("12345"),
                        "12346", new GitHubCommit("12346")
        ), lastActiveTime);

        //Action
        SUT.add(argumentForSUT);

        //Assert
        GitHubInfo resultOfLoad = SUT.getById(argumentForSUT.getId());

        Map<String, GitHubBranch> expectedBranches = Map.of(
                "hw1", new GitHubBranch("hw1"),
                "hw2", new GitHubBranch("hw2")
        );
        assertEquals(expectedBranches, resultOfLoad.getBranches(), ()->"Expected branches is" + expectedBranches +
                " but result is " + resultOfLoad.getBranches());

        Map<String, GitHubCommit> expectedCommits = Map.of(
                "12345", new GitHubCommit("12345"),
                "12346", new GitHubCommit("12346")
        );
        assertEquals(expectedCommits, resultOfLoad.getCommits(), ()->"Expected commits is" + expectedCommits +
                " but result is " + resultOfLoad.getCommits());

        GitHubLinkInfo expectedLinkInfo = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");
        assertEquals(expectedLinkInfo, resultOfLoad.getLinkInfo(),
                ()->"Expected link info is " + expectedLinkInfo + " but result is " + resultOfLoad.getLinkInfo());

        assertEquals(lastActiveTime.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                resultOfLoad.getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                ()->"Expected result of last active time is " + lastActiveTime +
                " but result is " + resultOfLoad.getLastActiveTime());

    }
    @Test
    @Rollback
    @Transactional
    public void applyChangesTest(){
        //Assign
        final OffsetDateTime lastActiveTimeForInitial = OffsetDateTime.now().minusDays(5);
        final OffsetDateTime dateForCommitsAndBranches = OffsetDateTime.now();
        final OffsetDateTime lastActiveTimeForArgument = OffsetDateTime.now().plusDays(5);
        GitHubLinkInfo linkInfoForInitialInfo = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        GitHubInfo initialInfo = new GitHubInfo(0, OffsetDateTime.now(), linkInfoForInitialInfo,
                Map.of(
                        "hw1", new GitHubBranch("hw1"),
                        "hw2", new GitHubBranch("hw2")
                ),
                Map.of(
                        "12345", new GitHubCommit("12345"),
                        "12346", new GitHubCommit("12346"),
                        "abcde", new GitHubCommit("abcde")
                ), lastActiveTimeForInitial);

        SUT.add(initialInfo);

        ResultOfCompareGitHubInfo argumentForSUT = new ResultOfCompareGitHubInfo(true,
                linkInfoForInitialInfo, initialInfo.getId(),
                new GitHubCommit[] {new GitHubCommit("12345"), new GitHubCommit("abcde")},
                new GitHubCommitResponse[] {
                        new GitHubCommitResponse("first", new GitHubNestedCommitResponse(new GitHubCommiterResponse(dateForCommitsAndBranches))),
                        new GitHubCommitResponse("second", new GitHubNestedCommitResponse(new GitHubCommiterResponse(dateForCommitsAndBranches)))
        },
                new GitHubBranch[] {new GitHubBranch("hw1")},
                new GitHubBranchResponse[]{new GitHubBranchResponse("hw3"), new GitHubBranchResponse("hw4")},
                Optional.of(lastActiveTimeForArgument));

        //Action
        SUT.applyChanges(argumentForSUT);

        //Assert
        GitHubInfo resultOfLoad = SUT.getById(initialInfo.getId());

        Map<String, GitHubBranch> expectedBranches = Map.of(
                "hw4", new GitHubBranch("hw4"),
                "hw2", new GitHubBranch("hw2"),
                "hw3", new GitHubBranch("hw3")
        );
        assertEquals(expectedBranches, resultOfLoad.getBranches(), ()->"Expected branches is" + expectedBranches +
                " but result is " + resultOfLoad.getBranches());

        Map<String, GitHubCommit> expectedCommits = Map.of(
                "12346", new GitHubCommit("12346"),
                "first", new GitHubCommit("first"),
                "second", new GitHubCommit("second")
        );
        assertEquals(expectedCommits, resultOfLoad.getCommits(), ()->"Expected commits is" + expectedCommits +
                " but result is " + resultOfLoad.getCommits());

        GitHubLinkInfo expectedLinkInfo = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");
        assertEquals(expectedLinkInfo, resultOfLoad.getLinkInfo(),
                ()->"Expected link info is " + expectedLinkInfo + " but result is " + resultOfLoad.getLinkInfo());

        assertEquals(lastActiveTimeForArgument.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                resultOfLoad.getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                ()->"Expected result of last active time is " + lastActiveTimeForInitial.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS) +
                        " but result is " + resultOfLoad.getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
    }
    @Test
    @Rollback
    @Transactional
    public void loadLinkInfoBuChatIdTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now().minusDays(5);
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        GitHubInfo argumentForSUT = new GitHubInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        "hw1", new GitHubBranch("hw1"),
                        "hw2", new GitHubBranch("hw2")
                ),
                Map.of(
                        "12345", new GitHubCommit("12345"),
                        "12346", new GitHubCommit("12346")
                ), lastActiveTime);

        SUT.add(argumentForSUT);

        //Action
        GitHubLinkInfo resultFromSUT = SUT.getLinkInfoById(argumentForSUT.getId());

        //Assert
        assertEquals(argumentForSUT.getLinkInfo(), resultFromSUT);
    }
    @Test
    @Rollback
    @Transactional
    public void findIdByLinkInfoForSavedInfoTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now().minusDays(5);
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        GitHubInfo argumentForSUT = new GitHubInfo(linkInfoForArgument, lastActiveTime);

        SUT.add(argumentForSUT);

        //Action
        Optional<Integer> optionalResultFromSUT = SUT.findIdByLinkInfo(linkInfoForArgument);

        //Assert
        assertTrue(optionalResultFromSUT.isPresent(),
                () -> "Method findIdByLinkInfo must return not optional result from SUT for saved info");
        assertEquals(argumentForSUT.getId(), optionalResultFromSUT.get(),
                ()->"Saved info have id " + argumentForSUT.getId() +
                " but loaded id is " + optionalResultFromSUT.get());
    }
    @Test
    @Rollback
    @Transactional
    public void findIdByLinkInfoForNotSavedInfoTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now().minusDays(5);
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        //Action
        Optional<Integer> optionalResultFromSUT = SUT.findIdByLinkInfo(linkInfoForArgument);

        //Assert
        assertTrue(optionalResultFromSUT.isEmpty(),
                () -> "Method findIdByLinkInfo must return optional result from SUT for not saved Info");
    }
}
