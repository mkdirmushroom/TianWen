package me.jeremy.ccst.model.question;

import java.util.List;

/**
 * Created by qiugang on 2014/9/28.
 */

public class CreateQuestionAnswer {

    private Integer questionId; //问题ID
    private String questionType; //问题类型 S/M/F
    private List<CreateAnswerDetailRequest> answers; //答案

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
    
    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<CreateAnswerDetailRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<CreateAnswerDetailRequest> answers) {
        this.answers = answers;
    }
}
