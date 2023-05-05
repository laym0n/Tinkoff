package dataaccess.jooq;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubBranchEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubCommitEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubBranchPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubCommitPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommiterResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubNestedCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JOOQGitHubDAOTest extends JOOQIntegrationEnvironment {
    @Autowired
    public JOOQGitHubInfoDAO SUT;
    @Test
    @Rollback
    @Transactional
    void validAddTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now();
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
        Optional<GitHubInfo> resultFromSUT = SUT.getById(argumentForSUT.getId());
        assertTrue(resultFromSUT.isPresent(), ()->"Repository don't find GitHubInfo but he exist");
        assertEquals(argumentForSUT, resultFromSUT.get());

    }
    @Test
    @Rollback
    @Transactional
    void applyChangesTest(){
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

        ResultOfCompareGitHubInfo argumentForSUT = new ResultOfCompareGitHubInfo(
            initialInfo.getId(),
                linkInfoForInitialInfo);
        argumentForSUT.setDifferent(true);
        argumentForSUT.setDroppedCommits(
            new GitHubCommit[] {new GitHubCommit("12345"), new GitHubCommit("abcde")}
            );
        argumentForSUT.setPushedCommits(new GitHubCommitResponse[] {
                new GitHubCommitResponse("first", new GitHubNestedCommitResponse(new GitHubCommiterResponse(dateForCommitsAndBranches))),
                new GitHubCommitResponse("second", new GitHubNestedCommitResponse(new GitHubCommiterResponse(dateForCommitsAndBranches)))
            });
        argumentForSUT.setDroppedBranches(
            new GitHubBranch[] {new GitHubBranch("hw1")});
        argumentForSUT.setAddedBranches(
            new GitHubBranchResponse[]{new GitHubBranchResponse("hw3"), new GitHubBranchResponse("hw4")});
        argumentForSUT.setLastActivityDate(
            Optional.of(lastActiveTimeForArgument));

        //Action
        SUT.applyChanges(argumentForSUT);

        //Assert
        Optional<GitHubInfo> resultFromSUT = SUT.getById(initialInfo.getId());
        assertTrue(resultFromSUT.isPresent(), ()->"Repository don't find GitHubInfo but he exist");

        Map<String, GitHubBranch> expectedBranches = Map.of(
                "hw4", new GitHubBranch("hw4"),
                "hw3", new GitHubBranch("hw3"),
                "hw2", new GitHubBranch("hw2")
        );
        assertEquals(expectedBranches, resultFromSUT.get().getBranches());

        Map<String, GitHubCommit> expectedCommits = Map.of(
            "12346", new GitHubCommit("12346"),
            "first", new GitHubCommit("first"),
            "second", new GitHubCommit("second")
        );
        assertEquals(expectedCommits, resultFromSUT.get().getCommits());


        assertEquals(linkInfoForInitialInfo, resultFromSUT.get().getLinkInfo());

        assertEquals(lastActiveTimeForArgument.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                resultFromSUT.get().getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                ()->"Expected result of last active time is "
                    + lastActiveTimeForInitial.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                    + " but result is " + resultFromSUT.get().getLastActiveTime()
                        .toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
    }
    @Test
    @Rollback
    @Transactional
    void loadLinkInfoBuChatIdTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now().minusDays(5);
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        GitHubInfo infoForSave = new GitHubInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        "hw1", new GitHubBranch("hw1"),
                        "hw2", new GitHubBranch("hw2")
                ),
                Map.of(
                        "12345", new GitHubCommit("12345"),
                        "12346", new GitHubCommit("12346")
                ), lastActiveTime);
        SUT.add(infoForSave);

        //Action
        GitHubLinkInfo resultFromSUT = SUT.getLinkInfoById(infoForSave.getId());

        //Assert
        assertEquals(infoForSave.getLinkInfo(), resultFromSUT);
    }
    @Test
    @Rollback
    @Transactional
    void findIdByLinkInfoForSavedInfoTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now().minusDays(5);
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        GitHubInfo infoForSave = new GitHubInfo(linkInfoForArgument, lastActiveTime);
        SUT.add(infoForSave);

        //Action
        Optional<Integer> optionalResultFromSUT =
                SUT.findIdByLinkInfo(linkInfoForArgument);

        //Assert
        assertTrue(optionalResultFromSUT.isPresent(),
                () -> "Method findIdByLinkInfo must return not optional result from SUT for saved info");
        assertEquals(infoForSave.getId(), optionalResultFromSUT.get(),
                ()->"Saved info have id " + infoForSave.getId() +
                " but loaded id is " + optionalResultFromSUT.get());
    }
    @Test
    @Rollback
    @Transactional
    void findIdByLinkInfoForNotSavedInfoTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now().minusDays(5);
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        //Action
        Optional<Integer> optionalResultFromSUT =
                SUT.findIdByLinkInfo(linkInfoForArgument);

        //Assert
        assertTrue(optionalResultFromSUT.isEmpty(),
                () -> "Method findIdByLinkInfo must return optional result from SUT for not saved Info");
    }
}
