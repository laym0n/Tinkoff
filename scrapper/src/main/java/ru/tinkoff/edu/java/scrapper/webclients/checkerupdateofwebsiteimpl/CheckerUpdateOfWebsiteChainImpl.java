package ru.tinkoff.edu.java.scrapper.webclients.checkerupdateofwebsiteimpl;

import lombok.AllArgsConstructor;
import parserservice.dto.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsite;

import java.time.OffsetDateTime;

@AllArgsConstructor
public class CheckerUpdateOfWebsiteChainImpl implements CheckerUpdateOfWebsite {
    private CheckerUpdateOfWebsite myCheckerUpdateOfWebsite;
    private CheckerUpdateOfWebsite nextCheckerUpdateOfWebsite;
    @Override
    public OffsetDateTime checkUpdateOfWebsite(WebsiteInfo websiteInfo) {
        OffsetDateTime result = myCheckerUpdateOfWebsite.checkUpdateOfWebsite(websiteInfo);
        if(result == null && nextCheckerUpdateOfWebsite != null)
            result = nextCheckerUpdateOfWebsite.checkUpdateOfWebsite(websiteInfo);
        return result;
    }
}
