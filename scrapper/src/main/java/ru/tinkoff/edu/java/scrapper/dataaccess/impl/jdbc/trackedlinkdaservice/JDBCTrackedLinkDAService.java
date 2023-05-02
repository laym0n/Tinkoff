package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.trackedlinkdaservice;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCTrackedLinkDAService implements TrackedLinkDAService {
    private JDBCTrackedLinkDAO trackedLinkDAO;
    private JDBCChainWebsiteInfoDAO webSiteInfoDAO;
    private JDBCChatDAO chatDAO;

    @Override
    public List<TrackedLink> getAllTrackedLinksByChatId(int idChat) {
        return trackedLinkDAO.findAllByChatId(idChat);
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
        trackedLinkDAO.add(trackedLink);
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
