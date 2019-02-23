package com.example.fyp;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class MarkerLocation {
    private GeoPoint geo_point;
    //pass null to timestamp = insert timesnap instantly
    private @ServerTimestamp Date timestamp;
    private ParkingDetails parkingDetails;
    private User user;

    public MarkerLocation(GeoPoint geo_point, Date timestamp, User user) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.user = user;
    }

    public MarkerLocation() {

    }


}


