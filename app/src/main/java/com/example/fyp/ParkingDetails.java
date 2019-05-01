package com.example.fyp;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class ParkingDetails implements Serializable {


    private GeoPoint geo_point;
    //pass null to timestamp = insert timesnap instantly
    private @ServerTimestamp Date timestamp;
    private String user_id;
    private String typeofParking;
    private String numberofParkingspots;
    private String Title;
    private String Description;
    private String marker_id;
    private String email;

    public ParkingDetails(String typeofParking, String numberofParkingspots, String Title, String Description,
                          GeoPoint geo_point, Date timestamp, String user_id, String marker_id, String email) {
        this.typeofParking = typeofParking;
        this.numberofParkingspots = numberofParkingspots;
        this.Title = Title;
        this.Description = Description;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.user_id = user_id;
        this.marker_id = marker_id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMarker_id() {
        return marker_id;
    }

    public void setMarker_id(String marker_id) {
        this.marker_id = marker_id;
    }

    @Override
    public String toString() {
        return "ParkingDetails{" +
                "geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                ", user_id='" + user_id + '\'' +
                ", typeofParking='" + typeofParking + '\'' +
                ", numberofParkingspots='" + numberofParkingspots + '\'' +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", marker_id='" + marker_id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
