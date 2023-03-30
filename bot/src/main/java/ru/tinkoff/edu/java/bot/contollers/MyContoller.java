package ru.tinkoff.edu.java.bot.contollers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

import java.security.InvalidParameterException;

@RestController
public class MyContoller {

    @PostMapping("/updates")
    public void updateLink(@RequestBody() LinkUpdateRequest request){
        throw new InvalidParameterException("Invalid request parameters");

    }
}
