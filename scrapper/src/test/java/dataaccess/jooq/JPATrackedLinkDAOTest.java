package dataaccess.jooq;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPATrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.TrackedLinkEntity;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JPATrackedLinkDAOTest extends JPAIntegrationEnvironment {
    @Autowired
    private JPATrackedLinkDAO SUT;
    @Autowired
    private JPAStackOverflowInfoDAO infoDAO;
    @Autowired
    private JPAChatDAO chatDAO;
    @Test
    @Rollback
    @Transactional
    void addLinkTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new ChatEntity(idChat));

        StackOverflowInfoEntity websiteInfo = new StackOverflowInfoEntity(1);
        infoDAO.add(websiteInfo);

        TrackedLinkEntity trackedLinkForSUT = new TrackedLinkEntity(websiteInfo.getId(), idChat);

        //Action
        SUT.add(trackedLinkForSUT);

        //Assert
        List<TrackedLinkEntity> resultFromSut = SUT.findAllByChatId(idChat);
        assertEquals(1, resultFromSut.size(),
                ()->"Only one chat track test website info");
        assertEquals(new TrackedLinkEntity(trackedLinkForSUT.getId(), websiteInfo.getId(), idChat), resultFromSut.get(0));
    }
    @Test
    @Rollback
    @Transactional
    void containsSavedTrackedLinkTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new ChatEntity(idChat));

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
        StackOverflowInfoEntity entityForLoad = new StackOverflowInfoEntity(websiteInfo);
        infoDAO.add(entityForLoad);

        TrackedLinkEntity trackedLinkForSUT = new TrackedLinkEntity(entityForLoad.getId(), idChat);
        SUT.add(trackedLinkForSUT);

        //Action
        boolean resultFromSUT = SUT.containsTrackedLinkWithChatIdAndLinkInfo(linkInfoForWebsite, idChat);

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
        chatDAO.add(new ChatEntity(idChat));

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);
        StackOverflowInfo websiteInfo = new StackOverflowInfo(linkInfoForWebsite);
        StackOverflowInfoEntity entityForLoad = new StackOverflowInfoEntity(websiteInfo);
        infoDAO.add(entityForLoad);

        TrackedLinkEntity trackedLinkForSUT = new TrackedLinkEntity(entityForLoad.getId(), idChat);
        SUT.add(trackedLinkForSUT);

        //Action
        Optional<TrackedLinkEntity> resultFromSUT = SUT.remove(linkInfoForWebsite, idChat);

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
        chatDAO.add(new ChatEntity(idChat));

        StackOverflowLinkInfo linkInfoForWebsite = new StackOverflowLinkInfo(1);

        //Action
        Optional<TrackedLinkEntity> resultFromSUT = SUT.remove(linkInfoForWebsite, idChat);

        //Assert
        assertTrue(resultFromSUT.isEmpty(), ()->"TrackedLinkDAO must return Optional.empty() if tracked link not saved");
    }
    @Test
    @Transactional
    @Rollback
    void findAllByChatIdTest(){
        //Assign
        final int idChat = 100;
        chatDAO.add(new ChatEntity(idChat));

        List<StackOverflowInfoEntity> soInfos = List.of(
                new StackOverflowInfoEntity(1),
                new StackOverflowInfoEntity(2),
                new StackOverflowInfoEntity(3),
                new StackOverflowInfoEntity(4),
                new StackOverflowInfoEntity(5),
                new StackOverflowInfoEntity(6),
                new StackOverflowInfoEntity(7)
        );

        for(var info : soInfos){
            infoDAO.add(info);
        }

        List<TrackedLinkEntity> argumentsForSUT = List.of(
                new TrackedLinkEntity(soInfos.get(0).getId(), idChat),
                new TrackedLinkEntity(soInfos.get(1).getId(), idChat),
                new TrackedLinkEntity(soInfos.get(2).getId(), idChat),
                new TrackedLinkEntity(soInfos.get(3).getId(), idChat)
        );
        for(TrackedLinkEntity trackedLink : argumentsForSUT)
            SUT.add(trackedLink);

        //Action
        List<TrackedLinkEntity> resultFromSUT = SUT.findAllByChatId(idChat);

        //Assert
        assertEquals(new HashSet<>(List.of(
                new TrackedLinkEntity(argumentsForSUT.get(0).getId(), soInfos.get(0).getId(), idChat),
                new TrackedLinkEntity(argumentsForSUT.get(1).getId(), soInfos.get(1).getId(), idChat),
                new TrackedLinkEntity(argumentsForSUT.get(2).getId(), soInfos.get(2).getId(), idChat),
                new TrackedLinkEntity(argumentsForSUT.get(3).getId(), soInfos.get(3).getId(), idChat)
        )), new HashSet<>(resultFromSUT));
    }
    @Test
    @Transactional
    @Rollback
    void findAllChatIdsWithTrackedLinkTest(){
        //Assign
        List<ChatEntity> chats = List.of(
            new ChatEntity(1),
            new ChatEntity(2),
            new ChatEntity(3),
            new ChatEntity(4),
            new ChatEntity(5)
        );
        for(ChatEntity chat: chats){
            chatDAO.add(chat);
        }

        List<StackOverflowInfoEntity> soInfos = List.of(
            new StackOverflowInfoEntity(1),
            new StackOverflowInfoEntity(2),
            new StackOverflowInfoEntity(3),
            new StackOverflowInfoEntity(4),
            new StackOverflowInfoEntity(5),
            new StackOverflowInfoEntity(6),
            new StackOverflowInfoEntity(7)

        );
        for(StackOverflowInfoEntity info : soInfos){
            infoDAO.add(info);
        }

        List<TrackedLinkEntity> argumentsForSUT = List.of(
                new TrackedLinkEntity(soInfos.get(0).getId(), 1),
                new TrackedLinkEntity(soInfos.get(1).getId(), 2),
                new TrackedLinkEntity(soInfos.get(2).getId(), 3),
                new TrackedLinkEntity(soInfos.get(3).getId(), 4)
        );
        List<TrackedLinkEntity> expectedResult = List.of(
                new TrackedLinkEntity(soInfos.get(4).getId(), 5),
                new TrackedLinkEntity(soInfos.get(4).getId(), 4),
                new TrackedLinkEntity(soInfos.get(4).getId(), 3),
                new TrackedLinkEntity(soInfos.get(4).getId(), 2)
        );
        for(TrackedLinkEntity trackedLink : expectedResult)
            SUT.add(trackedLink);
        for(TrackedLinkEntity trackedLink : argumentsForSUT)
            SUT.add(trackedLink);

        //Action
        int[] resultFromSUT = SUT.findAllChatsWithIdWebsiteInfo(soInfos.get(4).getId());

        //Assert
        Set<Integer> expectedIds = expectedResult.stream().map(i-> i.getChatId()).collect(Collectors.toSet());
        Set<Integer> loadedIds = Arrays.stream(resultFromSUT).mapToObj(i->i).collect(Collectors.toSet());
        assertEquals(expectedIds, loadedIds,
                ()->"Expected: " + expectedIds +
                "\nLoaded: " + loadedIds);
    }
}
