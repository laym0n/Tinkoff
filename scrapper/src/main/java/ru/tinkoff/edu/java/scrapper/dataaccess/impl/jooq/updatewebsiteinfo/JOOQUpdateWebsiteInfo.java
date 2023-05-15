package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.updatewebsiteinfo;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;

@AllArgsConstructor
public abstract class JOOQUpdateWebsiteInfo<T extends ResultOfCompareWebsiteInfo>
    implements UpdateWebsiteInfoDAService<T> {
    private JOOQTrackedLinkDAO trackedLinkDAO;

    @Override
    public int[] getAllChatIdWithTrackedIdWebsiteInfo(int idWebsiteInfo) {
        return trackedLinkDAO.findAllChatsWithIdWebsiteInfo(idWebsiteInfo);
    }
}
