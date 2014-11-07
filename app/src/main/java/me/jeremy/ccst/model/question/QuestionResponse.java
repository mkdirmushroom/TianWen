package me.jeremy.ccst.model.question;

import java.util.List;

/**
 * Created by qiugang on 2014/9/27.
 */
public class QuestionResponse {

    private Integer id;
    private String title;
    private String questionType;

    private List<ChooseResponse> choose;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChooseResponse> getChoose() {
        return choose;
    }

    public void setChoose(List<ChooseResponse> choose) {
        this.choose = choose;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
