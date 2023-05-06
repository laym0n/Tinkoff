package ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo;

import lombok.*;
import parserservice.dto.LinkInfo;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public abstract sealed class ResultOfCompareWebsiteInfo permits ResultOfCompareGitHubInfo, ResultOfCompareStackOverflowInfo {
    protected boolean isDifferent = false;
    private int idWebsiteInfo;
    public abstract LinkInfo getLinkInfo();
    protected ResultOfCompareWebsiteInfo(int idWebsiteInfo) {
        this.idWebsiteInfo = idWebsiteInfo;
    }
}
