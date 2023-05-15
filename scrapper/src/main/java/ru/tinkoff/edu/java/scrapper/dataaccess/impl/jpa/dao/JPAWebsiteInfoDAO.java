package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.WebsiteInfoEntity;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAWebsiteInfoDAO extends JPADAO {
    private JPAChainWebsiteInfoDAO chainWebsiteInfoDAO;

    public JPAWebsiteInfoDAO(JPAChainWebsiteInfoDAO chainWebsiteInfoDAO) {
        this.chainWebsiteInfoDAO = chainWebsiteInfoDAO;
    }

    public List<WebsiteInfoEntity> loadWebsiteInfoWithTheEarliestUpdateTime(int count) {
        List<WebsiteInfoEntity> abstractInfos = entityManager
                .createQuery("select wi from WebsiteInfoEntity wi "
                    + "order by wi.lastCheckUpdate asc, wi.id asc ")
                .setMaxResults(count).getResultList();
        return abstractInfos.stream()
            .map(i -> chainWebsiteInfoDAO.loadWebsiteInfo(i.getTypeOfWebsite().getName(), i.getId())).toList();
    }

    public void remove(int idWebsiteInfo) {
        entityManager.createQuery("delete from WebsiteInfoEntity wi where wi.id = :id")
                .setParameter("id", idWebsiteInfo).executeUpdate();
    }
}
