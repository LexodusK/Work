package com.example.fyp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        //pass context to inflater to custom layout to show up
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    //setting text to the view
    private void renderWindowText (Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
            //if title doesnt equal null, then title set textview to title
        if (!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);
        //if snippet doesnt equal null, then snippet set textview to snippet
        if (!snippet.equals("")){
            tvSnippet.setText(snippet);
        }



    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        Log.d(TAG, "getInfoWindow: clicked on ths");
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
