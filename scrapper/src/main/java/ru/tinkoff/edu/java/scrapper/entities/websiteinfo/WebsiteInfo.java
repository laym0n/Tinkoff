package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
public sealed abstract class WebsiteInfo permits GitHubInfo, StackoverflowInfo {
    private OffsetDateTime lastCheckUpdateDateTime;

    public WebsiteInfo() {
        this(OffsetDateTime.now());
    }
}
