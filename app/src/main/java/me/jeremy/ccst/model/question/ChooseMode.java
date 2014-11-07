package me.jeremy.ccst.model.question;

/**
 * Created by qiugang on 2014/9/28.
 */
public class ChooseMode {

    private Integer id; //选项ID
    private String detail; //选项文本
    private Boolean select;

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

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
