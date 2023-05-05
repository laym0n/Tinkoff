package dataaccess.jooq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JOOQChatDAOTest extends JOOQIntegrationEnvironment {
    @Autowired
    public JOOQChatDAO SUT;

    @Test
    @Transactional
    @Rollback
    void validAddTest(){
        //Assign
        final int idChat = 100;
        Chat chat = new Chat(idChat);

        //Action
        SUT.add(chat);

        //Assert
        Optional<Chat> loadedChat = SUT.findByID(idChat);
        assertEquals(loadedChat.get().getId(), idChat);
    }
    @Test
    @Transactional
    @Rollback
    void containsChatTest(){
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
    void notContainsChatTest(){
        //Assign
        final int idChat = 100;

        //Action
        boolean contains = SUT.containsChatWithId(idChat);

        //Assert
        assertFalse(contains, ()->"DB must not contain chat with id " + idChat);
    }
    @Test
    @Transactional
    @Rollback
    void validRemoveChatTest(){
        //Assign
        final int idChat = 100;
        Chat argumentForSUT = new Chat(idChat);
        SUT.add(argumentForSUT);

        //Action
        SUT.remove(idChat);

        //Assert
        assertFalse(SUT.containsChatWithId(idChat), ()->"Chat with id " +
                idChat + " must be removed");
    }
}
