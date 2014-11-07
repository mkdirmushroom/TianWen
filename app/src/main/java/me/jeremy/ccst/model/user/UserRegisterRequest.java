package me.jeremy.ccst.model.user;

/**
 * Created by qiugang on 2014/9/24.
 */
public class UserRegisterRequest {

    private String userName;
    private String passWord;
    private String studentCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
}
