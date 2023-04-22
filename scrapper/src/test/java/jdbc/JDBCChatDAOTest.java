package jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCChatDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCChatDAOTest extends JDBCIntegrationEnvironment {
    @Autowired
    public JDBCChatDAO SUT;

    @Test
    @Transactional
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
    }
    @Test
    @Transactional
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
    public void notContainsChatTest(){
        //Assign
        final int idOfChat = 100;

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
        SUT.add(addedChat);

        //Action
        SUT.remove(idOfChat);

        //Assert
        assertFalse(SUT.containsChatWithId(idOfChat), ()->"Chat with id " + idOfChat + " must be removed");
    }
//    @Test
//    @Rollback
//    public void removeNotExistedChatTest(){
//        //Assign
//        final int idOfChat = 100;
//
//        //Action
//        SUT.remove(idOfChat);
//
//        //Assert
//        assertFalse(SUT.containsChatWithId(idOfChat), ()->"Chat with id " + idOfChat + " must be removed");
//    }
}
