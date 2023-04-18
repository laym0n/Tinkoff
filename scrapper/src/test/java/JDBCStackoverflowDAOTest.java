import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.*;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
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

    }
}
