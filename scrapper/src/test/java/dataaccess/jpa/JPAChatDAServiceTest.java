package dataaccess.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.chatdaservice.JPAChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

public class JPAChatDAServiceTest extends JPAIntegrationEnvironment{
    @Autowired
    private JPAChatDAService SUT;
    @Test
    @Rollback
    @Transactional
    public void testThrowExceptionWhenAddDuplicatedChat(){
        //Assign
        Chat argument = new Chat(5);
        SUT.createChat(argument);

        //Action and Assert
        assertThrows(InvalidParameterException.class, ()->SUT.createChat(argument));
    }
}
