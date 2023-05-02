package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

@Entity
@Table(name = "tracked_link")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackedLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "website_info_id")
    @JoinColumn(table = "website_info", referencedColumnName = "id")
    private int websiteInfoId;
    @Column(name = "chat_id")
    @JoinColumn(table = "chat", referencedColumnName = "id")
    private int chatId;

    public TrackedLinkEntity(TrackedLink trackedLink) {
        this.id = trackedLink.getId();
        this.websiteInfoId = trackedLink.getIdWebsiteInfo();
        this.chatId = trackedLink.getIdChat();
    }

    public TrackedLinkEntity(int websiteInfoId, int chatId) {
        this(0, websiteInfoId, chatId);
    }

    public TrackedLink getTrackedLink() {
        return new TrackedLink(id, chatId, websiteInfoId, null);
    }
}
