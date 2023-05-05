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
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQStackOverflowInfoDAO;
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

class JOOQStackoverflowDAOTest extends JOOQIntegrationEnvironment {
    @Autowired
    public JOOQStackOverflowInfoDAO SUT;
    @Test
    @Rollback
    @Transactional
    void validAddTest(){
        //Assign
        final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now().minusDays(5);
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        StackOverflowInfo argumentForSUT = new StackOverflowInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        145, new StackOverflowComment(145),
                        1452135, new StackOverflowComment(1452135)
                ),
                Map.of(
                        51, new StackOverflowAnswer(51, "test1", lastEditDateOfAnswers),
                        5511, new StackOverflowAnswer(5511, "test2", lastEditDateOfAnswers)
        ));

        //Action
        SUT.add(argumentForSUT);

        //Assert
        Optional<StackOverflowInfo> resultFromSUT = SUT.getById(argumentForSUT.getId());
        assertTrue(resultFromSUT.isPresent(), ()->"Repository don't find StackOverflowInfo but he is exist");

        assertEquals(linkInfoForArgument, resultFromSUT.get().getLinkInfo(),
                () -> "Question ids is not equal");

        Map<Integer, StackOverflowComment> expectedComments = Map.of(
            145, new StackOverflowComment(145),
            1452135, new StackOverflowComment(1452135)
        );
        assertEquals(expectedComments, resultFromSUT.get().getComments(),
                ()->"Comments not equal");

        Map<Integer, StackOverflowAnswer> expectedAnswers = Map.of(
                51, new StackOverflowAnswer(
                        51,
                        "test1",
                        lastEditDateOfAnswers
                ),
                5511, new StackOverflowAnswer(
                        5511,
                        "test2",
                        lastEditDateOfAnswers
                )
        );
        assertTrue(expectedAnswers.values().stream().allMatch(expectedAnswer ->{
                boolean result = true;
                if(!resultFromSUT.get().getAnswers().containsKey(expectedAnswer.getIdAnswer())){
                    result = false;
                }
                else{
                    StackOverflowAnswer loadedAnswer = resultFromSUT.get().getAnswers().get(expectedAnswer.getIdAnswer());
                    result = loadedAnswer.getUserName().equals(expectedAnswer.getUserName()) &&
                        loadedAnswer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                            .equals(expectedAnswer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
                }
                return result;
            }) && expectedAnswers.size() == resultFromSUT.get().getAnswers().size(),
            ()->"Expected answers: " + expectedAnswers +
                "\n loaded answers: " + resultFromSUT.get().getAnswers());
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
        SUT.add(initialInfo);

        ResultOfCompareStackOverflowInfo argumentForSUT = new ResultOfCompareStackOverflowInfo(
            initialInfo.getId(),
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
        Optional<StackOverflowInfo> resultFromSUT = SUT.getById(initialInfo.getId());
        assertTrue(resultFromSUT.isPresent(), ()->"Repository don't find StackOverflowInfo but he is exist");

        assertEquals(linkInfoForInitial, resultFromSUT.get().getLinkInfo());

        Map<Integer, StackOverflowComment> expectedComments = Map.of(
                2, new StackOverflowComment(2),
                4, new StackOverflowComment(4),
                5, new StackOverflowComment(5)
        );
        assertEquals(expectedComments, resultFromSUT.get().getComments(),
                ()->"Comments not equal");

        Map<Integer, StackOverflowAnswer> expectedAnswers = Map.of(
                3, new StackOverflowAnswer(
                        3,
                        "test3",
                        lastEditDateOfAnswersForInitial
                ),
                4,  new StackOverflowAnswer(
                        4,
                        "test4",
                        lastEditDateOfAnswersForArgument
                ),
                5, new StackOverflowAnswer(
                        5,
                        "test5",
                        lastEditDateOfAnswersForArgument
                ),
                6, new StackOverflowAnswer(
                        6,
                        "test6",
                        lastEditDateOfAnswersForArgument
                ),
                7, new StackOverflowAnswer(
                        7,
                        "test7",
                        lastEditDateOfAnswersForArgument
                )
        );
        assertTrue(expectedAnswers.values().stream().allMatch(expectedAnswer ->{
                boolean result = true;
                if(!resultFromSUT.get().getAnswers().containsKey(expectedAnswer.getIdAnswer())){
                    result = false;
                }
                else{
                    StackOverflowAnswer loadedAnswer = resultFromSUT.get().getAnswers().get(expectedAnswer.getIdAnswer());
                    result = loadedAnswer.getUserName().equals(expectedAnswer.getUserName()) &&
                        loadedAnswer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                            .equals(expectedAnswer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
                }
                return result;
            }) && expectedAnswers.size() == resultFromSUT.get().getAnswers().size(),
            ()->"Expected answers: " + expectedAnswers +
                "\n loaded answers: " + resultFromSUT.get().getAnswers());
    }
    @Test
    @Rollback
    @Transactional
    void loadLinkInfoTest(){
        //Assign
        final OffsetDateTime lastEditDateOfAnswers = OffsetDateTime.now().minusDays(5);
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        StackOverflowInfo argumentForSUT = new StackOverflowInfo(0, OffsetDateTime.now(), linkInfoForArgument,
                Map.of(
                        145, new StackOverflowComment(145),
                        1452135, new StackOverflowComment(1452135)
                ),
                Map.of(
                        51, new StackOverflowAnswer(51, "test1", lastEditDateOfAnswers),
                        5511, new StackOverflowAnswer(5511, "test2", lastEditDateOfAnswers)
                ));
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
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        StackOverflowInfo argumentForSUT = new StackOverflowInfo(linkInfoForArgument);
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
        StackOverflowLinkInfo linkInfoForArgument = new StackOverflowLinkInfo(1412);

        //Action
        Optional<Integer> optionalResultFromSUT = SUT.findIdByLinkInfo(linkInfoForArgument);

        //Assert
        assertTrue(optionalResultFromSUT.isEmpty(),
                () -> "Method findIdByLinkInfo must return optional result from SUT for not saved info");
    }
}
