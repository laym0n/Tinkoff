package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.GitHubInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.WebsiteInfoEntity;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

@AllArgsConstructor
public class JPAChainGitHubInfoDAOImpl implements JPAChainWebsiteInfoDAO {
    private JPAGitHubInfoDAO gitHubInfoDAO;
    private JPAChainWebsiteInfoDAO nextChain;

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if(!(newWebsiteInfo instanceof GitHubInfo)) {
            if(nextChain != null)
                nextChain.create(newWebsiteInfo);
            return;
        }
        GitHubInfoEntity newInfoEntity = new GitHubInfoEntity((GitHubInfo) newWebsiteInfo);
        gitHubInfoDAO.add(newInfoEntity);
        newWebsiteInfo.setId(newInfoEntity.getId());
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof GitHubLinkInfo))
            return nextChain == null ? null : nextChain.findIdByLinkInfo(linkInfo);
        GitHubLinkInfo gitHubLinkInfo = (GitHubLinkInfo) linkInfo;
        return gitHubInfoDAO.findIdByUserNameAndRepositoryName(gitHubLinkInfo.userName(), gitHubLinkInfo.repositoryName());
    }
    @Override
    public WebsiteInfoEntity loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo){
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
