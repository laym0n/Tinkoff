package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.TrackedLinkEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPATrackedLinkDAO extends JPADAO {
    private JPAChainWebsiteInfoDAO websiteInfoDAO;

    public JPATrackedLinkDAO(JPAChainWebsiteInfoDAO websiteInfoDAO) {
        this.websiteInfoDAO = websiteInfoDAO;
    }

    public void add(TrackedLinkEntity newTrackedLink){
        entityManager.persist(newTrackedLink);
    }
    public Optional<TrackedLinkEntity> remove(LinkInfo trackedLink, int idChat){
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(trackedLink);
        if(idWebsiteInfo.isEmpty())
            return Optional.empty();
        List<TrackedLinkEntity> listRemovedLink = entityManager.createQuery("select tl from TrackedLinkEntity tl " +
                "where tl.chatId = :chatId and " +
                "tl.websiteInfoId = :websiteInfoId")
                .setParameter("websiteInfoId", idWebsiteInfo.get())
                .setParameter("chatId", idChat)
                .getResultList();
        if(listRemovedLink.isEmpty())
            return Optional.empty();
        entityManager.remove(listRemovedLink.get(0));
        return Optional.of(listRemovedLink.get(0));
    }
    public boolean containsTrackedLinkWithChatIdAndLinkInfo(LinkInfo linkInfo, int idChat){
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(linkInfo);
        if(idWebsiteInfo.isEmpty())
            return false;
        long count = entityManager.createQuery("select count(*) from TrackedLinkEntity tl " +
                "where tl.chatId = :chatId and tl.websiteInfoId = :websiteInfoId", Long.class)
                .setParameter("chatId", idChat)
                .setParameter("websiteInfoId", idWebsiteInfo.get())
                .getSingleResult();
        return count > 0l;
    }
    public int[] findAllChatsWithIdWebsiteInfo(int idWebsiteInfo){
        List<Integer> ids = entityManager
                .createQuery("select tl.chatId from TrackedLinkEntity tl where tl.websiteInfoId = :websiteInfoId")
                .setParameter("websiteInfoId", idWebsiteInfo)
                .getResultList();
        return ids.stream().flatMapToInt(IntStream::of).toArray();

    }
    public List<TrackedLinkEntity> findAllByChatId(int idChat){
        return entityManager.createQuery("select tl from TrackedLinkEntity tl where tl.chatId = :chatId")
                .setParameter("chatId", idChat)
                .getResultList();
    }
}
