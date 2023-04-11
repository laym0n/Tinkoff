package parserservice.dto;

public record StackOverflowInfo(int idAnswer) implements WebsiteInfo{

    @Override
    public String getPath() {
        return "https://stackoverflow.com/questions/" + idAnswer + "/";
    }
    public String getDescriptionOfParsedWebsite(){
        return "StackOverflow answer with id of answer " + idAnswer;
    }
}
