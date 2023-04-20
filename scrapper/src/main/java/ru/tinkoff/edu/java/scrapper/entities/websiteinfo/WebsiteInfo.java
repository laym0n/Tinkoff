package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;
import parserservice.dto.LinkInfo;

import java.time.OffsetDateTime;
import java.util.Objects;

@AllArgsConstructor
@ToString
@Getter
@Setter
public sealed abstract class WebsiteInfo permits GitHubInfo, StackOverflowInfo {
    private int id;
    @EqualsExclude
    private OffsetDateTime lastCheckUpdateDateTime;
    public WebsiteInfo() {
        this(OffsetDateTime.now());
    }

    public WebsiteInfo(OffsetDateTime lastCheckUpdateDateTime) {
        this(0, lastCheckUpdateDateTime);
    }

    public abstract LinkInfo getLinkInfo();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebsiteInfo that = (WebsiteInfo) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
