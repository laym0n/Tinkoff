package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/tg-chat")
public class TelegramChatController {
    @PostMapping("/{id}")
    public void registerChat(@PathVariable("id") int id){
        throw new InvalidParameterException("Invalid request parameters");
    }
    @DeleteMapping("/{id}")
    public void removeChat(@PathVariable("id") int id){
        throw new NotFoundException("The chat does not exist");
    }
}
