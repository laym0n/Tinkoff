package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import parserservice.dto.LinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.StackOverflowInfoEntity;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.WebsiteInfoEntity;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.util.Optional;

@AllArgsConstructor
public class JPAChainStackOverflowInfoDAOImpl implements JPAChainWebsiteInfoDAO {
    private JPAStackOverflowInfoDAO stackOverflowDAO;
    private JPAChainWebsiteInfoDAO nextChain;

    @Override
    public void create(WebsiteInfo newWebsiteInfo) {
        if(!(newWebsiteInfo instanceof StackOverflowInfo)){
            if(nextChain != null)
                nextChain.create(newWebsiteInfo);
            return;
        }
        StackOverflowInfoEntity newEntity = new StackOverflowInfoEntity((StackOverflowInfo) newWebsiteInfo);
        stackOverflowDAO.add(newEntity);
        newWebsiteInfo.setId(newEntity.getId());
    }

    @Override
    public Optional<Integer> findIdByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof StackOverflowLinkInfo))
            return nextChain == null ? Optional.empty() : nextChain.findIdByLinkInfo(linkInfo);
        return stackOverflowDAO.findIdByLinkInfo((StackOverflowLinkInfo) linkInfo);
    }

    @Override
    public WebsiteInfoEntity loadWebsiteInfo(String websiteInfoType, int idWebsiteInfo) {
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
