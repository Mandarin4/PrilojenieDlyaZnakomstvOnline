package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users;

public class Users {
    private String password, email;

    public Users(){
    }

    public Users(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
