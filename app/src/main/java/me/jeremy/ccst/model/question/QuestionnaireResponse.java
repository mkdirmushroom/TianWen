package me.jeremy.ccst.model.question;

/**
 * Created by qiugang on 2014/9/27.
 */
public class QuestionnaireResponse {

    private int id;
    private String code;
    private String topic;
    private String date;
    private boolean done;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDone(boolean tag) {
        this.done = tag;
    }

    public boolean getDone() {
        return done;
    }


}
