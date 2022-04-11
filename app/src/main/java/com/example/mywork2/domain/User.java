package com.example.mywork2.domain;

import java.util.ArrayList;

public class User {
    private String username;
    private String profileId;
    private String nickname;
    private String email;
    private String password;
    private ArrayList<Ticket> tickets;

    public User(String username, String profileId, String nickname, String email, String password, ArrayList<Ticket> tickets) {
        this.username = username;
        this.profileId = profileId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.tickets = tickets;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", profileId='" + profileId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", tickets=" + tickets +
                '}';
    }
}
