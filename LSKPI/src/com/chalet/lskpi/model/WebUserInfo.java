package com.chalet.lskpi.model;

import java.util.Date;

public class WebUserInfo {

    private int id;
    private String name;
    private String password;
    private String telephone;
    private String level;
    private Date createdate;
    private Date modifydate;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public Date getCreatedate() {
        return createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
    public Date getModifydate() {
        return modifydate;
    }
    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }
}
