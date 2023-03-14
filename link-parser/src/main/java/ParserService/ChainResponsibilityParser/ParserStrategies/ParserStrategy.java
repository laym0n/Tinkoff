package ParserService.ChainResponsibilityParser.ParserStrategies;

public abstract class ParserStrategy <T> {
    private String website;

    public ParserStrategy(String website) {
        this.website = website;
    }

    public boolean canParse(String path){
        return path.startsWith("https://" + website) || path.startsWith("http://" + website);
    }
    public abstract T parse(String parse);
}
