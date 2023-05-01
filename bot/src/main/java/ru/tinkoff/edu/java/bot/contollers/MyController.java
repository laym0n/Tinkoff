package ru.tinkoff.edu.java.bot.contollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.response.LinkUpdateResponse;
import ru.tinkoff.edu.java.bot.telegrambotimpl.LinkUpdateCheckerBot;
import ru.tinkoff.edu.java.bot.usecases.SendUpdateLinkUseCase;

@RestController
@AllArgsConstructor
public class MyController {
    private SendUpdateLinkUseCase sendUpdateLinkUseCase;
    @PostMapping("/updates")
    public void updateLink(@RequestBody() LinkUpdateResponse request){
        sendUpdateLinkUseCase.sendUpdateLink(request);
    }
}
