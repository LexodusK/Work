package com.example.fyp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        //if permission is granted, then proceed
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            {
                         return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    //global values
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            //encompass the entire world
            new LatLng(-40,-168), new LatLng(71,136));


    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mRecenter, mInfo, mPlacePicker;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private PlaceInfo mPlace;
    private Marker mMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mRecenter = (ImageView) findViewById(R.id.ic_recenter);
        mInfo = (ImageView) findViewById(R.id.place_info);
        mPlacePicker = (ImageView) findViewById(R.id.place_picker);

        getLocationPermission();


    }
    
    //initialize 
    private void init(){
        Log.d(TAG, "init: initializing");


        // auto text completion

            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();

            mSearchText.setOnItemClickListener(mAutoCompleteClickListener);

            mGeoDataClient = Places.getGeoDataClient(this, null);
            mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);



        //mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute method for searching (geolocate)
                    geoLocate();

                }

                return false;
            }
        });

        //recenter icon
        mRecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked Recenter icon");
                getDeviceLocation();
            }
        });

        //clicked on extra info to prompt the additional info
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked place info");
                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }else{
                        Log.d(TAG, "onClick: Place info " + mPlace.toString());
                        mMarker.showInfoWindow();
                    }
                }catch(NullPointerException e){
                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
                }
            }
        });
        //clicked on map icon to reveal surrounding things
        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesNotAvailableException: " + e.getMessage() );
                }
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void  onMapLongClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                        ));
            }
        });

        HideSoftKeyboard();
    }
    //clicked on map icon to reveal surrounding things
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK) {
                //place object
                Place place = PlacePicker.getPlace(this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                //submit request                  //create actual callback
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }



    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            //give list of addresses, max 1
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException " + e.getMessage() );
        }
            //if have list of addresses.  get only first position of list. only going to be one 
        if (list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location" + address.toString());
           // Toast.makeText(this, address.toString() , Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    //get device location
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            //if granted, then proceed
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        }
                        else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage() );
        }
    }

    private void moveCamera (LatLng latlng, float zoom, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving camera to lat:" + latlng.latitude + ", lng:" + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

        if (placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                                 "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                                 "Website:" + placeInfo.getWebsiteUri() + "\n" +
                                 "Price Rating " + placeInfo.getRating() + "\n";

                //pass the snippet to the marker
                MarkerOptions options = new MarkerOptions()
                        .position(latlng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                //need global marker object - need onclick listener
                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException" + e.getMessage() );
            }
        }
        //if null
        else{
            mMap.addMarker(new MarkerOptions().position(latlng));
        }


        HideSoftKeyboard();
    }

    private void moveCamera (LatLng latlng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving camera to lat:" + latlng.latitude + ", lng:" + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latlng)
                .title(title);

        mMap.addMarker(options);

        HideSoftKeyboard();
    }

    private void initMap (){
        Log.d(TAG, "initMap: initializing map ");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permission");
        //ask for permission to turn on GPS geolocation

        String[] permissions= {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                //if permission granted, initialize map again
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }

        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Called");
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                //some kind of permission was granted           if 1st permission was granted then good
                if (grantResults.length > 0){
                    for (int i=0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionGranted = true;
                    //initialize the map
                    initMap();
                }
            }
        }
    }

    private void HideSoftKeyboard(){
      //  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromInputMethod(mSearchText.getWindowToken(), 0);
         this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /* ---- Auto Complete google places API suggestions ----
            */
     private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            HideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            // get place ID and submit place API
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
                      //submit request                  //create actual callback
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

     private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
         @Override
         public void onResult(@NonNull PlaceBuffer places) {
            //callback to get all information on the place //receive request and callback
             if (!places.getStatus().isSuccess()){
                 Log.d(TAG, "onResult: Place query did not complete successfully" + places.getStatus().toString());
                 //must release placebuffer to prevent memory leak
                 places.release();
                 return;
             }
             final Place place = places.get(0);

             try{
                 mPlace = new PlaceInfo();
                 mPlace.setName(place.getName().toString());
                 mPlace.setAddress(place.getAddress().toString());
                 //mPlace.setAttributions(place.getAttributions().toString());
                 mPlace.setId(place.getId());
                 mPlace.setLatlng(place.getLatLng());
                 mPlace.setRating(place.getRating());
                 mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                 mPlace.setWebsiteUri(place.getWebsiteUri());

                 Log.d(TAG, "onResult:  Place" + mPlace.toString());
             }catch (NullPointerException e){
                 Log.e(TAG, "onResult: NullPointerException" + e.getMessage() );
             }
                //get camera for the title
                moveCamera(new LatLng(place.getViewport().getCenter().latitude
                ,place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

             /*Log.d(TAG, "onResult: Place Details" + place.getAttributions());
             Log.d(TAG, "onResult: Place Details" + place.getViewport());
             Log.d(TAG, "onResult: Place Details" + place.getPhoneNumber());
             Log.d(TAG, "onResult: Place Details" + place.getWebsiteUri());
             Log.d(TAG, "onResult: Place Details" + place.getId());
             Log.d(TAG, "onResult: Place Details" + place.getAddress());
             Log.d(TAG, "onResult: Place Details" + place.getLatLng());*/

             //mPlace = place;
                places.release();
         }
     };

}

