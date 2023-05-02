package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import java.util.Optional;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.WebsiteInfoEntity;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface JPAChainWebsiteInfoDAO {
    void create(WebsiteInfo newWebsiteInfo);

    Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo);

    WebsiteInfoEntity loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo);

    LinkInfo loadLinkInfoForWebsiteById(int idWebsiteInfo, String websiteType);
}
