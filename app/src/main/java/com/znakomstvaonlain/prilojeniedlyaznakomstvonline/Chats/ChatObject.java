package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Chats;

public class ChatObject {
    private String message;
    private String date;
    private Boolean currentUser;

    public ChatObject(String message, String date, Boolean currentUser) {
        this.message = message;
        this.date = date;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }

    public String getDate() {
        return date;
    }
}
