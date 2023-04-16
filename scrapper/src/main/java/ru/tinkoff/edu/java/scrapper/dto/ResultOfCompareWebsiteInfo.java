package ru.tinkoff.edu.java.scrapper.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.tinkoff.edu.java.scrapper.dto.response.website.WebsiteResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ResultOfCompareWebsiteInfo<W extends WebsiteInfo, R extends WebsiteResponse> {
    public boolean isDifferent;
    public W uniqueSavedData;
    public R uniqueLoadedData;

}
