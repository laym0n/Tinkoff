package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao;

import java.util.Optional;
import lombok.AllArgsConstructor;
import parserservice.dto.LinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

@AllArgsConstructor
public class JDBCChainStackOverflowInfoDAOImpl implements JDBCChainWebsiteInfoDAO {
    private static final String NAME_OF_WEBSITE_FOR_STACKOVERFLOW = "StackOverflow";
    private JDBCStackOverflowInfoDAO stackOverflowDAO;
    private JDBCChainWebsiteInfoDAO nextChain;

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if (!(newWebsiteInfo instanceof StackOverflowInfo)) {
            if (nextChain != null) {
                nextChain.create(newWebsiteInfo);
            }
            return;
        }
        stackOverflowDAO.add((StackOverflowInfo) newWebsiteInfo);
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if (!(linkInfo instanceof StackOverflowLinkInfo)) {
            return nextChain == null ? Optional.empty() : nextChain.findIdByLinkInfo(linkInfo);
        }
        return stackOverflowDAO.findIdByLinkInfo((StackOverflowLinkInfo) linkInfo);
    }

    @Override
    public WebsiteInfo loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo) {
        if (!websiteInfoType.equals(NAME_OF_WEBSITE_FOR_STACKOVERFLOW)) {
            return nextChain == null ? null : nextChain.loadWebsiteInfo(websiteInfoType, idWebsiteInfo);
        }
        return stackOverflowDAO.getById(idWebsiteInfo);
    }

    @Override
    public LinkInfo loadLinkInfoForWebsiteById(int idWebsiteInfo, String websiteType) {
        if (!websiteType.equals(NAME_OF_WEBSITE_FOR_STACKOVERFLOW)) {
            return nextChain == null ? null : nextChain.loadLinkInfoForWebsiteById(idWebsiteInfo, websiteType);
        }
        return stackOverflowDAO.loadLinkInfo(idWebsiteInfo);
    }
}
