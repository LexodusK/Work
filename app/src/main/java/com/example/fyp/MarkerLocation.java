package com.example.fyp;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class MarkerLocation implements Serializable {
    private Marker marker;
    //pass null to timestamp = insert timesnap instantly


    public MarkerLocation(Marker marker) {

    }

    public MarkerLocation() {

    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String toString() {
        return "MarkerLocation{" +
                "marker=" + marker +
                '}';
    }
}


