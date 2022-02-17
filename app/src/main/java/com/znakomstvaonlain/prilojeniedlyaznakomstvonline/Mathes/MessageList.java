package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Mathes;

public class MessageList {
    private String name, mobile, lastMessage, profilePic;
    private int unseenMEssage;

    public MessageList(String name, String mobile, String lastMessage, String profilePic, int unseenMEssage) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMEssage = unseenMEssage;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMEssage() {
        return unseenMEssage;
    }
}
