package ParserService.DTO;

public class StackOverflowInfo {
    private int id;
    private int idAnswer;

    public StackOverflowInfo() {
    }

    public StackOverflowInfo(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    public StackOverflowInfo(int id, int idAnswer) {
        this.id = id;
        this.idAnswer = idAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }
}
