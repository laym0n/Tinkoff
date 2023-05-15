package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.usecases.ManageChatsUseCase;

@RestController
@RequestMapping("/tg-chat")
@AllArgsConstructor
public class TelegramChatController {
    private static final String MAPPING_TO_CONTROLLER = "/{id}";
    private static final String PATH_VARIABLE_FOR_ID = "id";
    private ManageChatsUseCase manageChatsUseCase;

    @PostMapping(MAPPING_TO_CONTROLLER)
    public void registerChat(@PathVariable(PATH_VARIABLE_FOR_ID) int idChat) {
        manageChatsUseCase.registryChat(idChat);
    }

    @DeleteMapping(MAPPING_TO_CONTROLLER)
    public void removeChat(@PathVariable(PATH_VARIABLE_FOR_ID) int idChat) {
        manageChatsUseCase.removeChat(idChat);
    }
}
