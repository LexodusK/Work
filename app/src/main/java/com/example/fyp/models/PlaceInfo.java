package com.example.fyp.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    //to retain info when places.release is called
    /*needed because places.release should also remove the place data
     that we are getting in result ,
     bcz its necessary to release the place object at the end (otherwise memory overload),
     we don' t want to lose our search data ,
     that's why another class was made and set everything to these variables,
     so even after releasing the place object the data
     remain save in these variables﻿*/
    private String name;
    private String address;
    private String phoneNumber;
    private String Id;
    private Uri websiteUri;
    private LatLng latlng;
    private float rating;
    private String attributions;

    public PlaceInfo(String name, String address, String phoneNumber,
                     String id, Uri websiteUri, LatLng latlng,
                     float rating, String attributions) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        Id = id;
        this.websiteUri = websiteUri;
        this.latlng = latlng;
        this.rating = rating;
        this.attributions = attributions;
    }

    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", Id='" + Id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latlng=" + latlng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}


