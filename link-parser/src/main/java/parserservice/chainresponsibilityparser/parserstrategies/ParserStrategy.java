package parserservice.chainresponsibilityparser.parserstrategies;

import parserservice.dto.WebsiteInfo;

public abstract class ParserStrategy <T extends WebsiteInfo> {
    private String website;

    public ParserStrategy(String website) {
        this.website = website;
    }

    public boolean canParse(String path){
        return path.startsWith("https://" + website) || path.startsWith("http://" + website);
    }
    public abstract T parse(String parse);
}
