package com.example.fyp;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ParkingDetails {


    private GeoPoint geo_point;
    //pass null to timestamp = insert timesnap instantly
    private @ServerTimestamp Date timestamp;
    private User user;
    private String typeofParking;
    private String numberofParkingspots;
    private String Title;
    private String Description;

    public ParkingDetails(String typeofParking, String numberofParkingspots, String Title, String Description,
                          GeoPoint geo_point, Date timestamp, User user) {
        this.typeofParking = typeofParking;
        this.numberofParkingspots = numberofParkingspots;
        this.Title = Title;
        this.Description = Description;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.user = user;
    }

    public ParkingDetails() {
    }

    public String getTypeofParking() {
        return typeofParking;
    }

    public void setTypeofParking(String typeofParking) {
        this.typeofParking = typeofParking;
    }

    public String getNumberofParkingspots() {
        return numberofParkingspots;
    }

    public void setNumberofParkingspots(String numberofParkingspots) {
        this.numberofParkingspots = numberofParkingspots;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ParkingDetails{" +
                "geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                ", user=" + user +
                ", typeofParking='" + typeofParking + '\'' +
                ", numberofParkingspots='" + numberofParkingspots + '\'' +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }
}
