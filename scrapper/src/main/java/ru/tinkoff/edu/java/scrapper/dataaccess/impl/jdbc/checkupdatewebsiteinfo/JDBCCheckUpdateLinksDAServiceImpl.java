package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.checkupdatewebsiteinfo;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.CheckUpdateLinksDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.List;

@AllArgsConstructor
public class JDBCCheckUpdateLinksDAServiceImpl implements CheckUpdateLinksDAService {
    private JDBCWebsiteInfoDAO websiteInfoDAO;
    @Override
    public List<WebsiteInfo> getListWebsiteInfoByLastCheckTime(int count) {
        return websiteInfoDAO.loadWebsiteInfoWithTheEarliestUpdateTime(count);
    }
}
