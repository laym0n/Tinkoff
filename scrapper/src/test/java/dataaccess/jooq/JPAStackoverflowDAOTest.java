package dataaccess.jooq;

import java.sql.Timestamp;
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
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowAnswerEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowCommentEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowAnswerPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowCommentPrimaryKey;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowUserResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JPAStackoverflowDAOTest extends JPAIntegrationEnvironment {
    @Autowired
    public JPAStackOverflowInfoDAO SUT;
    @Test
    @Rollback
    @Transactional
    void validAddTest(){
        //Assign
        final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now().minusDays(5);
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        StackOverflowInfo infoForArgument = new StackOverflowInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        145, new StackOverflowComment(145),
                        1452135, new StackOverflowComment(1452135)
                ),
                Map.of(
                        51, new StackOverflowAnswer(51, "test1", lastEditDateOfAnswers),
                        5511, new StackOverflowAnswer(5511, "test2", lastEditDateOfAnswers)
        ));
        StackOverflowInfoEntity argumentForSUT = new StackOverflowInfoEntity(infoForArgument);

        //Action
        SUT.add(argumentForSUT);

        //Assert
        StackOverflowInfoEntity resultFromSUT = SUT.getById(argumentForSUT.getId());

        assertEquals(linkInfoForArgument.idQuestion(), resultFromSUT.getQuestionId(),
                () -> "Question ids is not equal");

        Set<StackOverflowCommentEntity> expectedComments = Set.of(
                new StackOverflowCommentEntity(new StackOverflowCommentPrimaryKey(145, argumentForSUT.getId())),
                new StackOverflowCommentEntity(new StackOverflowCommentPrimaryKey(1452135, argumentForSUT.getId()))
        );
        assertEquals(expectedComments, resultFromSUT.getComments().stream().collect(Collectors.toSet()),
                ()->"Comments not equal");

        Map<Integer, StackOverflowAnswerEntity> expectedAnswers = Map.of(
                51, new StackOverflowAnswerEntity(
                        new StackOverflowAnswerPrimaryKey(51, argumentForSUT.getId()),
                        "test1",
                        Timestamp.valueOf(lastEditDateOfAnswers.toLocalDateTime())
                ),
                5511, new StackOverflowAnswerEntity(
                        new StackOverflowAnswerPrimaryKey(5511, argumentForSUT.getId()),
                        "test2",
                        Timestamp.valueOf(lastEditDateOfAnswers.toLocalDateTime())
                )
        );
        assertTrue(resultFromSUT.getAnswers().stream().allMatch(loadedAnswer ->{
            boolean result = true;
            if(!expectedAnswers.containsKey(loadedAnswer.getPrimaryKey().getId())){
                result = false;
            }
            else{
                StackOverflowAnswerEntity expectedAnswer = expectedAnswers.get(loadedAnswer.getPrimaryKey().getId());
                result = loadedAnswer.getUserName().equals(expectedAnswer.getUserName()) &&
                        loadedAnswer.getLastEditDateTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                                .equals(expectedAnswer.getLastEditDateTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)) &&
                        loadedAnswer.getPrimaryKey().getWebsiteInfoId() == expectedAnswer.getPrimaryKey().getWebsiteInfoId();
            }
            return result;
        }) && expectedAnswers.size() == resultFromSUT.getAnswers().size(),
                ()->"Expected and loaded answers not equal");
    }
    @Test
    @Rollback
    @Transactional
    void applyChangesTest(){
        //Assign
        final OffsetDateTime lastEditDateOfAnswersForInitial = OffsetDateTime.now().minusDays(5);
        final OffsetDateTime lastEditDateOfAnswersForArgument = OffsetDateTime.now().plusDays(5);
        StackOverflowLinkInfo linkInfoForInitial = new StackOverflowLinkInfo(1412);

        StackOverflowInfo initialInfo = new StackOverflowInfo(0, OffsetDateTime.now(), linkInfoForInitial,
                Map.of(
                        1, new StackOverflowComment(1),
                        2, new StackOverflowComment(2),
                        3, new StackOverflowComment(3)
                ),
                Map.of(
                        1, new StackOverflowAnswer(1, "test1", lastEditDateOfAnswersForInitial),
                        2, new StackOverflowAnswer(2, "test2", lastEditDateOfAnswersForInitial),
                        3, new StackOverflowAnswer(3, "test3", lastEditDateOfAnswersForInitial),
                        4, new StackOverflowAnswer(4, "test4", lastEditDateOfAnswersForInitial),
                        5, new StackOverflowAnswer(5, "test5", lastEditDateOfAnswersForInitial)
                ));
        StackOverflowInfoEntity initialEntity = new StackOverflowInfoEntity(initialInfo);
        SUT.add(initialEntity);

        ResultOfCompareStackOverflowInfo argumentForSUT = new ResultOfCompareStackOverflowInfo(
            initialEntity.getId(),
                linkInfoForInitial);
        argumentForSUT.setDifferent(true);
        argumentForSUT.setDeletedAnswers(
            new StackOverflowAnswer[]{
                new StackOverflowAnswer(1, "test1", lastEditDateOfAnswersForInitial),
                new StackOverflowAnswer(2, "test2", lastEditDateOfAnswersForInitial)
            });
        argumentForSUT.setAddedAnswers(
            new StackOverflowAnswerResponse[]{
                new StackOverflowAnswerResponse(new StackOverflowUserResponse("test6"), lastEditDateOfAnswersForArgument,
                    lastEditDateOfAnswersForArgument, 6),
                new StackOverflowAnswerResponse(new StackOverflowUserResponse("test7"), lastEditDateOfAnswersForArgument,
                    lastEditDateOfAnswersForArgument, 7)
            });
        argumentForSUT.setEditedAnswers(
            new StackOverflowAnswerResponse[]{
                new StackOverflowAnswerResponse(new StackOverflowUserResponse("test4"), lastEditDateOfAnswersForArgument,
                    lastEditDateOfAnswersForArgument, 4),
                new StackOverflowAnswerResponse(new StackOverflowUserResponse("test5"), lastEditDateOfAnswersForArgument,
                    lastEditDateOfAnswersForArgument, 5)
            });
        argumentForSUT.setDeletedComments(
            new StackOverflowComment[]{
                new StackOverflowComment(1),
                new StackOverflowComment(3)
            });
        argumentForSUT.setAddedComments(
            new StackOverflowCommentResponse[]{
                new StackOverflowCommentResponse(new StackOverflowUserResponse("test8"), lastEditDateOfAnswersForArgument,
                    4),
                new StackOverflowCommentResponse(new StackOverflowUserResponse("test9"), lastEditDateOfAnswersForArgument,
                    5)
            });

        //Action
        SUT.applyChanges(argumentForSUT);

        //Assert
        StackOverflowInfoEntity resultFromSUT = SUT.getById(initialEntity.getId());

        assertEquals(linkInfoForInitial.idQuestion(), resultFromSUT.getQuestionId(),
                () -> "Loaded and expected question id is not equal ");
        Set<StackOverflowCommentEntity> expectedComments = Set.of(
                new StackOverflowCommentEntity(new StackOverflowCommentPrimaryKey(2, initialEntity.getId())),
                new StackOverflowCommentEntity(new StackOverflowCommentPrimaryKey(4, initialEntity.getId())),
                new StackOverflowCommentEntity(new StackOverflowCommentPrimaryKey(5, initialEntity.getId()))
        );
        assertEquals(expectedComments, resultFromSUT.getComments().stream().collect(Collectors.toSet()),
                ()->"Comments not equal");

        Map<Integer, StackOverflowAnswerEntity> expectedAnswers = Map.of(
                3, new StackOverflowAnswerEntity(
                        new StackOverflowAnswerPrimaryKey(3, initialEntity.getId()),
                        "test3",
                        Timestamp.valueOf(lastEditDateOfAnswersForInitial.toLocalDateTime())
                ),
                4,  new StackOverflowAnswerEntity(
                        new StackOverflowAnswerPrimaryKey(4, initialEntity.getId()),
                        "test4",
                        Timestamp.valueOf(lastEditDateOfAnswersForArgument.toLocalDateTime())
                ),
                5, new StackOverflowAnswerEntity(
                        new StackOverflowAnswerPrimaryKey(5, initialEntity.getId()),
                        "test5",
                        Timestamp.valueOf(lastEditDateOfAnswersForArgument.toLocalDateTime())
                ),
                6, new StackOverflowAnswerEntity(
                        new StackOverflowAnswerPrimaryKey(6, initialEntity.getId()),
                        "test6",
                        Timestamp.valueOf(lastEditDateOfAnswersForArgument.toLocalDateTime())
                ),
                7, new StackOverflowAnswerEntity(
                        new StackOverflowAnswerPrimaryKey(7, initialEntity.getId()),
                        "test7",
                        Timestamp.valueOf(lastEditDateOfAnswersForArgument.toLocalDateTime())
                )
        );
        assertTrue(resultFromSUT.getAnswers().stream().allMatch(loadedAnswer ->{
                    boolean result = true;
                    if(!expectedAnswers.containsKey(loadedAnswer.getPrimaryKey().getId())){
                        result = false;
                    }
                    else{
                        StackOverflowAnswerEntity expectedAnswer = expectedAnswers.get(loadedAnswer.getPrimaryKey().getId());
                        result = loadedAnswer.getUserName().equals(expectedAnswer.getUserName()) &&
                                loadedAnswer.getLastEditDateTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                                        .equals(expectedAnswer.getLastEditDateTime().toLocalDateTime()
                                                .truncatedTo(ChronoUnit.SECONDS)) &&
                        loadedAnswer.getPrimaryKey().getWebsiteInfoId() == expectedAnswer.getPrimaryKey().getWebsiteInfoId();
                    }
                    return result;
                }) && expectedAnswers.size() == resultFromSUT.getAnswers().size(),
                ()->"Loaded and expected answers is not equal\nExpected answers:\n" + expectedAnswers.values() +
                "\nLoaded answers:\n" + resultFromSUT.getAnswers());
    }
    @Test
    @Rollback
    @Transactional
    void loadLinkInfoTest(){
        //Assign
        final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now().minusDays(5);
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        StackOverflowInfo infoForArgument = new StackOverflowInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        145, new StackOverflowComment(145),
                        1452135, new StackOverflowComment(1452135)
                ),
                Map.of(
                        51, new StackOverflowAnswer(51, "test1", lastEditDateOfAnswers),
                        5511, new StackOverflowAnswer(5511, "test2", lastEditDateOfAnswers)
                ));
        StackOverflowInfoEntity argumentForSUT = new StackOverflowInfoEntity(infoForArgument);
        SUT.add(argumentForSUT);

        //Action
        StackOverflowLinkInfo resultFromSUT = SUT.loadLinkInfo(argumentForSUT.getId());

        //Assert
        assertEquals(linkInfoForArgument, resultFromSUT);
    }
    @Test
    @Rollback
    @Transactional
    void findIdWithLinkInfoForSavedTest(){
        //Assign
        final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now().minusDays(5);
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        StackOverflowInfo infoForArgument = new StackOverflowInfo(linkInfoForArgument);
        StackOverflowInfoEntity argumentForSUT = new StackOverflowInfoEntity(infoForArgument);
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
    void findIdWithLinkInfoForNotSavedTest(){
        //Assign
        final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now().minusDays(5);
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        //Action
        Optional<Integer> optionalResultFromSUT = SUT.findIdByLinkInfo(linkInfoForArgument);

        //Assert
        assertTrue(optionalResultFromSUT.isEmpty(),
                () -> "Method findIdByLinkInfo must return optional result from SUT for not saved info");
    }
}
