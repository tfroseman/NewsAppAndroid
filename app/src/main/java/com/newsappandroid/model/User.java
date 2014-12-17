package com.newsappandroid.model;

/**
 * User.java
 * Holds all of the info about the user in the app
 * Is a singleton class
 */

public class User {

    private int id;
    private String email, password;
    private String first_name, last_name, api_token = "";
    private String activated_at;
    private Boolean activated;
    private int signed_in = -9;
    private String last_login;
    private String created_at;
    private String updated_at;

    private static User user = new User();

    private User() {
    }

    public static User getUser() {
        return user;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public Boolean hasToken() {
        return !this.getApi_token().isEmpty();
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getActivated_at() {
        return activated_at;
    }

    public void setActivated_at(String activated_at) {
        this.activated_at = activated_at;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
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

    public int getSigned_in() {
        return signed_in;
    }

    public void setSigned_in(int signed_in) {
        this.signed_in = signed_in;
    }
}
