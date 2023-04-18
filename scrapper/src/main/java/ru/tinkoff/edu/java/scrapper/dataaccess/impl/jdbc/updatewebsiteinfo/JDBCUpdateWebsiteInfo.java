package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.updatewebsiteinfo;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;

@AllArgsConstructor
public abstract class JDBCUpdateWebsiteInfo<T extends ResultOfCompareWebsiteInfo> implements UpdateWebsiteInfoDAService<T> {
    private JDBCTrackedLinkDAO trackedLinkDAO;

    @Override
    public int[] getAllChatIdWithTrackedIdWebsiteInfo(int idWebsiteInfo) {
        return trackedLinkDAO.findAllChatsWithIdWebsiteInfo(idWebsiteInfo);
    }
}
