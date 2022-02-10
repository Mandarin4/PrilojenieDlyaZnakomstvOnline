package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Cards;

public class cards {
    private String userId;
    private String name;
    private String profileImageUri;

    public cards(String userId, String name, String profileImageUri){
        this.userId = userId;
        this.name = name;
        this.profileImageUri = profileImageUri;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }
}
