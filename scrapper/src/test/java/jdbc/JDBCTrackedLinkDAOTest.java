package jdbc;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.*;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class JDBCTrackedLinkDAOTest extends JDBCIntegrationEnvironment{
    @Autowired
    private JDBCTrackedLinkDAO SUT;
    @Autowired
    private JDBCStackOverflowInfoDAO infoDAO;
    @Autowired
    private JDBCChatDAO chatDAO;
//    @Test
//    @Rollback
//    @Transactional
//    public void addLinkTest(){
//        //Assign
//        final int idChat = 100;
//        chatDAO.add(new Chat(idChat));
//
//        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
//        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
//        infoDAO.add(websiteInfo);
//
//        TrackedLink trackedLinkForSUT = new TrackedLink(idChat, linkInfoForWebsite, websiteInfo.getId());
//
//        //Action
//        SUT.add(trackedLinkForSUT);
//
//        //Assert
//        List<TrackedLink> resultFromSut = SUT.findAllByChatId(idChat);
//        assertEquals(resultFromSut.size(), 1,
//                ()->"Only one chat track test website info");
//        assertEquals(trackedLinkForSUT, resultFromSut.get(0));
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void containsSavedTrackedLinkTest(){
//        //Assign
//        final int idChat = 100;
//        chatDAO.add(new Chat(idChat));
//
//        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
//        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
//        infoDAO.add(websiteInfo);
//
//        TrackedLink trackedLinkForSUT = new TrackedLink(idChat, linkInfoForWebsite, websiteInfo.getId());
//        SUT.add(trackedLinkForSUT);
//
//        //Action
//        boolean resultFromSUT = SUT.containsTrackedLinkWithChatIdAndLinkInfo(trackedLinkForSUT.getLinkInfo(), idChat);
//
//        //Assert
//        assertTrue(resultFromSUT, ()->"Link saved but result of method contains is false");
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void containsNotSavedTrackedLinkTest(){
//        //Assign
//        final int idChat = 100;
//
//        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
//
//        //Action
//        boolean resultFromSUT = SUT.containsTrackedLinkWithChatIdAndLinkInfo(linkInfoForWebsite, idChat);
//
//        //Assert
//        assertFalse(resultFromSUT, ()->"Link not saved but result of method contains is true");
//    }
//    @Test
//    @Rollback
//    @Transactional
//    public void removeSavedLinkByLinkInfoAndChatIdTest(){
//        //Assign
//        final int idChat = 100;
//        chatDAO.add(new Chat(idChat));
//
//        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
//        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
//        infoDAO.add(websiteInfo);
//
//        TrackedLink trackedLinkForSUT = new TrackedLink(idChat, linkInfoForWebsite, websiteInfo.getId());
//        SUT.add(trackedLinkForSUT);
//
//        //Action
//        Optional<TrackedLink> resultFromSUT = SUT.remove(trackedLinkForSUT.getLinkInfo(), idChat);
//
//        //Assert
//        assertTrue(resultFromSUT.isPresent(), ()->"Tracked Link was in DB and must be returned but result of optional is empty");
//        assertEquals(trackedLinkForSUT, resultFromSUT.get());
//        assertFalse(SUT.containsTrackedLinkWithChatIdAndLinkInfo(linkInfoForWebsite, idChat));
//    }
//    @Test
//    @Rollback
//    @Transactional
//    public void removeNotSavedLinkByLinkInfoAndChatIdTest(){
//        //Assign
//        final int idChat = 100;
//        chatDAO.add(new Chat(idChat));
//
//        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
//
//        //Action
//        Optional<TrackedLink> resultFromSUT = SUT.remove(linkInfoForWebsite, idChat);
//
//        //Assert
//        assertTrue(resultFromSUT.isEmpty(), ()->"TrackedLinkDAO must return Optional.empty() if tracked link not saved");
//    }
    @Test
    @Transactional
    @Rollback
    public void findAllByChatIdTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new Chat(idChat));

        infoDAO.add(new StackOverflowInfo(new StackOverflowLinkInfo(1)));
        infoDAO.add(new StackOverflowInfo(new StackOverflowLinkInfo(2)));
        infoDAO.add(new StackOverflowInfo(new StackOverflowLinkInfo(3)));
        infoDAO.add(new StackOverflowInfo(new StackOverflowLinkInfo(4)));
        infoDAO.add(new StackOverflowInfo(new StackOverflowLinkInfo(5)));
        infoDAO.add(new StackOverflowInfo(new StackOverflowLinkInfo(6)));
        infoDAO.add(new StackOverflowInfo(new StackOverflowLinkInfo(7)));

        List<TrackedLink> argumentsForSUT = List.of(
                new TrackedLink(idChat, new StackOverflowLinkInfo(1), 1),
                new TrackedLink(idChat, new StackOverflowLinkInfo(2), 2),
                new TrackedLink(idChat, new StackOverflowLinkInfo(3), 3),
                new TrackedLink(idChat, new StackOverflowLinkInfo(4), 4)
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
    public void findAllChatIdsWithTrackedLinkTest(){
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
                new TrackedLink(1, new StackOverflowLinkInfo(1), 1),
                new TrackedLink(2, new StackOverflowLinkInfo(2), 2),
                new TrackedLink(3, new StackOverflowLinkInfo(3), 3),
                new TrackedLink(4, new StackOverflowLinkInfo(4), 4)
        );
        List<TrackedLink> expectedResult = List.of(
                new TrackedLink(5,  new StackOverflowLinkInfo(5), 5),
                new TrackedLink(4,  new StackOverflowLinkInfo(5), 5),
                new TrackedLink(3,  new StackOverflowLinkInfo(5), 5),
                new TrackedLink(2,  new StackOverflowLinkInfo(5), 5)
        );
        for(TrackedLink trackedLink : expectedResult)
            SUT.add(trackedLink);
        for(TrackedLink trackedLink : argumentsForSUT)
            SUT.add(trackedLink);

        //Action
        int[] resultFromSUT = SUT.findAllChatsWithIdWebsiteInfo(5);

        //Assert
        assertTrue(Arrays.equals(expectedResult.stream().flatMapToInt(i-> IntStream.of(i.getIdChat())).toArray(), resultFromSUT),
                ()->"Expected: " + expectedResult.stream().flatMapToInt(i-> IntStream.of(i.getId())).toArray().toString() +
                "\nLoaded: " + resultFromSUT);
    }
}
