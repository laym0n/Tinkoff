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
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfo;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Component
public class ManageLinksUseCaseImpl implements ManageLinksUseCase {
    private ParserLinks parserLinks;
    private TrackedLinkDAService trackedLinkDAService;
    private CheckerOfWebsiteInfo checkerOfWebsiteInfo;
    @Autowired
    public ManageLinksUseCaseImpl(ParserLinks parserLinks,
                                  TrackedLinkDAService trackedLinkDAService,
                                  @Qualifier("checkerUpdateOfWebsite") CheckerOfWebsiteInfo checkerOfWebsiteInfo) {
        this.parserLinks = parserLinks;
        this.trackedLinkDAService = trackedLinkDAService;
        this.checkerOfWebsiteInfo = checkerOfWebsiteInfo;
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

        boolean isAlreadyContainsLink =
                trackedLinkDAService.containsTrackedLinkWithIdChatAndLinkInfo(idChat, linkInfoForAdd);
        if(isAlreadyContainsLink)
            throw new InvalidParameterException("Link with path " + addLinkRequest.getLink() + " already " +
                    "tracked in chat with id " + idChat);

        boolean isAlreadyWebsiteInfoSaved =
                trackedLinkDAService.containsWebsiteInfoWithLinkInfo(linkInfoForAdd);
        if(!isAlreadyWebsiteInfoSaved){
            WebsiteInfo newWebsiteInfo = checkerOfWebsiteInfo.getWebSiteInfoByLinkInfo(linkInfoForAdd);
            trackedLinkDAService.createWebsiteInfo(newWebsiteInfo);
        }

        TrackedLink newTrackedLink = new TrackedLink(idChat, linkInfoForAdd);
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
