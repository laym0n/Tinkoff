package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.trackedlinkdaservice;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPATrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.TrackedLinkEntity;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPATrackedLinkDAService implements TrackedLinkDAService {
    private JPATrackedLinkDAO trackedLinkDAO;
    private JPAChainWebsiteInfoDAO webSiteInfoDAO;
    private JPAChatDAO chatDAO;

    @Override
    public List<TrackedLink> getAllTrackedLinksByChatId(int idChat) {
        List<TrackedLinkEntity> trackedLinkEntities = trackedLinkDAO.findAllByChatId(idChat);
        return trackedLinkEntities.stream().map(TrackedLinkEntity::getTrackedLink).toList();
    }

    @Override
    public boolean containsChatWithId(int chatId) {
        return chatDAO.containsChatWithId(chatId);
    }

    @Override
    public Optional<Integer> containsWebsiteInfoWithLinkInfo(LinkInfo linkInfo) {
        return webSiteInfoDAO.findIdByLinkInfo(linkInfo);
    }

    @Override
    public boolean containsTrackedLinkWithIdChatAndLinkInfo(int idChat, LinkInfo linkInfo) {
        return trackedLinkDAO.containsTrackedLinkWithChatIdAndLinkInfo(linkInfo, idChat);
    }

    @Override
    public TrackedLink createTrackedLink(TrackedLink trackedLink) {
        TrackedLinkEntity trackedLinkEntity = new TrackedLinkEntity(trackedLink);
        trackedLinkDAO.add(trackedLinkEntity);
        trackedLink.setId(trackedLinkEntity.getId());
        return trackedLink;
    }

    @Override
    public void createWebsiteInfo(WebsiteInfo websiteInfo) {
        webSiteInfoDAO.create(websiteInfo);
    }

    @Override
    public Optional<TrackedLink> deleteTrackedLinkByIdChatAndLinkInfo(int idChat, LinkInfo linkInfo) {
        Optional<TrackedLinkEntity> optionalTrackedLinkEntity = trackedLinkDAO.remove(linkInfo, idChat);
        if (optionalTrackedLinkEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(optionalTrackedLinkEntity.get().getTrackedLink());
    }
}
