package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

public interface JDBCWebsiteInfoDAO {
    String getQueryForFindIdByLinkInfo(LinkInfo linkInfo);
    void create(WebsiteInfo newWebsiteInfo);
    Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo);
}
