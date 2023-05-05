package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.websiteinfochaindao;

import java.util.Optional;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

public interface JOOQChainWebsiteInfoDAO {
    void create(WebsiteInfo newWebsiteInfo);

    Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo);

    WebsiteInfo loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo);

    LinkInfo loadLinkInfoForWebsiteById(int idWebsiteInfo, String websiteType);
}
