package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

@Entity
@Table(name = "tracked_link")
@Data
@NoArgsConstructor
@Getter
@Setter
public class TrackedLinkEntity {
    @Id
    private int id;
    private int websiteInfoId;
    private int chatId;
    public TrackedLinkEntity(TrackedLink trackedLink){
        this.id = trackedLink.getId();
        this.websiteInfoId = trackedLink.getIdWebsiteInfo();;
        this.chatId = trackedLink.getIdChat();
    }
    public TrackedLink getTrackedLink(){
        return new TrackedLink(id, chatId, websiteInfoId,null);
    }
}
