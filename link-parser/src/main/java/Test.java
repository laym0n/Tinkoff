import ParserService.ChainResponsibilityParser.ParserImpl;
import ParserService.ChainResponsibilityParser.ParserStrategies.GitHubParserStrategy;
import ParserService.ChainResponsibilityParser.ParserStrategies.StackOverflowParserStrategy;
import ParserService.WebsiteInfoRepository;
import ParserService.DTO.GitHubInfo;
import ParserService.DTO.StackOverflowInfo;
import ParserService.Parser;

public class Test {
    public static void main(String[] args){
        WebsiteInfoRepository<GitHubInfo> gitHubInfoRepository = new WebsiteInfoRepository<>() {
            @Override
            public void save(GitHubInfo gitHubInfo) {
                System.out.println("UserName: " + gitHubInfo.getUserName() + " RepositoryName: " + gitHubInfo.getRepositoryName());
            }
        };
        WebsiteInfoRepository<StackOverflowInfo> stackOverflowRepository = new WebsiteInfoRepository<>() {
            @Override
            public void save(StackOverflowInfo stackOverflowInfo) {
                System.out.println("AnswerId: " + stackOverflowInfo.getIdAnswer());
            }
        };
        Parser resultParser = new Parser() {
            @Override
            public void parse(String path) {
                System.out.println(path + " is not valid for any parser");
            }
        };
        ParserImpl<GitHubInfo> gitHubParser = new ParserImpl<>(new GitHubParserStrategy("github.com"),
                resultParser, gitHubInfoRepository);
        ParserImpl<StackOverflowInfo> stackOverflowParser = new ParserImpl<>(
                new StackOverflowParserStrategy("stackoverflow.com"),
                gitHubParser, stackOverflowRepository);

        Parser parser = stackOverflowParser;

        String path1 = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        String path2 = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        String path3 = "https://stackoverflow.com/search?q=unsupported%20link";
        String path4 = "https://github.com/spring-projects/spring-framework";
        String path5 = "https://github1.com/spring-projects/spring-framework";
        String path6 = "htts://github.com/spring-projects/spring-framework";
        String path7 = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        String path8 = "https://stackoeverflow.com/questions/1642028/what-is-the-operator-in-c";
        String path9 = "https://stacoverflow.com/questions/1642028/what-is-the-operator-in-c";
        String path10 = "htts:/stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        String path11 = "";

        parser.parse(path1);
        parser.parse(path2);
        parser.parse(path3);
        parser.parse(path4);
        parser.parse(path5);
        parser.parse(path6);
        parser.parse(path7);
        parser.parse(path8);
        parser.parse(path9);
        parser.parse(path10);
        parser.parse(path11);


    }
}
