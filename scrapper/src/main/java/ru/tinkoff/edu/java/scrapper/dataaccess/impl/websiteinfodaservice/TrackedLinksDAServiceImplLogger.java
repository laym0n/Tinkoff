package ru.tinkoff.edu.java.scrapper.dataaccess.impl.websiteinfodaservice;

import org.springframework.stereotype.Repository;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class TrackedLinksDAServiceImplLogger implements TrackedLinkDAService {
    private static Logger log = Logger.getLogger(TrackedLinksDAServiceImplLogger.class.getName());

    @Override
    public List<TrackedLink> getAllTrackedLinksByChatId(int idChat) {
        return null;
    }

    @Override
    public boolean containsChatWithId(int chatId) {
        return false;
    }

    @Override
    public boolean containsWebsiteInfoWithLinkInfo(LinkInfo linkInfo) {
        return false;
    }

    @Override
    public boolean containsTrackedLinkWithIdChatAndLinkInfo(int idChat, LinkInfo linkInfoForRemove) {
        return false;
    }

    @Override
    public TrackedLink createTrackedLink(TrackedLink trackedLink) {
        return null;
    }

    @Override
    public void createWebsiteInfo(WebsiteInfo websiteInfo) {

    }

    @Override
    public Optional<TrackedLink> deleteTrackedLinkByIdChatAndLinkInfo(int idChat, LinkInfo linkInfo) {
        return Optional.empty();
    }
}
