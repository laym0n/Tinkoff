package parserservice;

import parserservice.dto.LinkInfo;

public interface ParserLinks {
    LinkInfo parse(String path);
}
