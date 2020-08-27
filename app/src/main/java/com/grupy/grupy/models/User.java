package com.grupy.grupy.models;

public class User {

    private String id;
    private String email;
    private String username;
    private long timestamp;
    private String imageProfile;
    private String imageCover;
    private long lastConnection;
    private boolean online;

    public  User() {

    }

    public User(String id, String email, String username, long timestamp, String imageProfile, String imageCover, long lastConnection, boolean online) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.timestamp = timestamp;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
        this.lastConnection = lastConnection;
        this.online = online;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
