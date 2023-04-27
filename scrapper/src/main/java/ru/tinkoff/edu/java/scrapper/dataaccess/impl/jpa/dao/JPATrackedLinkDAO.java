package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.TrackedLinkEntity;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
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
//        entityManager.createQuery("insert into TrackedLinkEntity ?").executeUpdate();
//
//        newTrackedLink.setId(idAddedTrackedLink);
    }
    public Optional<TrackedLink> remove(LinkInfo trackedLink, int idChat){
//        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(trackedLink);
//        if(idWebsiteInfo.isEmpty())
//            return Optional.empty();
//        String sql = "select id from tracked_link where chat_id = ? and website_info_id = ?;";
//        Object[] params = new Object[] { idChat, idWebsiteInfo.get() };
//
//        List<Integer> idsDeleted = jdbcTemplate.query(sql, (rs, rowNum) -> {
//            return rs.getInt("id");
//        }, params);
//
//        TrackedLink deletedEntity = null;
//        if (!idsDeleted.isEmpty()) {
//            deletedEntity = new TrackedLink(idsDeleted.get(0), idChat, idWebsiteInfo.get(), trackedLink);
//            jdbcTemplate.update("delete from tracked_link where chat_id = ? and website_info_id = ?;", params);
//        }
//        return Optional.ofNullable(deletedEntity);
        return Optional.empty();
    }
    public boolean containsTrackedLinkWithChatIdAndLinkInfo(LinkInfo linkInfo, int idChat){
//        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(linkInfo);
//        if(idWebsiteInfo.isEmpty())
//            return false;
//        Object[] params = new Object[] { idChat, idWebsiteInfo.get() };
//        int count = jdbcTemplate.queryForObject("select count(*) from tracked_link " +
//                "where chat_id = ? and website_info_id = ?", Integer.class, params);
//        return count > 0;
        return true;
    }
    public int[] findAllChatsWithIdWebsiteInfo(int idWebsiteInfo){
        List<Integer> ids = entityManager
                .createQuery("select tl.chatId from TrackedLinkEntity tl where tl.websiteInfoId = ?")
                .getResultList();
        return ids.stream().flatMapToInt(i->IntStream.of(i)).toArray();

    }
    public List<TrackedLinkEntity> findAllByChatId(int idChat){
        return entityManager.createQuery("select * from TrackedLinkEntity tl where tl.chatId = ?").getResultList();
    }
}
