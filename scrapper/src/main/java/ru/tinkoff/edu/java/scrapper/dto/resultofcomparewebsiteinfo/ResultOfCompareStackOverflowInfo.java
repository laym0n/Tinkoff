package ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo;

import lombok.Getter;
import lombok.Setter;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

@Getter
@Setter
public final class ResultOfCompareStackOverflowInfo extends ResultOfCompareWebsiteInfo {
    private StackOverflowAnswer[] deletedAnswers;
    private StackOverflowAnswerResponse[] addedAnswers;
    private StackOverflowAnswerResponse[] editedAnswers;
    private StackOverflowComment[] deletedComments;
    private StackOverflowCommentResponse[] addedComments;
    private StackOverflowLinkInfo linkInfo = null;

//    public ResultOfCompareStackOverflowInfo(boolean isDifferent,
//            StackOverflowLinkInfo linkInfo,
//            int idWebsiteInfo,
//            StackOverflowAnswer[] deletedAnswers,
//            StackOverflowAnswerResponse[] addedAnswers,
//            StackOverflowAnswerResponse[] editedAnswers,
//            StackOverflowComment[] deletedComments,
//            StackOverflowCommentResponse[] addedComments) {
//        super(isDifferent, idWebsiteInfo);
//        this.deletedAnswers = deletedAnswers;
//        this.addedAnswers = addedAnswers;
//        this.editedAnswers = editedAnswers;
//        this.deletedComments = deletedComments;
//        this.addedComments = addedComments;
//        this.linkInfo = linkInfo;
//    }

    public ResultOfCompareStackOverflowInfo(int idWebsiteInfo, StackOverflowLinkInfo linkInfo) {
        super(idWebsiteInfo);
        this.linkInfo = linkInfo;
    }
}
