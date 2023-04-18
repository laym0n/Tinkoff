import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.usecases.ManageChatsUseCase;
import ru.tinkoff.edu.java.scrapper.usecases.impl.managechats.ManageChatsUseCaseImpl;

import java.security.InvalidParameterException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ManageChatsUseCaseTest {
    @Test
    public void testValidRegistryChat(){
        ChatDAService chatDAService = mock(ChatDAService.class);
//        when(chatDAService.containsChatWithId(anyInt())).thenReturn(false);
        ManageChatsUseCase sut = new ManageChatsUseCaseImpl(chatDAService);

        sut.registryChat(1);

        verify(chatDAService).createChat(new Chat(1));
    }
    @Test
    public void testRegistryAlreadyRegistredChat(){
        ChatDAService chatDAService = mock(ChatDAService.class);
//        when(chatDAService.containsChatWithId(anyInt())).thenReturn(true);
        ManageChatsUseCase sut = new ManageChatsUseCaseImpl(chatDAService);

        assertThrows(InvalidParameterException.class, () -> {
            sut.registryChat(1);
        });

    }
    @Test
    public void testValidRemoveChat(){
        ChatDAService chatDAService = mock(ChatDAService.class);
//        when(chatDAService.containsChatWithId(anyInt())).thenReturn(true);
        ManageChatsUseCase sut = new ManageChatsUseCaseImpl(chatDAService);

        sut.removeChat(1);

        verify(chatDAService).deleteChat(1);
    }
    @Test
    public void testRemoveNotRegisteredChat(){
        ChatDAService chatDAService = mock(ChatDAService.class);
//        when(chatDAService.containsChatWithId(anyInt())).thenReturn(false);
        ManageChatsUseCase sut = new ManageChatsUseCaseImpl(chatDAService);

        assertThrows(NotFoundException.class, () -> {
            sut.removeChat(1);
        });
    }

}
