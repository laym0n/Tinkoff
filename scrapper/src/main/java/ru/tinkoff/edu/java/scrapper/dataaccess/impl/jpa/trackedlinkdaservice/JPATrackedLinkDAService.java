package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.trackedlinkdaservice;

import lombok.AllArgsConstructor;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPATrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.TrackedLinkEntity;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
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
    public boolean containsTrackedLinkWithIdChatAndLinkInfo(int idChat, LinkInfo linkInfoForRemove) {
        return trackedLinkDAO.containsTrackedLinkWithChatIdAndLinkInfo(linkInfoForRemove, idChat);
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
        return trackedLinkDAO.remove(linkInfo, idChat);
    }
}
