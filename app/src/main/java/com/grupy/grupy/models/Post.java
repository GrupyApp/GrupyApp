package com.grupy.grupy.models;

public class Post {

    private String id;
    private String name;
    private String description;
    private String image1 = "";
    private String image2 = "";
    private String image3 = "";
    private String image4 = "";
    private String image5 = "";
    private String image6 = "";
    private String image7 = "";
    private String image8 = "";
    private String image9 = "";
    private String image10 = "";
    private String IdUser;

    //private String category;

    public Post() {

    }

    public Post(String id, String name, String description, String image1, String image2, String image3, String image4, String image5, String image6, String image7, String image8, String image9, String image10, String idUser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.image6 = image6;
        this.image7 = image7;
        this.image8 = image8;
        this.image9 = image9;
        this.image10 = image10;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getImage7() {
        return image7;
    }

    public void setImage7(String image7) {
        this.image7 = image7;
    }

    public String getImage8() {
        return image8;
    }

    public void setImage8(String image8) {
        this.image8 = image8;
    }

    public String getImage9() {
        return image9;
    }

    public void setImage9(String image9) {
        this.image9 = image9;
    }

    public String getImage10() {
        return image10;
    }

    public void setImage10(String image10) {
        this.image10 = image10;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public void addImageUrl(String url) {
        if (this.image1 == "") {
            this.image1 = url;
        }
        else if (this.image2 == "") {
            this.image2 = url;
        }
        else if (this.image3 == "") {
            this.image3 = url;
        }
        else if (this.image4 == "") {
            this.image4 = url;
        }
        else if (this.image5 == "") {
            this.image5 = url;
        }
        else if (this.image6 == "") {
            this.image6 = url;
        }
        else if (this.image7 == "") {
            this.image7 = url;
        }
        else if (this.image8 == "") {
            this.image8 = url;
        }
        else if (this.image9 == "") {
            this.image9 = url;
        }
        else if (this.image10 == "") {
            this.image10 = url;
        }
    }
}
