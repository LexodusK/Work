package com.example.fyp;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


//Test to insert data into database, not used in final implementation

public class UserLocation {

    private GeoPoint geo_point;
    //pass null to timestamp = insert timesnap instantly
    private @ServerTimestamp Date timestamp;
    private User user;

    public UserLocation(GeoPoint geo_point, Date timestamp, User user) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.user = user;
    }

    public UserLocation() {

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

    //for debugging in log
    @Override
    public String toString() {
        return "UserLocation{" +
                "geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }
}
