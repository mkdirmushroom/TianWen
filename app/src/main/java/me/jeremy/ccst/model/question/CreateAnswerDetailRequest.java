package me.jeremy.ccst.model.question;

/**
 * Created by qiugang on 2014/9/28.
 */
public class CreateAnswerDetailRequest {

    private Integer answerId; //选择题选项ID
    private String content; //填空题内容

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
