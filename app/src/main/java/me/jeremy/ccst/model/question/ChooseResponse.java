package me.jeremy.ccst.model.question;

/**
 * Created by qiugang on 2014/9/27.
 */
public class ChooseResponse {

    private Integer id; //选项ID
    private String detail; //选项文本

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
