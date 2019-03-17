package com.example.fyp;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import static android.support.constraint.Constraints.TAG;

public class ClusterRenderer extends DefaultClusterRenderer<MarkerCluster> {

    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MarkerCluster> clusterManager) {
        super(context, map, clusterManager);
        clusterManager.setRenderer(this);
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerCluster markerItem, MarkerOptions markerOptions) {
        if (markerItem.getIcon() != null) {
            Log.d(TAG, "onBeforeClusterItemRendered: markeritem is null");
            markerOptions.icon(markerItem.getIcon()); //Here you retrieve BitmapDescriptor from ClusterItem and set it as marker icon
        }
        Log.d(TAG, "onBeforeClusterItemRendered: marker item has smth" + markerItem.getIcon());
        markerOptions.visible(true);
    }
}

