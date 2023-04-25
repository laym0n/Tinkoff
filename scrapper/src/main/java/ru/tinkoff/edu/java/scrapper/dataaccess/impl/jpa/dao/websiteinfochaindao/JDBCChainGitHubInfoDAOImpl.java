package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JDBCGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

@AllArgsConstructor
public class JDBCChainGitHubInfoDAOImpl implements JDBCChainWebsiteInfoDAO {
    private JDBCGitHubInfoDAO gitHubInfoDAO;
    private JDBCChainWebsiteInfoDAO nextChain;

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if(!(newWebsiteInfo instanceof GitHubInfo)) {
            if(nextChain != null)
                nextChain.create(newWebsiteInfo);
            return;
        }
        gitHubInfoDAO.add((GitHubInfo) newWebsiteInfo);
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof GitHubLinkInfo))
            return nextChain == null ? null : nextChain.findIdByLinkInfo(linkInfo);
        return gitHubInfoDAO.findIdByLinkInfo((GitHubLinkInfo) linkInfo);
    }
    @Override
    public WebsiteInfo loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo){
        if(!websiteInfoType.equals("GitHub")){
            return nextChain == null ? null : nextChain.loadWebsiteInfo(websiteInfoType, idWebsiteInfo);
        }
        return gitHubInfoDAO.getById(idWebsiteInfo);
    }

    @Override
    public LinkInfo loadLinkInfoForWebsiteById(int idWebsiteInfo, String websiteType) {
        if(!websiteType.equals("GitHub")){
            return nextChain == null ? null : nextChain.loadLinkInfoForWebsiteById(idWebsiteInfo, websiteType);
        }
        return gitHubInfoDAO.getLinkInfoById(idWebsiteInfo);
    }
}
