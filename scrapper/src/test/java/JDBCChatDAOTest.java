import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCChatDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration
@Transactional
public class JDBCChatDAOTest extends IntegrationEnvironment{
    public static JDBCChatDAO SUT;
    public static DataSource dataSource;
    @BeforeAll
    public static void setChatDAO(){
        String jdbcUrl = singletonPostgreSQLContainer.getJdbcUrl();
        String username = singletonPostgreSQLContainer.getUsername();
        String password = singletonPostgreSQLContainer.getPassword();
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);

        SUT = new JDBCChatDAO(dataSource);
    }
    @Test
    @Rollback
    public void validAddTest(){
        //Assign
        final int idChat = 100;
        Chat argumentForSUT = new Chat(idChat);

        //Action
        SUT.add(argumentForSUT);

        //Assert
        Chat chatFromDB = SUT.findByID(idChat);
        assertEquals(chatFromDB.getId(), idChat);
        SUT.remove(idChat);
    }
    @Test
    @Rollback
    public void containsChatTest(){
        //Assign
        final int idChat = 100;
        Chat argumentForSUT = new Chat(idChat);
        SUT.add(argumentForSUT);

        //Action
        boolean contains = SUT.containsChatWithId(idChat);

        //Assert
        assertTrue(contains, ()->"DB must contain chat with id " + idChat);
    }
    @Test
    @Rollback
    public void notContainsChatTest(){
        //Assign
        final int idOfChat = 100;

        //Action
        boolean contains = SUT.containsChatWithId(idOfChat);

        //Assert
        assertFalse(contains, ()->"DB must not contain chat with id " + idOfChat);
    }
    @Test
    @Rollback
    public void validRemoveChatTest(){
        //Assign
        final int idOfChat = 100;
        Chat addedChat = new Chat(idOfChat);
        SUT.add(addedChat);

        //Action
        SUT.remove(idOfChat);

        //Assert
        assertFalse(SUT.containsChatWithId(idOfChat), ()->"Chat with id " + idOfChat + " must be removed");
    }
    @Test
    @Rollback
    public void removeNotExistedChatTest(){
        //Assign
        final int idOfChat = 100;

        //Action
        SUT.remove(idOfChat);

        //Assert
        assertFalse(SUT.containsChatWithId(idOfChat), ()->"Chat with id " + idOfChat + " must be removed");
    }
}
