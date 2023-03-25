package parserservice;

import parserservice.dto.WebsiteInfo;

public interface Parser {
    WebsiteInfo parse(String path);
}
