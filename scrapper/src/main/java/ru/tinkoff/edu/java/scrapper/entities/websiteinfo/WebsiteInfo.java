package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import parserservice.dto.LinkInfo;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Setter
public sealed abstract class WebsiteInfo permits GitHubInfo, StackoverflowInfo {
    private LinkInfo linkInfo;
    private OffsetDateTime lastCheckUpdateDateTime;
    public WebsiteInfo(LinkInfo linkInfo) {
        this(linkInfo, OffsetDateTime.now());
    }
}
