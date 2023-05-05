package dataaccess.jooq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.trackedlinkdaservice.JOOQTrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.trackedlinkdaservice.JPATrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JOOQTrackedLinkDAServiceTest extends JOOQIntegrationEnvironment {
    @Autowired
    private JOOQTrackedLinkDAService SUT;
    @Autowired
    private JOOQStackOverflowInfoDAO stackOverflowInfoDAO;
    @Autowired
    private JOOQChatDAO chatDAO;
    @Test
    @Rollback
    @Transactional
    void testSetIdForSavedInfoWhenSaveWebsiteInfo(){
        //Assign
        StackOverflowInfo argument1 = new StackOverflowInfo(new StackOverflowLinkInfo(5));
        StackOverflowInfo argument2 = new StackOverflowInfo(new StackOverflowLinkInfo(100));

        //Action
        SUT.createWebsiteInfo(argument1);
        SUT.createWebsiteInfo(argument2);

        //Assert
        StackOverflowInfo resultFromSUT1 = stackOverflowInfoDAO.getById(argument1.getId()).get();
        StackOverflowInfo resultFromSUT2 = stackOverflowInfoDAO.getById(argument2.getId()).get();
        assertEquals(argument1, resultFromSUT1);
        assertEquals(argument2, resultFromSUT2);
    }
    @Test
    @Rollback
    @Transactional
    void testSetIdForSavedTrackedLinkWhenSaveTrackedLink(){
        //Assign
        final int idChat1 = 100;
        final int idChat2 = 101;
        chatDAO.add(new Chat(idChat1));
        chatDAO.add(new Chat(idChat2));

        StackOverflowInfo info1 = new StackOverflowInfo(new StackOverflowLinkInfo(5));
        StackOverflowInfo info2 = new StackOverflowInfo(new StackOverflowLinkInfo(100));
        SUT.createWebsiteInfo(info1);
        SUT.createWebsiteInfo(info2);

        TrackedLink argument1 = new TrackedLink(idChat1, new StackOverflowLinkInfo(5), info1.getId());
        TrackedLink argument2 = new TrackedLink(idChat2, new StackOverflowLinkInfo(100), info2.getId());

        //Action
        SUT.createTrackedLink(argument1);
        SUT.createTrackedLink(argument2);

        //Assert
        assertTrue(SUT.containsTrackedLinkWithIdChatAndLinkInfo(idChat1, info1.getLinkInfo()),
                ()->"Looks like repository do not set id");
        assertTrue(SUT.containsTrackedLinkWithIdChatAndLinkInfo(idChat2, info2.getLinkInfo()),
                ()->"Looks like repository do not set id");
    }
}
