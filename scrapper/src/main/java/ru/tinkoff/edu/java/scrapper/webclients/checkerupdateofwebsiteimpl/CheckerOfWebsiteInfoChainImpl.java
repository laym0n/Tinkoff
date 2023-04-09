package ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsiteimpl;

import lombok.AllArgsConstructor;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfo;
import java.security.InvalidParameterException;

@AllArgsConstructor
public class CheckerOfWebsiteInfoChainImpl implements CheckerOfWebsiteInfo {
    private CheckerOfWebsiteInfo myCheckerOfWebsiteInfo;
    private CheckerOfWebsiteInfo nextCheckerOfWebsiteInfo;

    @Override
    public WebsiteInfo getWebSiteInfoByLinkInfo(LinkInfo linkInfo) {
        WebsiteInfo result = myCheckerOfWebsiteInfo.getWebSiteInfoByLinkInfo(linkInfo);
        if(nextCheckerOfWebsiteInfo != null) {
            if(nextCheckerOfWebsiteInfo == null)
                throw new InvalidParameterException("Link " + linkInfo.getPath() +
                        " parsed as " + linkInfo.getDescriptionOfParsedLink() + " but can not be checked for get info");
            result = nextCheckerOfWebsiteInfo.getWebSiteInfoByLinkInfo(linkInfo);
        }
        return result;
    }
}
