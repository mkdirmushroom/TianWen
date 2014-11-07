package me.jeremy.ccst.model.question;

import java.util.List;

/**
 * Created by qiugang on 14-10-12.
 */
public class PageModel {

    private List<QuestionnaireResponse> records;
    private Long totalRecords;
    private Integer pageSize;
    private Integer pageNo;

    public List<QuestionnaireResponse> getRecords() {
        return records;
    }

    public void setRecords(List<QuestionnaireResponse> records) {
        this.records = records;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

}
