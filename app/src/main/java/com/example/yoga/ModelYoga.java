package com.example.yoga;

public class ModelYoga {
    private String id;
    private String userEmail;
    private String name;
    private String location;
    private String date;
    private String parkingAvailable;
    private String length;
    private String difficulty;
    private String description;
    private String image;
    private String partners;
    private String addedTime;
    private String updateTime;

    public ModelYoga(String id, String userEmail, String name, String location, String date,
                     String parkingAvailable, String length, String difficulty,
                     String description, String image, String partners, String addedTime, String updateTime) {
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailable = parkingAvailable;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.image = image;
        this.partners = partners;
        this.addedTime = addedTime;
        this.updateTime = updateTime;
    }

    public ModelYoga() {

    }

    // Getter and Setter methods for each property

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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

    public String getPartner() {
        return partners;
    }

    public void setPartner(String partners) {
        this.partners = partners;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}

