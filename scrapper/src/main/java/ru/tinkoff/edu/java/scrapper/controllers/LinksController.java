package ru.tinkoff.edu.java.scrapper.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.usecases.ManageLinksUseCase;

@RestController
@RequestMapping("/links")
@AllArgsConstructor
public class LinksController {
    private static final String CHAT_HEADER = "Tg-Chat-Id";
    private ManageLinksUseCase manageLinksUseCase;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinksForChat(@RequestHeader(CHAT_HEADER) int idChat) {
        List<TrackedLink> linksForChat = manageLinksUseCase.findAllLinksByIdChat(idChat);
        ListLinksResponse listLinksResponse = new ListLinksResponse(linksForChat);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listLinksResponse);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLinkForChat(@RequestHeader(CHAT_HEADER) int idChat,
            @RequestBody() AddLinkRequest addLinkRequest) {
        TrackedLink addedTrackedLink = manageLinksUseCase.addLinkForChat(idChat, addLinkRequest);
        LinkResponse linkResponse = new LinkResponse(addedTrackedLink);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkResponse);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLinkFromChat(@RequestHeader(CHAT_HEADER) int idChat,
            @RequestBody RemoveLinkRequest removeLinkRequest) {
        TrackedLink addedTrackedLink = manageLinksUseCase.removeLinkFromChat(idChat, removeLinkRequest);
        LinkResponse linkResponse = new LinkResponse(addedTrackedLink);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkResponse);
    }
}
