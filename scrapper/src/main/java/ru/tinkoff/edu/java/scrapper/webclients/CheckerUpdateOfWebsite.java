package ru.tinkoff.edu.java.scrapper.webclients;

import parserservice.dto.WebsiteInfo;

import java.time.OffsetDateTime;

public interface CheckerUpdateOfWebsite {
    OffsetDateTime checkUpdateOfWebsite(WebsiteInfo websiteInfo);
}
