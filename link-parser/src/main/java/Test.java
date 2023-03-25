import parserservice.chainresponsibilityparser.ParserImpl;
import parserservice.chainresponsibilityparser.parserstrategies.GitHubParserStrategy;
import parserservice.chainresponsibilityparser.parserstrategies.StackOverflowParserStrategy;
import parserservice.dto.GitHubInfo;
import parserservice.dto.StackOverflowInfo;
import parserservice.Parser;
import parserservice.dto.WebsiteInfo;

import java.util.logging.Logger;

public class Test {
    public static void main(String[] args){
        ParserImpl gitHubParser = new ParserImpl(new GitHubParserStrategy(),
                null);
        ParserImpl stackOverflowParser = new ParserImpl(
                new StackOverflowParserStrategy(),
                gitHubParser);

        Parser parser = stackOverflowParser;

        String[] pathes = new String[]{
                "https://github.com/sanyarnd/tinkoff-java-course-2022/",
        "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
        "https://stackoverflow.com/search?q=unsupported%20link",
        "https://github.com/spring-projects/spring-framework",
        "https://github1.com/spring-projects/spring-framework",
        "htts://github.com/spring-projects/spring-framework",
        "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
        "https://stackoeverflow.com/questions/1642028/what-is-the-operator-in-c",
        "https://stacoverflow.com/questions/1642028/what-is-the-operator-in-c",
        "htts:/stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
        ""
        };
        for(var path : pathes)
            showWebsiteInfo(parser.parse(path));


    }
    public static void showWebsiteInfo(WebsiteInfo info){
        if(info == null){
            System.out.println("Путь не валиден");
            return;
        }
        switch (info){
            case GitHubInfo githubInfo:
                System.out.println("Имя пользователя " + githubInfo.userName() + " Имя репозитория " + githubInfo.repositoryName());
                break;
            case StackOverflowInfo stackOverflowInfo:
                System.out.println("Id ответа " + stackOverflowInfo.idAnswer());
        }
    }
}
