package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GitHubCommit {
    private String sha;

}
