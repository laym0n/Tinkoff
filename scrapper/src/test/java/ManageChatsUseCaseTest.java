import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.usecases.ManageChatsUseCase;
import ru.tinkoff.edu.java.scrapper.usecases.impl.managechats.ManageChatsUseCaseImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ManageChatsUseCaseTest {
    @Test
    void testValidRegistryChat() {
        //Assign
        ChatDAService chatDAService = mock(ChatDAService.class);
        ManageChatsUseCase sut = new ManageChatsUseCaseImpl(chatDAService);

        //Action
        sut.registryChat(1);

        //Assert
        verify(chatDAService).createChat(new Chat(1));
    }

    @Test
    void testValidRemoveChat() {
        //Assign
        ChatDAService chatDAService = mock(ChatDAService.class);
        ManageChatsUseCase sut = new ManageChatsUseCaseImpl(chatDAService);

        //Action
        sut.removeChat(1);

        //Assert
        verify(chatDAService).deleteChat(1);
    }

}
