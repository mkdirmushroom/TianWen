package me.jeremy.ccst.model.question;

import java.util.List;

/**
 * Created by qiugang on 2014/9/28.
 */

public class CreateAnswerSheetRequest {

    private Integer userId; //用户ID
    private Integer questionnaireId; //问卷ID
    private List<CreateQuestionAnswer> questions; //问题

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public List<CreateQuestionAnswer> getQuestions() {
        return questions;
    }

    public void setQuestions(List<CreateQuestionAnswer> questions) {
        this.questions = questions;
    }
}
