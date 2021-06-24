package com.example.locationfindingapp;

public class ShopData {
    private String id;
    private String name;
    private String description;
    private String price;
    private String numberAvailable;
    private String imgurl;

    public ShopData(String name, String description, String price, String numberAvailable,String imgurl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgurl=imgurl;
        this.numberAvailable = numberAvailable;
    }
    public ShopData(String id,String name, String description, String price, String numberAvailable,String imgurl) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.numberAvailable = numberAvailable;
        this.imgurl=imgurl;
    }
    public ShopData()
    {}

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getNumberAvailable() {
        return numberAvailable;
    }

    public void setNumberAvailable(String numberAvailable) {
        this.numberAvailable = numberAvailable;
    }
}
