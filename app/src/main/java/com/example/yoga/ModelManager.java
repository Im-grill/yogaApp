package com.example.yoga;

public class ModelManager {
    private String id;
    private String name;
    private String yogaId;
    private String comment;
    private String image;
    private String addedTime;

    public ModelManager(String id, String name, String yogaId, String comment, String image, String addedTime) {
        this.id = id;
        this.name = name;
        this.yogaId = yogaId;
        this.comment = comment;
        this.image = image;
        this.addedTime = addedTime;
    }

    public ModelManager() {

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

    public String getYogaId() {
        return yogaId;
    }

    public void setyogaId(String yogaId) {
        this.yogaId = yogaId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }
}
