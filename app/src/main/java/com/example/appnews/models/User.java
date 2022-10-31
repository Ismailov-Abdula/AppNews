package com.example.appnews.models;

public class User {
    private String name;
    private String role;
    private String login;
    private String password;

    public enum ROLE{
        ADMIN,
        USER
    }

    public User() {
    }

    public User(String name, String role, String login, String password) {
        this.name = name;
        this.role = role;
        this.login = login;
        this.password = password;
    }

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
