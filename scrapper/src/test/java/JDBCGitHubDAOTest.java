import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCGitHubBranchesDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCGitHubCommitDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.Map;

public class JDBCGitHubDAOTest extends IntegrationEnvironment{
    @Test
    @Transactional
    @Rollback
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

        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        JDBCGitHubCommitDAO commitDAO = new JDBCGitHubCommitDAO(dataSource);
        JDBCGitHubBranchesDAO branchesDAO = new JDBCGitHubBranchesDAO(dataSource);

        JDBCGitHubInfoDAO SUT = new JDBCGitHubInfoDAO(dataSource, commitDAO, branchesDAO);

        //Action
        SUT.add(argumentForSUT);

        //Assert
//        GitHubInfo resultOfLoad = SUT.getById(argumentForSUT.getId());
    }
}
