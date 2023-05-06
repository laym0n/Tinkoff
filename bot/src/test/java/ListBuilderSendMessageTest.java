import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.bot.telegrambotimpl.Command;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.impl.buildersforeachcommand.ListBuilderSendMessage;
import ru.tinkoff.edu.java.bot.webclients.ScrapperClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListBuilderSendMessageTest {
    @Test
    void testZeroLinksTracked(){
        //Assign
        final long chatId = 1;

        ParsedCommand commandForArgument = new ParsedCommand(chatId, Command.LIST, "");

        SendMessage expectedMessage =
                new SendMessage(Long.valueOf(chatId).toString(),
                        "Ни одной ссылки ещё не отслеживается");

        ListLinksResponse listLinksResponseForMock = new ListLinksResponse(
                0, new LinkResponse[0], "", "", "", "", new String[0]
        );
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        when(scrapperClient.allLinksFromChat(chatId)).thenReturn(listLinksResponseForMock);

        ListBuilderSendMessage sut = new ListBuilderSendMessage(scrapperClient);

        //Action
        SendMessage resultFromSUT = sut.getSendMessage(commandForArgument);

        //Assert
        assertEquals(expectedMessage.getText(), resultFromSUT.getText(),
                ()->"Expected message is " + expectedMessage.getText() + " but result message is " + resultFromSUT.getText());
        assertEquals(expectedMessage.getChatId(), resultFromSUT.getChatId(),
                ()->"Expected chatId is " + expectedMessage.getChatId() + " but result chatId is " + resultFromSUT.getChatId());
    }
    @Test
    void testNotZeroLinksTracked(){
        //Assign
        final long chatId = 1;

        ParsedCommand commandForArgument = new ParsedCommand(chatId, Command.LIST, "");

        LinkResponse[] arrayOfLinkResponse = new LinkResponse[1];
        arrayOfLinkResponse[0] =  new LinkResponse(1, null, "", "", "", "", new String[0]);
        ListLinksResponse listLinksResponseForMock = new ListLinksResponse(
                1, arrayOfLinkResponse, "", "", "", "", new String[0]
        );
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        when(scrapperClient.allLinksFromChat(chatId)).thenReturn(listLinksResponseForMock);

        SendMessage notExpectedMessage =
                new SendMessage(Long.valueOf(chatId).toString(),
                        "Ни одной ссылки ещё не отслеживается");

        ListBuilderSendMessage sut = new ListBuilderSendMessage(scrapperClient);

        //Action
        SendMessage resultFromSUT = sut.getSendMessage(commandForArgument);

        //Assert
        assertNotEquals(notExpectedMessage.getText(), resultFromSUT.getText(),
                ()->"Expected message must not be " + notExpectedMessage.getText());
        assertEquals(notExpectedMessage.getChatId(), resultFromSUT.getChatId(),
                ()->"Expected chatId is " + notExpectedMessage.getChatId() + " but result chatId is " + resultFromSUT.getChatId());
    }
}
