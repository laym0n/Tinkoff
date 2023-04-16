package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import parserservice.dto.LinkInfo;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Setter
public sealed abstract class WebsiteInfo permits GitHubInfo, StackOverflowInfo {
    private int id;
    private OffsetDateTime lastCheckUpdateDateTime;
    public WebsiteInfo() {
        this(OffsetDateTime.now());
    }

    public WebsiteInfo(OffsetDateTime lastCheckUpdateDateTime) {
        this(0, lastCheckUpdateDateTime);
    }

    public abstract LinkInfo getLinkInfo();
}
