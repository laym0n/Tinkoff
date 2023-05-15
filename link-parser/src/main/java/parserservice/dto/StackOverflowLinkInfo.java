package parserservice.dto;

public record StackOverflowLinkInfo(int idQuestion) implements LinkInfo {

    @Override
    public String getPath() {
        return "https://stackoverflow.com/questions/" + idQuestion + "/";
    }

    public String getDescriptionOfParsedLink() {
        return "StackOverflow answer with id of answer " + idQuestion;
    }
}
