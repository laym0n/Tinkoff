package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.usecases.ManageLinksUseCase;

import java.util.List;

@RestController
@RequestMapping("/links")
@AllArgsConstructor
public class LinksController {
    private ManageLinksUseCase manageLinksUseCase;
    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinksForChat(@RequestHeader("Tg-Chat-Id") int idChat){
        List<TrackedLink> linksForChat = manageLinksUseCase.findAllLinksByIdChat(idChat);
        ListLinksResponse listLinksResponse = new ListLinksResponse(linksForChat);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listLinksResponse);
    }
    @PostMapping
    public ResponseEntity<LinkResponse> addLinkForChat(@RequestHeader("Tg-Chat-Id") int idChat, @RequestBody() AddLinkRequest addLinkRequest){
        TrackedLink addedTrackedLink = manageLinksUseCase.addLinkForChat(idChat, addLinkRequest);
        LinkResponse linkResponse = new LinkResponse(addedTrackedLink);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkResponse);
    }
    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLinkFromChat(@RequestHeader("Tg-Chat-Id") int idChat, @RequestBody RemoveLinkRequest removeLinkRequest){
        TrackedLink addedTrackedLink = manageLinksUseCase.removeLinkFromChat(idChat, removeLinkRequest);
        LinkResponse linkResponse = new LinkResponse(addedTrackedLink);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkResponse);
    }
}
