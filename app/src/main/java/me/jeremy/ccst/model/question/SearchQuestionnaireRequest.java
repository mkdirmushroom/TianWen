package me.jeremy.ccst.model.question;

/**
 * Created by qiugang on 14-10-12.
 */
public class SearchQuestionnaireRequest {

    private String searchType;
    private String code;
    private String keyWord;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}
