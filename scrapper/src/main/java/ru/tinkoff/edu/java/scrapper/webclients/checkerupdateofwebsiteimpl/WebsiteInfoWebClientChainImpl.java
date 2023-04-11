package ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsiteimpl;

import lombok.AllArgsConstructor;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.webclients.WebsiteInfoWebClient;
import java.security.InvalidParameterException;

@AllArgsConstructor
public class WebsiteInfoWebClientChainImpl implements WebsiteInfoWebClient {
    private WebsiteInfoWebClient myWebsiteInfoWebClient;
    private WebsiteInfoWebClient nextWebsiteInfoWebClient;

    @Override
    public WebsiteInfo getWebSiteInfoByLinkInfo(LinkInfo linkInfo) {
        WebsiteInfo result = myWebsiteInfoWebClient.getWebSiteInfoByLinkInfo(linkInfo);
        if(nextWebsiteInfoWebClient != null) {
            if(nextWebsiteInfoWebClient == null)
                throw new InvalidParameterException("Link " + linkInfo.getPath() +
                        " parsed as " + linkInfo.getDescriptionOfParsedLink() + " but can not be checked for get info");
            result = nextWebsiteInfoWebClient.getWebSiteInfoByLinkInfo(linkInfo);
        }
        return result;
    }
}
