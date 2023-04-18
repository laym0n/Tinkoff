import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCChatDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.*;

public class JDBCChatDAOTest extends IntegrationEnvironment{
    @Test
    @Transactional
    @Rollback
    public void validAddTest(){
        //Assign
        final int idOfChat = 100;
        Chat argumentForSUT = new Chat(idOfChat);

        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        JDBCChatDAO SUT = new JDBCChatDAO(dataSource);

        //Action
        SUT.add(argumentForSUT);

        //Assert
        Chat chatFromDB = SUT.findByID(idOfChat);
        assertEquals(chatFromDB.getId(), idOfChat);
    }
    @Test
    @Transactional
    @Rollback
    public void containsChatTest(){
        //Assign
        final int idOfChat = 100;
        Chat argumentForSUT = new Chat(idOfChat);

        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        JDBCChatDAO SUT = new JDBCChatDAO(dataSource);
        SUT.add(argumentForSUT);

        //Action
        boolean contains = SUT.containsChatWithId(idOfChat);

        //Assert
        assertTrue(contains, ()->"DB must contain chat with id " + idOfChat);
    }
    @Test
    @Transactional
    @Rollback
    public void notContainsChatTest(){
        //Assign
        final int idOfChat = 100;

        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        JDBCChatDAO SUT = new JDBCChatDAO(dataSource);

        //Action
        boolean contains = SUT.containsChatWithId(idOfChat);

        //Assert
        assertFalse(contains, ()->"DB must not contain chat with id " + idOfChat);
    }
    @Test
    @Transactional
    @Rollback
    public void validRemoveChatTest(){
        //Assign
        final int idOfChat = 100;
        Chat addedChat = new Chat(idOfChat);

        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        JDBCChatDAO SUT = new JDBCChatDAO(dataSource);
        SUT.add(addedChat);

        //Action
        SUT.remove(idOfChat);

        //Assert
        assertFalse(SUT.containsChatWithId(idOfChat), ()->"Chat with id " + idOfChat + " must be removed");
    }
    @Test
    @Transactional
    @Rollback
    public void removeNotExistedChatTest(){
        //Assign
        final int idOfChat = 100;

        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        JDBCChatDAO SUT = new JDBCChatDAO(dataSource);

        //Action
        SUT.remove(idOfChat);

        //Assert
        assertFalse(SUT.containsChatWithId(idOfChat), ()->"Chat with id " + idOfChat + " must be removed");
    }
}
