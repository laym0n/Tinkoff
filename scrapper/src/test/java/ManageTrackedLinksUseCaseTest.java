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
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.ManageLinksUseCase;
import ru.tinkoff.edu.java.scrapper.usecases.impl.managelinks.ManageLinksUseCaseImpl;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClient;

import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ManageTrackedLinksUseCaseTest {
    @Test
    void testValidAddLinkWithAlreadySavedWebsiteInfoForChat(){
        //Assign
        final int idChat = 1;
        final int expectedIdTrackedLink = 100;
        final int expectedIfWebsiteInfo = 200;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        TrackedLink expectedResultFromSUT =
                new TrackedLink(expectedIdTrackedLink, idChat, expectedIfWebsiteInfo, expectedLinkInfo);
        TrackedLink expectedArgumentForDAService = new TrackedLink(expectedIdTrackedLink, idChat, expectedIfWebsiteInfo, expectedLinkInfo);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(eq(idChat))).thenReturn(true);
        when(trackedLinkDAService
                .containsTrackedLinkWithIdChatAndLinkInfo(eq(idChat), eq(expectedLinkInfo)))
        .thenReturn(false);
        when(trackedLinkDAService.containsWebsiteInfoWithLinkInfo(expectedLinkInfo))
                .thenReturn(Optional.of(expectedIfWebsiteInfo));
        when(trackedLinkDAService.createTrackedLink(any(TrackedLink.class))).thenAnswer(i-> {
            ((TrackedLink)i.getArgument(0)).setId(expectedIdTrackedLink);
            return i.getArgument(0);
        });

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, null);

        //Action
        TrackedLink resultFromSUT = sut.addLinkForChat(idChat, new AddLinkRequest(path));

        //Assert
        verify(trackedLinkDAService, never()).createWebsiteInfo(any(WebsiteInfo.class));
        ArgumentCaptor<TrackedLink> argument = ArgumentCaptor.forClass(TrackedLink.class);
        verify(trackedLinkDAService).createTrackedLink(argument.capture());
        assertEquals(expectedArgumentForDAService.getIdChat(), argument.getValue().getIdChat(),
                ()->"Expected chatId of argument for DAService is "
                        + expectedArgumentForDAService.getIdChat() +
                " but chatId of argument is " + argument.getValue().getIdChat());
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
    void testValidAddLinkWithNotSavedWebsiteInfoForChat(){
        //Assign
        final int idChat = 1;
        final int expectedIdTrackedLink = 100;
        final int expectedIdWebsiteInfo = 0;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        TrackedLink expectedResultFromSUT =
                new TrackedLink(expectedIdTrackedLink, idChat, expectedIdWebsiteInfo, expectedLinkInfo);
        TrackedLink expectedArgumentForDAService = new TrackedLink(expectedIdTrackedLink, idChat, expectedIdWebsiteInfo, expectedLinkInfo);

        WebsiteInfo gitHubInfo = new GitHubInfo((GitHubLinkInfo) expectedLinkInfo, OffsetDateTime.now());

        WebsiteInfoWebClient webClient = mock(WebsiteInfoWebClient.class);
        when(webClient.getWebSiteInfoByLinkInfo(eq(expectedLinkInfo))).thenReturn(gitHubInfo);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(eq(idChat))).thenReturn(true);
        when(trackedLinkDAService
                .containsTrackedLinkWithIdChatAndLinkInfo(eq(idChat), eq(expectedLinkInfo)))
                .thenReturn(false);
        when(trackedLinkDAService.containsWebsiteInfoWithLinkInfo(eq(expectedLinkInfo)))
                .thenReturn(Optional.empty());
        when(trackedLinkDAService.createTrackedLink(any(TrackedLink.class))).thenAnswer(i-> {
            ((TrackedLink)i.getArgument(0)).setId(expectedIdTrackedLink);
            return i.getArgument(0);
        });

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, webClient);

        //Action
        TrackedLink resultFromSUT = sut.addLinkForChat(idChat, new AddLinkRequest(path));

        //Assert
        ArgumentCaptor<WebsiteInfo> argumentWebsiteInfo = ArgumentCaptor.forClass(WebsiteInfo.class);
        verify(trackedLinkDAService).createWebsiteInfo(argumentWebsiteInfo.capture());
        assertEquals(gitHubInfo, argumentWebsiteInfo.getValue(),
                ()->"Expected argument of WebsiteInfo in createWebsiteInfo is "
                        + gitHubInfo.toString() + " but was " + argumentWebsiteInfo.getValue());

        ArgumentCaptor<TrackedLink> argumentOfTrackedLink = ArgumentCaptor.forClass(TrackedLink.class);
        verify(trackedLinkDAService).createTrackedLink(argumentOfTrackedLink.capture());
        assertEquals(expectedArgumentForDAService.getIdChat(), argumentOfTrackedLink.getValue().getIdChat(),
                ()->"Expected chatId of argument for DAService is "
                        + expectedArgumentForDAService.getIdChat() +
                        " but chatId of argument is " + argumentOfTrackedLink.getValue().getIdChat());
        assertEquals(expectedArgumentForDAService.getLinkInfo(), argumentOfTrackedLink.getValue().getLinkInfo(),
                ()->"Expected WebsiteInfo of argument for DAService is "
                        + expectedArgumentForDAService.getLinkInfo() +
                        " but WebsiteInfo of argument is "
                        + argumentOfTrackedLink.getValue().getLinkInfo());
        assertEquals(expectedResultFromSUT, resultFromSUT,
                ()->"Expected result of method is " + expectedResultFromSUT +
                        " but result is " + resultFromSUT);
    }
    @Test
    void testAddLinkForNotExistedChat(){
        //Assign
        final int idChat = 1;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final String expectedMessage = "Chat with id " + idChat + " is not registered";

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();

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
    void testAddNotValidLinkForChat(){
        //Assign
        final String path = "";
        final int idChat = 1;
        final String expectedMessage = "Path " + path + " can not be parsed";

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
    void testAddAlreadyTrackedLinkForChat(){
        //Assign
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final int idChat = 1;
        final String expectedMessage = "Link with path " + path + " already " +
                "tracked in chat with id " + idChat;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        LinkInfo expectedLinkInfo = parserLinks.parse(path);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);
        when(trackedLinkDAService.containsTrackedLinkWithIdChatAndLinkInfo(eq(idChat), eq(expectedLinkInfo)))
                .thenReturn(true);

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
    void testValidRemoveLinkFromChat(){
        //Assign
        final int idChat = 1;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final int expectedIdLink = 100;
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        final TrackedLink expectedResult = new TrackedLink(expectedIdLink, idChat, expectedIdLink, expectedLinkInfo);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);
        when(trackedLinkDAService
                .deleteTrackedLinkByIdChatAndLinkInfo(eq(idChat), eq(expectedLinkInfo)))
        .thenReturn(Optional.of(expectedResult));

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, null);

        //Action
        TrackedLink resultFromSUT = sut.removeLinkFromChat(idChat, new RemoveLinkRequest(path));

        ArgumentCaptor<LinkInfo> argumentCaptorOfLinkInfo = ArgumentCaptor.forClass(LinkInfo.class);
        ArgumentCaptor<Integer> argumentCaptorOfIdChat = ArgumentCaptor.forClass(Integer.class);
        verify(trackedLinkDAService)
                .deleteTrackedLinkByIdChatAndLinkInfo(argumentCaptorOfIdChat.capture(),
                        argumentCaptorOfLinkInfo.capture());
        assertEquals(idChat, argumentCaptorOfIdChat.getValue(),
                ()->"Link must be removed from chat with id " + idChat +
                " but removed from chat with id " + argumentCaptorOfIdChat.getValue());
        assertEquals(expectedLinkInfo, argumentCaptorOfLinkInfo.getValue(),
                ()->"Link " + expectedLinkInfo.getDescriptionOfParsedLink() + " must be removed but "
        + argumentCaptorOfLinkInfo.getValue().getDescriptionOfParsedLink() + " was removed");
        assertEquals(expectedResult, resultFromSUT,
                ()->"Expected result of remove path " + path + " is "
                        + expectedResult + " but result from sut is " + resultFromSUT);
    }

    @Test
    void testRemoveNotValidLinkFromChat(){
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
    void testRemoveNotExistedInDBLinkFromChat(){
        //Assign
        final int idChat = 1;
        final String path = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final String expectedMessage = "Link with path " + path +
                " already is not tracked in chat with id " + idChat;

        ParserLinks parserLinks = new ParserLinksFactoryImpl().getParserLinks();
        final LinkInfo expectedLinkInfo = parserLinks.parse(path);

        TrackedLinkDAService trackedLinkDAService = mock(TrackedLinkDAService.class);
        when(trackedLinkDAService.containsChatWithId(anyInt())).thenReturn(true);
        when(trackedLinkDAService
                .deleteTrackedLinkByIdChatAndLinkInfo(eq(idChat), eq(expectedLinkInfo)))
                .thenReturn(Optional.empty());

        ManageLinksUseCase sut = new ManageLinksUseCaseImpl(parserLinks, trackedLinkDAService, null);

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
    void testValidGetAllLinksForChat(){
        //Assign
        final int idChat = 1;
        OffsetDateTime expectedOffsetDateTime = OffsetDateTime.MAX;

        List<TrackedLink> expectedResult = new ArrayList<>();
        expectedResult.add(new TrackedLink(1, idChat,1,
                new GitHubLinkInfo("user1", "repository1")));
        expectedResult.add(new TrackedLink(2, idChat,1,
                new GitHubLinkInfo("user2", "repository2")));
        expectedResult.add(new TrackedLink(3, idChat,1,
                new GitHubLinkInfo("user3", "repository3")));


        List<TrackedLink> resultFromDAService = new ArrayList<>();
        resultFromDAService.add(new TrackedLink(1, idChat,1,
                new GitHubLinkInfo("user1", "repository1")));
        resultFromDAService.add(new TrackedLink(2, idChat,1,
                new GitHubLinkInfo("user2", "repository2")));
        resultFromDAService.add(new TrackedLink(3, idChat,1,
                new GitHubLinkInfo("user3", "repository3")));

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
    void testGetAllLinksForNotExistedChat(){
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
