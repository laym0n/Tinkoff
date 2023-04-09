package ru.tinkoff.edu.java.scrapper.dataaccess.impl.websiteinfodaservice;

import org.springframework.stereotype.Repository;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class TrackedLinksDAServiceImplLogger implements TrackedLinkDAService {
    private static Logger log = Logger.getLogger(TrackedLinksDAServiceImplLogger.class.getName());
    @Override
    public TrackedLink create(TrackedLink entity) {
        log.info("Create tracked link " + entity.toString());
        return entity;
    }

    @Override
    public Optional<TrackedLink> findById(Integer idEntity) {
        log.info("Find tracked link with id " + idEntity);
        return Optional.of(new TrackedLink(idEntity,
                new GitHubLinkInfo("drownedtears", "forum"),
                5, OffsetDateTime.now()));
    }

    @Override
    public void update(TrackedLink entity) {
        log.info("Update tracked link " + entity.toString());
    }

    @Override
    public void delete(Integer idEntity) {
        log.info("Delete tracked link with id " + idEntity);
    }

    @Override
    public Optional<TrackedLink> findTrackedLinkByIdChatAndLinkInfo(int idChat, LinkInfo linkInfoForRemove) {
        log.info("Find tracked link by WebsiteInfo " + linkInfoForRemove.getDescriptionOfParsedLink());
        return Optional.of(new TrackedLink(5,
                linkInfoForRemove,
                5, OffsetDateTime.now()));
    }

    @Override
    public List<TrackedLink> getAllTrackedLinksByChatId(int idChat) {
        log.info("Get all links from chat with id " + idChat);
        return List.of(new TrackedLink(2,
                new GitHubLinkInfo("drownedtears", "forum"),
                idChat, OffsetDateTime.now()));
    }

    @Override
    public boolean containsChatWithId(int chatId) {
        log.info("Check exist Chat with id " + chatId);
        return true;
    }

    @Override
    public boolean containsWebsiteInfoWithLinkInfo(int trackedLinkId) {
        log.info("Check exist TrackedLink with id " + trackedLinkId);
        return true;
    }
}
