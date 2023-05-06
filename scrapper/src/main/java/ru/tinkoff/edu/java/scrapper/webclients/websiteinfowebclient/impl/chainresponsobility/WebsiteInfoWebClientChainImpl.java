package ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.impl.chainresponsobility;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClient;
import java.security.InvalidParameterException;

@AllArgsConstructor
@NoArgsConstructor
public abstract class WebsiteInfoWebClientChainImpl implements WebsiteInfoWebClient {
    private WebsiteInfoWebClient nextWebsiteInfoWebClient = null;

    @Override
    public WebsiteInfo getWebSiteInfoByLinkInfo(LinkInfo linkInfo) {
        WebsiteInfo result;
        if(!canLoad(linkInfo)){
            if(nextWebsiteInfoWebClient == null)
                throw new InvalidParameterException("Link " + linkInfo.getPath() +
                        " parsed as " + linkInfo.getDescriptionOfParsedLink() + " but can not be checked for get info");
            result = nextWebsiteInfoWebClient.getWebSiteInfoByLinkInfo(linkInfo);
        }
        else
            result = loadWebsiteInfo(linkInfo);
        return result;
    }
    protected abstract boolean canLoad(LinkInfo linkInfo);
    protected abstract WebsiteInfo loadWebsiteInfo(LinkInfo linkInfo);
}
