package parserservice;

import parserservice.dto.WebsiteInfo;

public interface ParserLinks {
    WebsiteInfo parse(String path);
}
