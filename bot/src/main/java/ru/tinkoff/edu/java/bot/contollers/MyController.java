package ru.tinkoff.edu.java.bot.contollers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.response.LinkUpdateResponse;
import ru.tinkoff.edu.java.bot.telegrambotimpl.LinkUpdateCheckerBot;

@RestController
public class MyController {
    private LinkUpdateCheckerBot myBot;
    @PostMapping("/updates")
    public void updateLink(@RequestBody() LinkUpdateResponse request){
        myBot.sendUpdateMessages(request);
    }
}
