package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import java.util.Optional;

@AllArgsConstructor
public class JDBCChainStackOverflowInfoDAOImpl implements JDBCChainWebsiteInfoDAO {
    private JDBCStackOverflowInfoDAO stackOverflowDAO;
    private JDBCChainWebsiteInfoDAO nextChain;

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if(!(newWebsiteInfo instanceof StackOverflowInfo)){
            if(nextChain != null)
                nextChain.create(newWebsiteInfo);
            return;
        }
        stackOverflowDAO.add((StackOverflowInfo) newWebsiteInfo);
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof StackOverflowLinkInfo))
            return nextChain == null ? null : nextChain.findIdByLinkInfo(linkInfo);
        return stackOverflowDAO.findIdByLinkInfo((StackOverflowLinkInfo) linkInfo);
    }

    @Override
    public WebsiteInfo loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo) {
        if(!websiteInfoType.equals("StackOverflow")){
            return nextChain == null ? null : nextChain.loadWebsiteInfo(websiteInfoType, idWebsiteInfo);
        }
        return stackOverflowDAO.getById(idWebsiteInfo);
    }

    @Override
    public LinkInfo loadLinkInfoForWebsiteById(int idWebsiteInfo, String websiteType) {
        if(!websiteType.equals("StackOverflow")){
            return nextChain == null ? null : nextChain.loadLinkInfoForWebsiteById(idWebsiteInfo, websiteType);
        }
        return stackOverflowDAO.loadLinkInfo(idWebsiteInfo);
    }
}
