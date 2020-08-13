package com.grupy.grupy.models;

public class Post {

    private String id;
    private String name;
    private String description;
    private String image;
    private String IdUser;

    //private String category;

    public Post() {

    }

    public Post(String id, String title, String description, String image, String idUser) {
        this.id = id;
        this.name = title;
        this.description = description;
        this.image = image;
        IdUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }
}
