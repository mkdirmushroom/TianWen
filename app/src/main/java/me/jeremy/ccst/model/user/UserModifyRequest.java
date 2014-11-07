package me.jeremy.ccst.model.user;

/**
 * Created by qiugang on 14/10/30.
 */
public class UserModifyRequest {

    private int id;
    private String qq;
    private String phone;
    private String email;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
