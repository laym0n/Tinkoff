package ru.tinkoff.edu.java.scrapper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

import java.net.URI;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinkResponse {
    private int id;
    private URI uri;
    public LinkResponse(TrackedLink trackedLink){
        id = trackedLink.getId();
        uri = trackedLink.getURI();
    }
}
