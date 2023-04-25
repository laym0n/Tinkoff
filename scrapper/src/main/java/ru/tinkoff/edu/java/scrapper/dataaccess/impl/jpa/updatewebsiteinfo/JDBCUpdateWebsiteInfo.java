package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.updatewebsiteinfo;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JDBCTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;

@AllArgsConstructor
public abstract class JDBCUpdateWebsiteInfo<T extends ResultOfCompareWebsiteInfo> implements UpdateWebsiteInfoDAService<T> {
    private JDBCTrackedLinkDAO trackedLinkDAO;

    @Override
    public int[] getAllChatIdWithTrackedIdWebsiteInfo(int idWebsiteInfo) {
        return trackedLinkDAO.findAllChatsWithIdWebsiteInfo(idWebsiteInfo);
    }
}
