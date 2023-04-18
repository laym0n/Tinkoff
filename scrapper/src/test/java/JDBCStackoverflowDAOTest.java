import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.*;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import static org.junit.jupiter.api.Assertions.*;
import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class JDBCStackoverflowDAOTest extends IntegrationEnvironment{
    @Test
    @Transactional
    @Rollback
    public void validAddTest(){
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

        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        JDBCStackOverflowAnswerDAO answerDAO = new JDBCStackOverflowAnswerDAO(dataSource);
        JDBCStackOverflowCommentDAO commentDAO = new JDBCStackOverflowCommentDAO(dataSource);

        JDBCStackOverflowInfoDAO SUT = new JDBCStackOverflowInfoDAO(dataSource, answerDAO, commentDAO);

        //Action
        SUT.add(argumentForSUT);

        //Assert
        StackOverflowInfo loadedInfo = SUT.getById(argumentForSUT.getId());

        StackOverflowLinkInfo expectedLinkInfo = new StackOverflowLinkInfo(1412);
        assertEquals(expectedLinkInfo, loadedInfo.getLinkInfo(),
                () -> "Link info not equals");
        Map<Integer, StackOverflowComment> expectedComments = Map.of(
                145, new StackOverflowComment(145),
                1452135, new StackOverflowComment(1452135)
        );
        assertEquals(expectedComments, loadedInfo.getComments(),
                ()->"Comments not equal");

        Map<Integer, StackOverflowAnswer> expectedAnswers = Map.of(
                51, new StackOverflowAnswer(51, "test1", lastEditDateOfAnswers),
                5511, new StackOverflowAnswer(5511, "test2", lastEditDateOfAnswers)
        );
        assertTrue(expectedAnswers.values().stream().allMatch(expectedAnswer ->{
            boolean result = true;
            if(!loadedInfo.getAnswers().containsKey(expectedAnswer.getIdAnswer())){
                result = false;
            }
            else{
                StackOverflowAnswer loadedAnswer = loadedInfo.getAnswers().get(expectedAnswer.getIdAnswer());
                result = loadedAnswer.getUserName().equals(expectedAnswer.getUserName()) &&
                        loadedAnswer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                                .equals(expectedAnswer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
            }
            return result;
        }) && expectedAnswers.size() == loadedInfo.getAnswers().size(),
                ()->"Expected answers: " + expectedAnswers +
                "\n loaded answers: " + loadedInfo.getAnswers());
    }
}
