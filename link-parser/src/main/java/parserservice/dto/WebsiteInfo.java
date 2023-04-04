package parserservice.dto;

public sealed interface WebsiteInfo permits GitHubInfo, StackOverflowInfo {
    String getPath();
    String getDescriptionOfParsedWebsite();
}
