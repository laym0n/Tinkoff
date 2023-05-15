package ru.tinkoff.edu.java.scrapper.dataaccess;

import java.util.List;
import java.util.Optional;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface TrackedLinkDAService {
    List<TrackedLink> getAllTrackedLinksByChatId(int idChat);

    boolean containsChatWithId(int chatId);

    Optional<Integer> containsWebsiteInfoWithLinkInfo(LinkInfo linkInfo);

    boolean containsTrackedLinkWithIdChatAndLinkInfo(int idChat, LinkInfo linkInfoForRemove);

    TrackedLink createTrackedLink(TrackedLink trackedLink);

    void createWebsiteInfo(WebsiteInfo websiteInfo);

    Optional<TrackedLink> deleteTrackedLinkByIdChatAndLinkInfo(int idChat, LinkInfo linkInfo);

}
