import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;
import parserservice.ParserLinks;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import parserservice.factories.factoryimpl.ParserLinksFactoryImpl;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.usecases.ManageLinksUseCase;
import ru.tinkoff.edu.java.scrapper.usecases.impl.managelinks.ManageLinksUseCaseImpl;
import ru.tinkoff.edu.java.scrapper.webclients.WebsiteInfoWebClient;

import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ManageTrackedLinksUseCaseTest {
    @Test
    public void testValidAddLinkWithAlreadySavedWebsiteInfoForChat(){
        //Assign
        final int idChat = 1;
        final int expectedIdTrackedLink = 100;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        TrackedLink expectedResultFromSUT =
                new TrackedLink(expectedIdTrackedLink, idChat, expectedLinkInfo);
        TrackedLink expectedArgumentForDAService = new TrackedLink(idChat, expectedLinkInfo);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);
        when(trackedLinkDAService
                .containsTrackedLinkWithIdChatAndLinkInfo(anyInt(), any(LinkInfo.class)))
        .thenReturn(false);
        when(trackedLinkDAService.containsWebsiteInfoWithLinkInfo(any(LinkInfo.class)))
                .thenReturn(true);
        when(trackedLinkDAService.createTrackedLink(any(TrackedLink.class))).thenAnswer(i-> {
            ((TrackedLink)i.getArgument(0)).setId(expectedIdTrackedLink);
            return i.getArgument(0);
        });

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, null);

        //Action
        TrackedLink resultFromSUT = sut.addLinkForChat(idChat, new AddLinkRequest(path));

        //Assert
        ArgumentCaptor<TrackedLink> argument = ArgumentCaptor.forClass(TrackedLink.class);
        verify(trackedLinkDAService).createTrackedLink(argument.capture());
        assertEquals(expectedArgumentForDAService.getChatId(), argument.getValue().getChatId(),
                ()->"Expected chatId of argument for DAService is "
                        + expectedArgumentForDAService.getChatId() +
                " but chatId of argument is " + argument.getValue().getChatId());
        assertEquals(expectedArgumentForDAService.getLinkInfo(), argument.getValue().getLinkInfo(),
                ()->"Expected WebsiteInfo of argument for DAService is "
                        + expectedArgumentForDAService.getLinkInfo() +
                        " but WebsiteInfo of argument is "
                        + argument.getValue().getLinkInfo());
        assertEquals(expectedResultFromSUT, resultFromSUT,
                ()->"Expected result of method is " + expectedResultFromSUT +
                        " but result is " + resultFromSUT);
    }
    @Test
    public void testAddLinkForNotExistedChat(){
        //Assign
        final int idChat = 1;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final String expectedMessage = "Chat with id " + idChat + " is not registered";
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        WebsiteInfoWebClient websiteInfoWebClient = mock(WebsiteInfoWebClient.class);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(false);

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, websiteInfoWebClient);

        //Action
        InvalidParameterException resultExceptionFromSUT =
                assertThrows(InvalidParameterException.class, ()-> {
            sut.addLinkForChat(idChat, new AddLinkRequest(path));
        });

        //Assert
        assertEquals(expectedMessage, resultExceptionFromSUT.getMessage(),
                ()->"Expected message is " + expectedMessage +
                        " but result message is " + resultExceptionFromSUT.getMessage());
    }

    @Test
    public void testAddValidButNotExistedLinkForChat(){
        //Assign
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final int idChat = 1;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        LinkInfo expectedLinkInfo = parserLinks.parse(path);

        final String expectedMessage = "Path " + path +
                " is parsed as " + expectedLinkInfo.getDescriptionOfParsedLink()
                + " but can not be checked for update because any checker for that site don't exist";

        WebsiteInfoWebClient websiteInfoWebClient = mock(WebsiteInfoWebClient.class);
        when(websiteInfoWebClient.checkUpdateOfWebsite(any(LinkInfo.class))).thenReturn(null);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, websiteInfoWebClient);

        //Action
        InvalidParameterException resultExceptionFromSUT =
                assertThrows(InvalidParameterException.class, ()-> {
                    sut.addLinkForChat(idChat, new AddLinkRequest(path));
                });

        //Assert
        assertEquals(expectedMessage, resultExceptionFromSUT.getMessage(),
                ()->"Expected message is " + expectedMessage +
                        " but result message is " + resultExceptionFromSUT.getMessage());
    }

    @Test
    public void testAddNotValidLinkForChat(){
        //Assign
        final String path = "";
        final int idChat = 1;
        final String expectedMessage = "Path " + path + " can not be parsed";
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, null);

        //Action
        InvalidParameterException resultExceptionFromSUT =
                assertThrows(InvalidParameterException.class, ()-> {
                    sut.addLinkForChat(idChat, new AddLinkRequest(path));
                });

        //Assert
        assertEquals(expectedMessage, resultExceptionFromSUT.getMessage(),
                ()->"Expected message is " + expectedMessage +
                        " but result message is " + resultExceptionFromSUT.getMessage());
    }

    @Test
    public void testValidRemoveLinkFromChat(){
        //Assign
        final int idChat = 1;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final int expectedIdLink = 100;
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        final TrackedLink expectedResult = new TrackedLink(expectedIdLink, expectedLinkInfo, idChat, expectedOffsetDateTime);

        WebsiteInfoWebClient websiteInfoWebClient = mock(WebsiteInfoWebClient.class);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);
        when(trackedLinkDAService.
                findTrackedLinkByIdChatAndLinkInfo(anyInt(), any(LinkInfo.class))
        ).thenReturn(Optional.of(expectedResult));

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, websiteInfoWebClient);

        //Action
        TrackedLink resultFromSUT = sut.removeLinkFromChat(idChat, new RemoveLinkRequest(path));

        verify(trackedLinkDAService).delete(expectedIdLink);
        assertEquals(expectedResult, resultFromSUT,
                ()->"Expected result of remove path " + path + " is " + expectedResult + " but result from sut is " + resultFromSUT);
    }

    @Test
    public void testRemoveNotValidLinkFromChat(){
        //Assign
        final int idChat = 1;
        final String path = "";
        final String expectedMessage = "Path " + path + " can not be parsed";
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        WebsiteInfoWebClient websiteInfoWebClient = mock(WebsiteInfoWebClient.class);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, websiteInfoWebClient);

        //Action
        InvalidParameterException resultExceptionFromSUT =
                assertThrows(InvalidParameterException.class, ()-> {
                    sut.removeLinkFromChat(idChat, new RemoveLinkRequest(path));
                });

        //Assert
        assertEquals(expectedMessage, resultExceptionFromSUT.getMessage(),
                ()->"Expected message is " + expectedMessage +
                        " but result message is " + resultExceptionFromSUT.getMessage());
    }
    @Test
    public void testRemoveNotExistedInDBLinkFromChat(){
        //Assign
        final int idChat = 1;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final String expectedMessage = "Link with path " + path + " already is not tracked in chat with id " + idChat;
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        WebsiteInfoWebClient websiteInfoWebClient = mock(WebsiteInfoWebClient.class);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);
        when(trackedLinkDAService.findTrackedLinkByIdChatAndLinkInfo(anyInt(), any(LinkInfo.class)))
                .thenReturn(Optional.empty());

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, websiteInfoWebClient);

        //Action
        NotFoundException resultExceptionFromSUT =
                assertThrows(NotFoundException.class, ()-> {
                    sut.removeLinkFromChat(idChat, new RemoveLinkRequest(path));
                });

        //Assert
        assertEquals(expectedMessage, resultExceptionFromSUT.getMessage(),
                ()->"Expected message is " + expectedMessage +
                        " but result message is " + resultExceptionFromSUT.getMessage());
    }
    @Test
    public void testValidGetAllLinksForChat(){
        //Assign
        final int idChat = 1;
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        List<TrackedLink> expectedResult = new ArrayList<>();
        expectedResult.add(new TrackedLink(1,
                new GitHubLinkInfo("user1", "repository1"),
                1, expectedOffsetDateTime));
        expectedResult.add(new TrackedLink(2,
                new GitHubLinkInfo("user2", "repository2"),
                2, expectedOffsetDateTime));
        expectedResult.add(new TrackedLink(3,
                new GitHubLinkInfo("user3", "repository3"),
                1, expectedOffsetDateTime));


        List<TrackedLink> resultFromDAService = new ArrayList<>();
        resultFromDAService.add(new TrackedLink(1,
                new GitHubLinkInfo("user1", "repository1"),
                1, expectedOffsetDateTime));
        resultFromDAService.add(new TrackedLink(2,
                new GitHubLinkInfo("user2", "repository2"),
                2, expectedOffsetDateTime));
        resultFromDAService.add(new TrackedLink(3,
                new GitHubLinkInfo("user3", "repository3"),
                1, expectedOffsetDateTime));

        WebsiteInfoWebClient websiteInfoWebClient = mock(WebsiteInfoWebClient.class);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);
        when(trackedLinkDAService.getAllTrackedLinksByChatId(anyInt()))
                .thenReturn(resultFromDAService);

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(null, trackedLinkDAService, websiteInfoWebClient);

        //Action
        List<TrackedLink> resultFromSUT = sut.findAllLinksByIdChat(idChat);

        //Assert
        assertIterableEquals(expectedResult, resultFromSUT,
                ()->"Expected result is " + expectedResult +
                        "but result from sut is " + resultFromSUT);
    }
    @Test
    public void testGetAllLinksForNotExistedChat(){
        //Assign
        final int idChat = 1;
        final String expectedMessage = "Chat with id " + idChat + " is not registered";

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(false);

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(null, trackedLinkDAService, null);

        //Action
        InvalidParameterException resultExceptionFromSUT =
                assertThrows(InvalidParameterException.class, ()-> {
                    sut.findAllLinksByIdChat(idChat);
                });

        //Assert
        assertEquals(expectedMessage, resultExceptionFromSUT.getMessage(),
                ()->"Expected message is " + expectedMessage +
                        " but result message is " + resultExceptionFromSUT.getMessage());
    }
}
