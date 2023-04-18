package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websitedaoimpl;

import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

public class JDBCGitHubInfoDAOChainImplJDBC implements JDBCWebsiteInfoDAO {
    private JDBCGitHubInfoDAO gitHubInfoDAO;
    private JDBCWebsiteInfoDAO nextChain;
    @Override
    public String getQueryForFindIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof GitHubLinkInfo))
            return nextChain.getQueryForFindIdByLinkInfo(linkInfo);
        return gitHubInfoDAO.getQueryForFindIdByLinkInfo((GitHubLinkInfo) linkInfo);
    }

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if(!(newWebsiteInfo instanceof GitHubInfo)) {
            nextChain.create(newWebsiteInfo);
            return;
        }
        gitHubInfoDAO.add((GitHubInfo) newWebsiteInfo);
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof GitHubLinkInfo))
            return nextChain.findIdByLinkInfo(linkInfo);
        return gitHubInfoDAO.findIdByLinkInfo((GitHubLinkInfo) linkInfo);
    }
}
