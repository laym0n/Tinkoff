package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.checkupdatewebsiteinfo;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.CheckUpdateLinksDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.List;

@AllArgsConstructor
public class JPACheckUpdateLinksDAServiceImpl implements CheckUpdateLinksDAService {
    private JPAWebsiteInfoDAO websiteInfoDAO;
    @Override
    public List<WebsiteInfo> getListWebsiteInfoByLastCheckTime(int count) {
        return websiteInfoDAO.loadWebsiteInfoWithTheEarliestUpdateTime(count);
    }
}
