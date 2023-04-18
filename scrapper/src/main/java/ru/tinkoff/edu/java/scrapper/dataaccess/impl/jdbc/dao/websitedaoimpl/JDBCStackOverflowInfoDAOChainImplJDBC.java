package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websitedaoimpl;

import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import java.util.Optional;

public class JDBCStackOverflowInfoDAOChainImplJDBC implements JDBCWebsiteInfoDAO {
    private JDBCStackOverflowInfoDAO stackOverflowDAO;
    private JDBCWebsiteInfoDAO nextChain;
    @Override
    public String getQueryForFindIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof GitHubLinkInfo))
            return nextChain.getQueryForFindIdByLinkInfo(linkInfo);
        return stackOverflowDAO.getQueryForFindIdByLinkInfo((StackOverflowLinkInfo) linkInfo);
    }

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if(!(newWebsiteInfo instanceof StackOverflowInfo)){
            nextChain.create(newWebsiteInfo);
            return;
        }
        stackOverflowDAO.add((StackOverflowInfo) newWebsiteInfo);
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof GitHubLinkInfo))
            return nextChain.findIdByLinkInfo(linkInfo);
        return stackOverflowDAO.findIdByLinkInfo((StackOverflowLinkInfo) linkInfo);
    }
}
