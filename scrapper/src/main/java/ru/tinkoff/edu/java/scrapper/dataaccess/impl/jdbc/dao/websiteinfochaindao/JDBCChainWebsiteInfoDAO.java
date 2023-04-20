package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

public interface JDBCChainWebsiteInfoDAO {
    String getQueryForFindIdByLinkInfo(LinkInfo linkInfo);
    void create(WebsiteInfo newWebsiteInfo);
    Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo);
    WebsiteInfo loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo);
}
