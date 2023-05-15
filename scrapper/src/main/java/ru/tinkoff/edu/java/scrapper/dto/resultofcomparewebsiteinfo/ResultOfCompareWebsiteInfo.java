package ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import parserservice.dto.LinkInfo;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public abstract sealed class ResultOfCompareWebsiteInfo
    permits ResultOfCompareGitHubInfo, ResultOfCompareStackOverflowInfo {
    protected boolean isDifferent = false;
    private int idWebsiteInfo;

    public abstract LinkInfo getLinkInfo();

    protected ResultOfCompareWebsiteInfo(int idWebsiteInfo) {
        this.idWebsiteInfo = idWebsiteInfo;
    }
}
