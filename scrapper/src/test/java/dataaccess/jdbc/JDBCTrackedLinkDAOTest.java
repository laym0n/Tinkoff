package dataaccess.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JDBCTrackedLinkDAOTest extends JDBCIntegrationEnvironment {
    @Autowired
    private JDBCTrackedLinkDAO SUT;
    @Autowired
    private JDBCStackOverflowInfoDAO infoDAO;
    @Autowired
    private JDBCChatDAO chatDAO;
    @Test
    @Rollback
    @Transactional
    void addLinkTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new Chat(idChat));

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
        infoDAO.add(websiteInfo);

        TrackedLink trackedLinkForSUT = new TrackedLink(idChat, linkInfoForWebsite, websiteInfo.getId());

        //Action
        SUT.add(trackedLinkForSUT);

        //Assert
        List<TrackedLink> resultFromSut = SUT.findAllByChatId(idChat);
        assertEquals(1, resultFromSut.size(),
                ()->"Only one chat track test website info");
        assertEquals(trackedLinkForSUT, resultFromSut.get(0));
    }

    @Test
    @Rollback
    @Transactional
    void containsSavedTrackedLinkTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new Chat(idChat));

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
        infoDAO.add(websiteInfo);

        TrackedLink trackedLinkForSUT = new TrackedLink(idChat, linkInfoForWebsite, websiteInfo.getId());
        SUT.add(trackedLinkForSUT);

        //Action
        boolean resultFromSUT = SUT.containsTrackedLinkWithChatIdAndLinkInfo(trackedLinkForSUT.getLinkInfo(), idChat);

        //Assert
        assertTrue(resultFromSUT, ()->"Link saved but result of method contains is false");
    }

    @Test
    @Rollback
    @Transactional
    void containsNotSavedTrackedLinkTest(){
        //Assign
        final int idChat = 100;

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);

        //Action
        boolean resultFromSUT = SUT.containsTrackedLinkWithChatIdAndLinkInfo(linkInfoForWebsite, idChat);

        //Assert
        assertFalse(resultFromSUT, ()->"Link not saved but result of method contains is true");
    }
    @Test
    @Rollback
    @Transactional
    void removeSavedLinkByLinkInfoAndChatIdTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new Chat(idChat));

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
        infoDAO.add(websiteInfo);

        TrackedLink trackedLinkForSUT = new TrackedLink(idChat, linkInfoForWebsite, websiteInfo.getId());
        SUT.add(trackedLinkForSUT);

        //Action
        Optional<TrackedLink> resultFromSUT = SUT.remove(trackedLinkForSUT.getLinkInfo(), idChat);

        //Assert
        assertTrue(resultFromSUT.isPresent(), ()->"Tracked Link was in DB and must be returned but result of optional is empty");
        assertEquals(trackedLinkForSUT, resultFromSUT.get());
        assertFalse(SUT.containsTrackedLinkWithChatIdAndLinkInfo(linkInfoForWebsite, idChat));
    }
    @Test
    @Rollback
    @Transactional
    void removeNotSavedLinkByLinkInfoAndChatIdTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new Chat(idChat));

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);

        //Action
        Optional<TrackedLink> resultFromSUT = SUT.remove(linkInfoForWebsite, idChat);

        //Assert
        assertTrue(resultFromSUT.isEmpty(), ()->"TrackedLinkDAO must return Optional.empty() if tracked link not saved");
    }
    @Test
    @Transactional
    @Rollback
    void findAllByChatIdTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new Chat(idChat));

        List<StackOverflowInfo> soInfos = List.of(
                new StackOverflowInfo(new StackOverflowLinkInfo(1)),
                new StackOverflowInfo(new StackOverflowLinkInfo(2)),
                new StackOverflowInfo(new StackOverflowLinkInfo(3)),
                new StackOverflowInfo(new StackOverflowLinkInfo(4)),
                new StackOverflowInfo(new StackOverflowLinkInfo(5)),
                new StackOverflowInfo(new StackOverflowLinkInfo(6)),
                new StackOverflowInfo(new StackOverflowLinkInfo(7))
        );

        for(var info : soInfos){
            infoDAO.add(info);
        }

        List<TrackedLink> argumentsForSUT = List.of(
                new TrackedLink(idChat, new StackOverflowLinkInfo(1), soInfos.get(0).getId()),
                new TrackedLink(idChat, new StackOverflowLinkInfo(2), soInfos.get(1).getId()),
                new TrackedLink(idChat, new StackOverflowLinkInfo(3), soInfos.get(2).getId()),
                new TrackedLink(idChat, new StackOverflowLinkInfo(4), soInfos.get(3).getId())
        );
        for(TrackedLink trackedLink : argumentsForSUT)
            SUT.add(trackedLink);

        //Action
        List<TrackedLink> resultFromSUT = SUT.findAllByChatId(idChat);

        //Assert
        assertEquals(argumentsForSUT, resultFromSUT);
    }
    @Test
    @Transactional
    @Rollback
    void findAllChatIdsWithTrackedLinkTest(){
        //Assign
        List<Chat> chats = List.of(
            new Chat(1),
            new Chat(2),
            new Chat(3),
            new Chat(4),
            new Chat(5)
        );
        for(Chat chat: chats){
            chatDAO.add(chat);
        }

        List<StackOverflowInfo> soInfos = List.of(
            new StackOverflowInfo(new StackOverflowLinkInfo(1)),
            new StackOverflowInfo(new StackOverflowLinkInfo(2)),
            new StackOverflowInfo(new StackOverflowLinkInfo(3)),
            new StackOverflowInfo(new StackOverflowLinkInfo(4)),
            new StackOverflowInfo(new StackOverflowLinkInfo(5)),
            new StackOverflowInfo(new StackOverflowLinkInfo(6)),
            new StackOverflowInfo(new StackOverflowLinkInfo(7))

        );
        for(StackOverflowInfo info : soInfos){
            infoDAO.add(info);
        }

        List<TrackedLink> argumentsForSUT = List.of(
                new TrackedLink(1, new StackOverflowLinkInfo(1), soInfos.get(0).getId()),
                new TrackedLink(2, new StackOverflowLinkInfo(2), soInfos.get(1).getId()),
                new TrackedLink(3, new StackOverflowLinkInfo(3), soInfos.get(2).getId()),
                new TrackedLink(4, new StackOverflowLinkInfo(4), soInfos.get(3).getId())
        );
        List<TrackedLink> expectedResult = List.of(
                new TrackedLink(5,  new StackOverflowLinkInfo(5), soInfos.get(4).getId()),
                new TrackedLink(4,  new StackOverflowLinkInfo(5), soInfos.get(4).getId()),
                new TrackedLink(3,  new StackOverflowLinkInfo(5), soInfos.get(4).getId()),
                new TrackedLink(2,  new StackOverflowLinkInfo(5), soInfos.get(4).getId())
        );
        for(TrackedLink trackedLink : expectedResult)
            SUT.add(trackedLink);
        for(TrackedLink trackedLink : argumentsForSUT)
            SUT.add(trackedLink);

        //Action
        int[] resultFromSUT = SUT.findAllChatsWithIdWebsiteInfo(soInfos.get(4).getId());

        //Assert
        Set<Integer> expectedIds = expectedResult.stream().map(TrackedLink::getIdChat).collect(Collectors.toSet());
        Set<Integer> loadedIds = Arrays.stream(resultFromSUT).boxed().collect(Collectors.toSet());
        assertEquals(expectedIds, loadedIds,
                ()->"Expected: " + expectedIds +
                "\nLoaded: " + loadedIds);
    }
}
