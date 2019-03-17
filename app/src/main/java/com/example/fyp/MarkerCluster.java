package com.example.fyp;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerCluster implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private BitmapDescriptor icon;

    public MarkerCluster(double lat, double lng){
        mPosition = new LatLng(lat, lng);
    }

    public MarkerCluster(double lat, double lng, String title, String snippet){
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;

    }

    public MarkerCluster(MarkerOptions markerOptions) {
        this.mPosition = markerOptions.getPosition();
        this.mTitle = markerOptions.getTitle();
        this.mSnippet = markerOptions.getSnippet();
        this.icon = markerOptions.getIcon();
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }
}
