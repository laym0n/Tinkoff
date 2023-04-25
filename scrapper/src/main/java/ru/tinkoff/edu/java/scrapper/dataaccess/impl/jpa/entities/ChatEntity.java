package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class ChatEntity {
    @Id
    private int id;
    public ChatEntity(Chat chat){
        this.id = chat.getId();
    }
    public Chat getChat(){
        return new Chat(id);
    }
}
