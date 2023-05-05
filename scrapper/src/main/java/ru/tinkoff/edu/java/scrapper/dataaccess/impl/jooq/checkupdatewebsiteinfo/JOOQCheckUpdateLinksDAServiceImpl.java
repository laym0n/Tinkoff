package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.checkupdatewebsiteinfo;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.CheckUpdateLinksDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@AllArgsConstructor
public class JOOQCheckUpdateLinksDAServiceImpl implements CheckUpdateLinksDAService {
    private JOOQWebsiteInfoDAO websiteInfoDAO;

    @Override
    public List<WebsiteInfo> getListWebsiteInfoByLastCheckTime(int count) {
        return websiteInfoDAO.loadWebsiteInfoWithTheEarliestUpdateTime(count);
    }
}
