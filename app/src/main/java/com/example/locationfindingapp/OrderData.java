package com.example.locationfindingapp;

import java.util.HashMap;

public class OrderData {
    private String userId;
    private String userName;
    private String userEmail;
    private HashMap<String,String> item;
    private String price;
    private  String latitude;
    private String longitude;
    private String itemDetail;

    public OrderData(String userId, String userName, String userEmail, String price, String latitude, String longitude, String itemDetail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.itemDetail = itemDetail;
    }
    public OrderData(String userEmail, String price,String itemDetail) {
        this.userEmail = userEmail;
        this.price = price;
        this.itemDetail = itemDetail;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(String itemDetail) {
        this.itemDetail = itemDetail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public HashMap<String, String> getItem() {
        return item;
    }

    public void setItem(HashMap<String, String> item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}

