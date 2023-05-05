package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.websiteinfochaindao;

import java.util.Optional;
import lombok.AllArgsConstructor;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

@AllArgsConstructor
public class JOOQChainGitHubInfoDAOImpl implements JOOQChainWebsiteInfoDAO {
    private static final String NAME_OF_WEBSITE_FOR_GIT_HUB = "GitHub";
    private JOOQGitHubInfoDAO gitHubInfoDAO;
    private JOOQChainWebsiteInfoDAO nextChain;

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if (!(newWebsiteInfo instanceof GitHubInfo)) {
            if (nextChain != null) {
                nextChain.create(newWebsiteInfo);
            }
            return;
        }
        gitHubInfoDAO.add((GitHubInfo) newWebsiteInfo);
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if (!(linkInfo instanceof GitHubLinkInfo)) {
            return nextChain == null ? Optional.empty() : nextChain.findIdByLinkInfo(linkInfo);
        }
        return gitHubInfoDAO.findIdByLinkInfo((GitHubLinkInfo) linkInfo);
    }

    @Override
    public WebsiteInfo loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo) {
        if (!websiteInfoType.equals(NAME_OF_WEBSITE_FOR_GIT_HUB)) {
            return nextChain == null ? null : nextChain.loadWebsiteInfo(websiteInfoType, idWebsiteInfo);
        }
        return gitHubInfoDAO.getById(idWebsiteInfo).get();
    }

    @Override
    public LinkInfo loadLinkInfoForWebsiteById(int idWebsiteInfo, String websiteType) {
        if (!websiteType.equals(NAME_OF_WEBSITE_FOR_GIT_HUB)) {
            return nextChain == null ? null : nextChain.loadLinkInfoForWebsiteById(idWebsiteInfo, websiteType);
        }
        return gitHubInfoDAO.getLinkInfoById(idWebsiteInfo);
    }
}
