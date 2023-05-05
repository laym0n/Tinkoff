package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.ChatRecord;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQChatDAO extends JOOQDAO {
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat chat =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

    public void add(Chat newChat) {
        ChatRecord newChatRecord = context.newRecord(chat);
        newChatRecord.setId(newChat.getId());
        newChatRecord.store();
    }

    public void remove(int idChat) {
        context.delete(chat)
            .where(chat.ID.eq(idChat))
            .execute();
    }

    public boolean containsChatWithId(int chatId) {
        int countRows =
            context.selectCount()
                .from(chat)
                .where(chat.ID.eq(Integer.valueOf(chatId)))
                .fetch().get(0).value1();
        return countRows != 0;
    }

    public Optional<Chat> findByID(int id) {
        ChatRecord loadedChatRecord =
            context.fetchOne(chat, chat.ID.eq(id));
        if (loadedChatRecord == null) {
            return Optional.empty();
        }
        Chat result = new Chat(loadedChatRecord.getId());
        return Optional.of(result);
    }
}
