package com.uslive.rabyks.models;

/**
 * Created by marezina on 19.5.2015.
 */
public class User {
    private int id;
    private String email;
    private String password;
    private String number;
    private String role;

    public User() {
    }

    public User(int id, String email, String password, String number, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.number = number;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
