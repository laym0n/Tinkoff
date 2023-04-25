package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

public interface JPAChainWebsiteInfoDAO {
    void create(WebsiteInfo newWebsiteInfo);
    Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo);
    WebsiteInfo loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo);
    LinkInfo loadLinkInfoForWebsiteById(int idWebsiteInfo, String websiteType);
}
