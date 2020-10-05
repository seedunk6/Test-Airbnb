package com.nextdots.airbnb.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mariexi Hostienda on 05/12/16.
 */
public class Listing implements Serializable{


    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }



    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    private String name;
    @SerializedName("property_type")
    private String property_type;
    @SerializedName("picture_url")
    private String picture_url;
    @SerializedName("room_type")
    private String room_type;
    @SerializedName("public_address")
    private String public_address;
    @SerializedName("bathrooms")
    private double bathrooms;
    @SerializedName("bedrooms")
    private int bedrooms;
    @SerializedName("beds")
    private int beds;
    @SerializedName("person_capacity")
    private int person_capacity;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    @SerializedName("id")
    private int id;
    @SerializedName("price")
    private String price;
    @SerializedName("native_currency")
    private String native_currency;
    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNative_currency() {
        return native_currency;
    }

    public void setNative_currency(String native_currency) {
        this.native_currency = native_currency;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getPerson_capacity() {
        return person_capacity;
    }

    public void setPerson_capacity(int person_capacity) {
        this.person_capacity = person_capacity;
    }

    public double getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(double bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getPublic_address() {
        return public_address;
    }

    public void setPublic_address(String public_address) {
        this.public_address = public_address;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }


    public Listing(String name, String property_type, String picture_url, String room_type, String public_address, double bathrooms, int bedrooms, int beds, int person_capacity, double lat, double lng) {
        this.name = name;
        this.property_type = property_type;
        this.picture_url = picture_url;
        this.room_type = room_type;
        this.public_address = public_address;
        this.bathrooms = bathrooms;
        this.bedrooms = bedrooms;
        this.beds = beds;
        this.person_capacity = person_capacity;
        this.lat = lat;
        this.lng = lng;
    }


}
