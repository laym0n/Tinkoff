package ru.tinkoff.edu.java.bot;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.webclients.ScrapperClient;
import ru.tinkoff.edu.java.bot.webclients.ScrapperClientImpl;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        ScrapperClientImpl scrapperClient = new ScrapperClientImpl("http://localhost:8082");
        scrapperClient.removeChat(1);
    }
}
