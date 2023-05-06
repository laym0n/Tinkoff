package parserservice.dto;

public sealed interface LinkInfo permits GitHubLinkInfo, StackOverflowLinkInfo {
    String getPath();
    String getDescriptionOfParsedLink();
}
