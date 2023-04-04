package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.usecases.ManageChatsUseCase;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/tg-chat")
@AllArgsConstructor
public class TelegramChatController {
    private ManageChatsUseCase manageChatsUseCase;
    @PostMapping("/{id}")
    public void registerChat(@PathVariable("id") int idChat){
        manageChatsUseCase.registryChat(idChat);
    }
    @DeleteMapping("/{id}")
    public void removeChat(@PathVariable("id") int idChat){
        manageChatsUseCase.removeChat(idChat);
    }
}
