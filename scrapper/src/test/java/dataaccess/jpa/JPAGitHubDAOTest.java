package dataaccess.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCGitHubInfoDAO;
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

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JPAGitHubDAOTest extends JPAIntegrationEnvironment {
    @Autowired
    public JPAGitHubInfoDAO SUT;
    @Test
    @Rollback
    @Transactional
    void validAddTest(){
        //Assign
        final OffsetDateTime lastActiveTime = OffsetDateTime.now();
        GitHubLinkInfo linkInfoForArgument = new GitHubLinkInfo("sanyarnd", "tinkoff-java-course-2022");

        GitHubInfo gitHubInfo = new GitHubInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        "hw1", new GitHubBranch("hw1"),
                        "hw2", new GitHubBranch("hw2")
                ),
                Map.of(
                        "12345", new GitHubCommit("12345"),
                        "12346", new GitHubCommit("12346")
        ), lastActiveTime);
        GitHubInfoEntity argumentForSUT = new GitHubInfoEntity(gitHubInfo);

        //Action
        SUT.add(argumentForSUT);
        argumentForSUT.setId(argumentForSUT.getId());

        //Assert
        GitHubInfoEntity resultFromSUT = SUT.getById(argumentForSUT.getId());
        assertEquals(argumentForSUT, resultFromSUT);

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

        GitHubInfoEntity initialInfoEntity = new GitHubInfoEntity(initialInfo);
        SUT.add(initialInfoEntity);

        ResultOfCompareGitHubInfo argumentForSUT = new ResultOfCompareGitHubInfo(
            initialInfoEntity.getId(),
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
        GitHubInfoEntity resultFromSUT = SUT.getById(initialInfoEntity.getId());

        Set<GitHubBranchEntity> expectedBranches = Set.of(
                new GitHubBranchEntity(new GitHubBranchPrimaryKey("hw4", initialInfoEntity.getId())),
                new GitHubBranchEntity(new GitHubBranchPrimaryKey("hw3", initialInfoEntity.getId())),
                new GitHubBranchEntity(new GitHubBranchPrimaryKey("hw2", initialInfoEntity.getId()))
        );
        assertEquals(expectedBranches, resultFromSUT.getBranches().stream().collect(Collectors.toSet()));

        Set<GitHubCommitEntity> expectedCommits = Set.of(
                new GitHubCommitEntity(new GitHubCommitPrimaryKey("12346", initialInfoEntity.getId())),
                new GitHubCommitEntity(new GitHubCommitPrimaryKey("first", initialInfoEntity.getId())),
                new GitHubCommitEntity(new GitHubCommitPrimaryKey("second", initialInfoEntity.getId()))
        );
        assertEquals(expectedCommits, resultFromSUT.getCommits().stream().collect(Collectors.toSet()));


        assertEquals(linkInfoForInitialInfo.userName(), resultFromSUT.getUserName(),
                ()->"Expected user name is " + linkInfoForInitialInfo.userName() +
                        " but loaded result is " + resultFromSUT.getUserName());

        assertEquals(linkInfoForInitialInfo.repositoryName(), resultFromSUT.getRepositoryName(),
                ()->"Expected repository name is " + linkInfoForInitialInfo.repositoryName() +
                        " but loaded result is " + resultFromSUT.getRepositoryName());

        assertEquals(lastActiveTimeForArgument.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                resultFromSUT.getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
                ()->"Expected result of last active time is " + lastActiveTimeForInitial.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS) +
                        " but result is " + resultFromSUT.getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
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
        GitHubInfoEntity entityForSave = new GitHubInfoEntity(infoForSave);
        SUT.add(entityForSave);

        //Action
        GitHubLinkInfo resultFromSUT = SUT.getLinkInfoById(entityForSave.getId());

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
        GitHubInfoEntity entityForSave = new GitHubInfoEntity(infoForSave);
        SUT.add(entityForSave);

        //Action
        Optional<Integer> optionalResultFromSUT =
                SUT.findIdByUserNameAndRepositoryName(linkInfoForArgument.userName(), linkInfoForArgument.repositoryName());

        //Assert
        assertTrue(optionalResultFromSUT.isPresent(),
                () -> "Method findIdByLinkInfo must return not optional result from SUT for saved info");
        assertEquals(entityForSave.getId(), optionalResultFromSUT.get(),
                ()->"Saved info have id " + entityForSave.getId() +
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
                SUT.findIdByUserNameAndRepositoryName(linkInfoForArgument.userName(), linkInfoForArgument.repositoryName());

        //Assert
        assertTrue(optionalResultFromSUT.isEmpty(),
                () -> "Method findIdByLinkInfo must return optional result from SUT for not saved Info");
    }
}
