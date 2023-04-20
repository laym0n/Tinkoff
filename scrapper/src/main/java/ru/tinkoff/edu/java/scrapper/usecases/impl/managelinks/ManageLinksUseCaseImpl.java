package ru.tinkoff.edu.java.scrapper.usecases.impl.managelinks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import parserservice.ParserLinks;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.ManageLinksUseCase;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClient;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Component
public class ManageLinksUseCaseImpl implements ManageLinksUseCase {
    private ParserLinks parserLinks;
    private TrackedLinkDAService trackedLinkDAService;
    private WebsiteInfoWebClient websiteInfoWebClient;
    @Autowired
    public ManageLinksUseCaseImpl(ParserLinks parserLinks,
                                  TrackedLinkDAService trackedLinkDAService,
                                  @Qualifier("checkerUpdateOfWebsite") WebsiteInfoWebClient websiteInfoWebClient) {
        this.parserLinks = parserLinks;
        this.trackedLinkDAService = trackedLinkDAService;
        this.websiteInfoWebClient = websiteInfoWebClient;
    }

    @Override
    public List<TrackedLink> findAllLinksByIdChat(int idChat) {
        checkIfChatExist(idChat);
        List<TrackedLink> linksForChat = trackedLinkDAService.getAllTrackedLinksByChatId(idChat);
        return linksForChat;
    }

    @Override
    public TrackedLink addLinkForChat(int idChat, AddLinkRequest addLinkRequest) {
        checkIfChatExist(idChat);

        LinkInfo linkInfoForAdd = getLinkInfoFromParser(addLinkRequest.getLink());

        boolean isAlreadyContainsTrackedLink =
                trackedLinkDAService.containsTrackedLinkWithIdChatAndLinkInfo(idChat, linkInfoForAdd);
        if(isAlreadyContainsTrackedLink)
            throw new InvalidParameterException("Link with path " + addLinkRequest.getLink() + " already " +
                    "tracked in chat with id " + idChat);

        Optional<Integer> optionalIdAlreadySavedWebsiteInfo =
                trackedLinkDAService.containsWebsiteInfoWithLinkInfo(linkInfoForAdd);
        int idAlreadySavedWebsiteInfo = optionalIdAlreadySavedWebsiteInfo.orElse(0);
        if(optionalIdAlreadySavedWebsiteInfo.isEmpty()){
            WebsiteInfo newWebsiteInfo = websiteInfoWebClient.getWebSiteInfoByLinkInfo(linkInfoForAdd);
            trackedLinkDAService.createWebsiteInfo(newWebsiteInfo);
            idAlreadySavedWebsiteInfo = newWebsiteInfo.getId();
        }

        TrackedLink newTrackedLink = new TrackedLink(idChat, linkInfoForAdd, idAlreadySavedWebsiteInfo);
        newTrackedLink = trackedLinkDAService.createTrackedLink(newTrackedLink);

        return newTrackedLink;
    }

    @Override
    public TrackedLink removeLinkFromChat(int idChat, RemoveLinkRequest removeLinkRequest) {
        checkIfChatExist(idChat);

        LinkInfo linkInfoForRemove = getLinkInfoFromParser(removeLinkRequest.getLink());

        Optional<TrackedLink> optionalRemovableTrackedLink =
                trackedLinkDAService.deleteTrackedLinkByIdChatAndLinkInfo(idChat, linkInfoForRemove);

        if(optionalRemovableTrackedLink.isEmpty())
            throw new NotFoundException("Link with path " + removeLinkRequest.getLink() +
                    " already is not tracked in chat with id " + idChat);

        TrackedLink removableTrackedLink = optionalRemovableTrackedLink.get();

        return removableTrackedLink;
    }
    private LinkInfo getLinkInfoFromParser(String path){
        LinkInfo linkInfo = parserLinks.parse(path);
        if(linkInfo == null)
            throw new InvalidParameterException("Path " + path + " can not be parsed");
        return linkInfo;
    }
    private void checkIfChatExist(int idChat){
        if(!trackedLinkDAService.containsChatWithId(idChat))
            throw new InvalidParameterException("Chat with id " + idChat + " is not registered");
    }
}
