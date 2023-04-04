package ru.tinkoff.edu.java.scrapper.usecases.impl.managelinks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import parserservice.ParserLinks;
import parserservice.dto.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import ru.tinkoff.edu.java.scrapper.usecases.ManageLinksUseCase;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsite;

import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ManageLinksUseCaseImpl implements ManageLinksUseCase {
    private ParserLinks linkParserLinks;
    private TrackedLinkDAService trackedLinkDAService;
    private CheckerUpdateOfWebsite checkerUpdateOfWebsite;
    @Autowired
    public ManageLinksUseCaseImpl(ParserLinks linkParserLinks,
                                  TrackedLinkDAService trackedLinkDAService,
                                  @Qualifier("checkerUpdateOfWebsite") CheckerUpdateOfWebsite checkerUpdateOfWebsite) {
        this.linkParserLinks = linkParserLinks;
        this.trackedLinkDAService = trackedLinkDAService;
        this.checkerUpdateOfWebsite = checkerUpdateOfWebsite;
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

        WebsiteInfo websiteInfoForAdd = getWebsiteInfoFromParser(addLinkRequest.getLink());

        OffsetDateTime lastUpdateTime = checkerUpdateOfWebsite.checkUpdateOfWebsite(websiteInfoForAdd);
        if(lastUpdateTime == null)
            throw new InvalidParameterException("Path " + addLinkRequest.getLink() +
                    " is parsed as " + websiteInfoForAdd.getDescriptionOfParsedWebsite()
                    + " but can not be checked for update because any checker for that site don't exist");

        Optional<TrackedLink> optionalRemovableTrackedLink =
                trackedLinkDAService.findTrackedLinkByWebsiteInfoAndIdChat(idChat,websiteInfoForAdd);
        if(optionalRemovableTrackedLink.isPresent())
            throw new InvalidParameterException("Link with path " + addLinkRequest.getLink() + " already " +
                    "tracked in chat with id " + idChat);

        TrackedLink newTracedLink = new TrackedLink(websiteInfoForAdd, idChat, lastUpdateTime);
        TrackedLink addedTrackedLink = trackedLinkDAService.create(newTracedLink);
        return addedTrackedLink;
    }

    @Override
    public TrackedLink removeLinkFromChat(int idChat, RemoveLinkRequest removeLinkRequest) {
        checkIfChatExist(idChat);

        WebsiteInfo websiteInfoForRemove = getWebsiteInfoFromParser(removeLinkRequest.getLink());

        Optional<TrackedLink> optionalRemovableTrackedLink = trackedLinkDAService.findTrackedLinkByWebsiteInfoAndIdChat(idChat, websiteInfoForRemove);

        if(optionalRemovableTrackedLink.isEmpty())
            throw new NotFoundException("Link with path " + removeLinkRequest.getLink() +
                    " already is not tracked in chat with id " + idChat);

        TrackedLink removableTrackedLink = optionalRemovableTrackedLink.get();
        trackedLinkDAService.delete(removableTrackedLink.getId());

        return removableTrackedLink;
    }
    private WebsiteInfo getWebsiteInfoFromParser(String path){
        WebsiteInfo websiteInfo = linkParserLinks.parse(path);
        if(websiteInfo == null)
            throw new InvalidParameterException("Path " + path + " can not be parsed");
        return websiteInfo;
    }
    private void checkIfChatExist(int idChat){
        if(!trackedLinkDAService.containsChatWithId(idChat))
            throw new InvalidParameterException("Chat with id " + idChat + " is not registered");
    }
}
