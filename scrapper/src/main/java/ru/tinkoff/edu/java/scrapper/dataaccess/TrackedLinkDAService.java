package ru.tinkoff.edu.java.scrapper.dataaccess;

import parserservice.dto.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

import java.util.List;
import java.util.Optional;

public interface TrackedLinkDAService extends CRUDService<TrackedLink, Integer> {
    Optional<TrackedLink> findTrackedLinkByWebsiteInfoAndIdChat(int idChat, WebsiteInfo websiteInfoForRemove);
    List<TrackedLink> getAllTrackedLinksByChatId(int idChat);
    boolean containsChatWithId(int chatId);
    boolean containsTrackedLinkWithId(int trackedLinkId);
}
