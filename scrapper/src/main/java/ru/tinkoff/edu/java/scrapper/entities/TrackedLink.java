package ru.tinkoff.edu.java.scrapper.entities;

import lombok.*;
import parserservice.dto.WebsiteInfo;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TrackedLink {
    private int id;
    private WebsiteInfo websiteInfo;
    private int chatId;
    private OffsetDateTime lastUpdateDateTime;

    public TrackedLink(WebsiteInfo websiteInfo, int chatId, OffsetDateTime lastUpdateDateTime) {
        this.websiteInfo = websiteInfo;
        this.chatId = chatId;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackedLink that = (TrackedLink) o;
        return getId() == that.getId() && getChatId() == that.getChatId() && Objects.equals(getWebsiteInfo(), that.getWebsiteInfo()) && Objects.equals(getLastUpdateDateTime(), that.getLastUpdateDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getWebsiteInfo(), getChatId(), getLastUpdateDateTime());
    }

    public String getLinkPath(){
        return websiteInfo.getPath();
    }
    public URI getLinkPathInURI(){
        return URI.create(websiteInfo.getPath());
    }
}
