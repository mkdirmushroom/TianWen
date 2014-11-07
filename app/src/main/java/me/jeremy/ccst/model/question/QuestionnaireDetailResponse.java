package me.jeremy.ccst.model.question;

import java.util.List;

/**
 * Created by qiugang on 2014/9/27.
 */
public class QuestionnaireDetailResponse {

    private Integer id; //问卷ID
    private String code; //问卷编号
    private String topic; //问卷主题
    private List<QuestionResponse> questions; //问题

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }
}
