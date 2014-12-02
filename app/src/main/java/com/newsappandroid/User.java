package com.newsappandroid;

import java.util.Date;

public class User {
    private String email;
    private String password;

    private int id;
    private String first_name;
    private Date updated_at;
    private String api_token;
    private Boolean activated;
    private String last_name;
    private Date created_at;
    private Boolean activated_at;
    private Date last_login;


    protected User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Boolean getActivated_at() {
        return activated_at;
    }

    public void setActivated_at(Boolean activated_at) {
        this.activated_at = activated_at;
    }

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", first_name='" + first_name + '\'' +
                ", updated_at=" + updated_at +
                ", api_token='" + api_token + '\'' +
                ", activated=" + activated +
                ", last_name='" + last_name + '\'' +
                ", created_at=" + created_at +
                ", activated_at=" + activated_at +
                ", last_login=" + last_login +
                '}';
    }
}
