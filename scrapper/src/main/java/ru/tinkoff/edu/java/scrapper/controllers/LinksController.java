package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;

@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinksForChat(@RequestHeader("Tg-Chat-Id") int id){
        ListLinksResponse listLinksResponse = new ListLinksResponse();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listLinksResponse);
    }
    @PostMapping
    public ResponseEntity<LinkResponse> addLinkForChat(@RequestHeader("Tg-Chat-Id") int id, @RequestBody() AddLinkRequest addLinkRequest){

        LinkResponse linkResponse = new LinkResponse();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkResponse);
    }
    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLinkFromChat(@RequestHeader("Tg-Chat-Id") int id, @RequestBody RemoveLinkRequest removeLinkRequest){
        LinkResponse linkResponse = new LinkResponse();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkResponse);
    }
}
