package ru.tinkoff.edu.java.scrapper.dataaccess.impl.websiteinfodaservice;

import parserservice.dto.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.TrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TrackedLinksDAServiceImplOnHashMap implements TrackedLinkDAService {
    private ChatDAService chatDAService;

    public TrackedLinksDAServiceImplOnHashMap(ChatDAService chatDAService) {
        this.chatDAService = chatDAService;
    }

    private HashMap<Integer, TrackedLink> savedTrackedLink = new HashMap<>();
    int lastId = 0;
    @Override
    public TrackedLink create(TrackedLink newTrackedLink) {
        newTrackedLink.setId(lastId++);
        Chat chatForLink = getChatByChatId(newTrackedLink.getChatId());
        chatForLink.getLinks().add(newTrackedLink);
        savedTrackedLink.put(newTrackedLink.getId(), newTrackedLink);
        chatDAService.update(chatForLink);
        return newTrackedLink;
    }

    @Override
    public Optional<TrackedLink> findById(Integer idEntity) {
        return Optional.ofNullable(savedTrackedLink.get(idEntity));
    }

    @Override
    public void update(TrackedLink entity) {
        savedTrackedLink.put(entity.getId(), entity);
    }

    @Override
    public void delete(Integer idEntity) {
        TrackedLink removableTrackedLink = savedTrackedLink.get(idEntity);
        if(removableTrackedLink.equals(null))
            return;
        Chat chatForLink = getChatByChatId(removableTrackedLink.getChatId());
        chatForLink.getLinks().remove(removableTrackedLink);
        savedTrackedLink.remove(idEntity);
        chatDAService.update(chatForLink);
    }

    @Override
    public boolean containsChatWithId(int chatId) {
        return chatDAService.containsChatWithId(chatId);
    }

    @Override
    public boolean containsTrackedLinkWithId(int trackedLinkId) {
        return false;
    }

    @Override
    public Optional<TrackedLink> findTrackedLinkByWebsiteInfoAndIdChat(int idChat, WebsiteInfo websiteInfoForRemove) {
        Optional<TrackedLink> result = Optional.empty();
        for (TrackedLink trackedLink: savedTrackedLink.values()) {
            if(trackedLink.getChatId() == idChat && trackedLink.getWebsiteInfo().equals(websiteInfoForRemove)){
                result = Optional.of(trackedLink);
                break;
            }
        }
        return result;
    }

    @Override
    public List<TrackedLink> getAllTrackedLinksByChatId(int idChat) {
        Chat chat = getChatByChatId(idChat);
        return chat.getLinks();
    }
    private Chat getChatByChatId(int chatId){
        Optional<Chat> optionalChatForLink = chatDAService.findById(chatId);
        if(optionalChatForLink.isEmpty())
            throw new InvalidParameterException("Chat with id  " + chatId + " don't exist in database");
        return optionalChatForLink.get();
    }
}
