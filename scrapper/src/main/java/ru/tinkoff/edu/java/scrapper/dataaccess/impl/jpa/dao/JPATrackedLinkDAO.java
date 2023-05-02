package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.TrackedLinkEntity;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPATrackedLinkDAO extends JPADAO {
    private static final String WEBSITE_ID_VARIABLE_NAME = "websiteInfoId";
    private static final String CHAT_ID_VARIABLE_NAME = "chatId";
    private JPAChainWebsiteInfoDAO websiteInfoDAO;

    public JPATrackedLinkDAO(JPAChainWebsiteInfoDAO websiteInfoDAO) {
        this.websiteInfoDAO = websiteInfoDAO;
    }

    @Transactional
    public void add(TrackedLinkEntity newTrackedLink) {
        entityManager.persist(newTrackedLink);
    }

    public Optional<TrackedLinkEntity> remove(LinkInfo trackedLink, int idChat) {
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(trackedLink);
        if (idWebsiteInfo.isEmpty()) {
            return Optional.empty();
        }
        List<TrackedLinkEntity> listRemovedLink = entityManager
            .createQuery(
                "select tl from TrackedLinkEntity tl "
                    + "where tl.chatId = :chatId and "
                    + "tl.websiteInfoId = :websiteInfoId")
            .setParameter(WEBSITE_ID_VARIABLE_NAME, idWebsiteInfo.get())
            .setParameter(CHAT_ID_VARIABLE_NAME, idChat)
            .getResultList();
        if (listRemovedLink.isEmpty()) {
            return Optional.empty();
        }
        entityManager.remove(listRemovedLink.get(0));
        return Optional.of(listRemovedLink.get(0));
    }

    public boolean containsTrackedLinkWithChatIdAndLinkInfo(LinkInfo linkInfo, int idChat) {
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(linkInfo);
        if (idWebsiteInfo.isEmpty()) {
            return false;
        }
        long count = entityManager.createQuery("select count(*) from TrackedLinkEntity tl "
                + "where tl.chatId = :chatId and tl.websiteInfoId = :websiteInfoId", Long.class)
                .setParameter(CHAT_ID_VARIABLE_NAME, idChat)
                .setParameter(WEBSITE_ID_VARIABLE_NAME, idWebsiteInfo.get())
                .getSingleResult();
        return count > 0L;
    }

    public int[] findAllChatsWithIdWebsiteInfo(int idWebsiteInfo) {
        List<Integer> ids = entityManager
                .createQuery("select tl.chatId from TrackedLinkEntity tl where tl.websiteInfoId = :websiteInfoId")
                .setParameter(WEBSITE_ID_VARIABLE_NAME, idWebsiteInfo)
                .getResultList();
        return ids.stream().flatMapToInt(IntStream::of).toArray();

    }

    public List<TrackedLinkEntity> findAllByChatId(int idChat) {
        return entityManager.createQuery("select tl from TrackedLinkEntity tl where tl.chatId = :chatId")
                .setParameter(CHAT_ID_VARIABLE_NAME, idChat)
                .getResultList();
    }
}
