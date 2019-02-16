
package com.example.fyp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
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
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.example.fyp.Login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

    public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
            GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

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
//            initMap();
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
        private static final String TAG = "MainActivity";
        private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
        private static final int ERROR_DIALOG_REQUEST = 9001;
        private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
        private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
        private static final float DEFAULT_ZOOM = 15f;
        private static final int PLACE_PICKER_REQUEST = 1;
        private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
                //encompass the entire world
                new LatLng(-40,-168), new LatLng(71,136));



        //widgets
        private AutoCompleteTextView mSearchText;
        private ImageView mRecenter, mInfo, mPlacePicker;

        //vars
        private boolean mLocationPermissionGranted = false;
        private GoogleMap mMap;
        private FusedLocationProviderClient mFusedLocationProviderClient;
        private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
        private GoogleApiClient mGoogleApiClient;
        private GeoDataClient mGeoDataClient;
        private PlaceDetectionClient mPlaceDetectionClient;
        private PlaceInfo mPlace;
        private Marker mMarker;
        private DrawerLayout drawer;
        private FirebaseFirestore mDb;
        private UserLocation mUserLocation;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);
            mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
            mRecenter = (ImageView) findViewById(R.id.ic_recenter);
            mInfo = (ImageView) findViewById(R.id.place_info);
            mPlacePicker = (ImageView) findViewById(R.id.place_picker);
            mDb = FirebaseFirestore.getInstance();

            //getLocationPermission();
            initMap();



            /* -----------   Hamburger menu ----------------- */

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Log.d(TAG, "onCreate: Created");

            drawer = findViewById(R.id.drawer_layout);

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawer.addDrawerListener(toggle);
            //take care of rotating hamburger menu
            toggle.syncState();


            Log.d(TAG, "onCreate: synced");
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        private void getUserDetails (){

       //if (user == logged in)
            if (mUserLocation==null){
                //empty constructor in userloc
                mUserLocation = new UserLocation();
                DocumentReference userRef = mDb.collection(getString(R.string.collection_users))
                        .document(FirebaseAuth.getInstance().getUid()); //collection in firebase

                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if(task.isSuccessful()){
                             Log.d(TAG, "onComplete: succesfully added user details");
                             User user = task.getResult().toObject(User.class);
                             mUserLocation.setUser(user);
                             ((UserClient)getApplicationContext()).setUser(user);
                             getDeviceLocation();
                         }
                    }
                });
            }
            else{
                Log.d(TAG, "getUserDetails: mUserLocation is null");
                getDeviceLocation();
            }
            //else

        }

        //call this once get device location
        private void saveUserLocation(){
            //check to make sure not to pass null object
            if (mUserLocation != null){
                //mdb is the instantiated firebase                   user location in firebase (== "User Locations")
                DocumentReference locationRef = mDb.collection(getString(R.string.collection_user_locations))
                        .document(FirebaseAuth.getInstance().getUid()); //doc = IDed by id

                locationRef.set(mUserLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //task successfully inserted into DB
                        if (task.isSuccessful()){
                            Log.d(TAG, "saveUserLocation:  \n inserted into DB." +
                                    "lat: " + mUserLocation.getGeo_point().getLatitude() +
                                    "long: " + mUserLocation.getGeo_point().getLongitude());
                        }
                    }
                });
            }
        }

        @Override
        public void onBackPressed() {
            if (drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }

        }


        //geopoint  to store in firebase -- not to be confused with getdevicelocation

       /* private void getLastKnownLocation(){
            Log.d(TAG, "getLastKnownLocation: called last known location");
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()){
                        Location location = task.getResult();
                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "onComplete: latitude " + geoPoint.getLatitude());
                        Log.d(TAG, "onComplete: longitude " + geoPoint.getLongitude());

                    }
                }
            });
        }*/

        private boolean checkMapServices(){
            if(isServicesOK()){
                if(isMapsEnabled()){
                    return true;
                }
            }
            return false;
        }

        private void signOut(){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


        private void buildAlertMessageNoGps() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            //accepted or denied -> onActicityResuly
                            startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

        public boolean isMapsEnabled(){
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
                return false;
            }
            return true;
        }

        private void getLocationPermission() {

/*//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.*/

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
//            initMap();
                getDeviceLocation();
//                getUserDetails();
                // getChatrooms();
            } else {   //dialog to use location permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        public boolean isServicesOK(){
            Log.d(TAG, "isServicesOK: checking google services version");

            int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(com.example.fyp.MainActivity.this);

            if(available == ConnectionResult.SUCCESS){
                //everything is fine and the user can make map requests
                Log.d(TAG, "isServicesOK: Google Play Services is working");
                return true;
            }
            else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
                //an error occured but we can resolve it
                Log.d(TAG, "isServicesOK: an error occured but we can fix it");
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(com.example.fyp.MainActivity.this, available, ERROR_DIALOG_REQUEST);
                dialog.show();
            }else{
                Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String permissions[],
                                               @NonNull int[] grantResults) {
            mLocationPermissionGranted = false;
            switch (requestCode) {
                case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 //have results
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = true;
                        //    initMap();
                        getDeviceLocation();
//                        getUserDetails();
                    }
                }
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG, "onActivityResult: called.");
            switch (requestCode) {
                case PERMISSIONS_REQUEST_ENABLE_GPS: {
                    if(mLocationPermissionGranted){
                        //getChatrooms();
//                    initMap();
                        getDeviceLocation();
//                        getUserDetails();
                    }
                    else{
                        getLocationPermission();
                    }
                }
            }

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
//                initMap();
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
                        startActivityForResult(builder.build(com.example.fyp.MainActivity.this), PLACE_PICKER_REQUEST);
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
   /* protected void onActivityResult (int requestCode, int resultCode, Intent data){
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
    }*/


        // move to autocompleted search text location
        private void geoLocate(){
            Log.d(TAG, "geoLocate: geolocating");

            String searchString = mSearchText.getText().toString();
            Geocoder geocoder = new Geocoder(com.example.fyp.MainActivity.this);
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
                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){

                                Location currentLocation = (Location) task.getResult();
                                if (currentLocation!=null) {
                                    Log.d(TAG, "onComplete: found location! lat:" + currentLocation.getLatitude() + "long:" + currentLocation.getLongitude());
                                    GeoPoint geoPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                                    Log.d(TAG, "onComplete: lat: " + geoPoint.getLatitude());
                                    Log.d(TAG, "onComplete: long:" + geoPoint.getLongitude());
                                    //once device is found, save the location
                                   if (mUserLocation!=null) {
                                        mUserLocation.setGeo_point(geoPoint);
                                        mUserLocation.setTimestamp(null);
//                                       saveUserLocation();
                                    }
                                }

//                            assert currentLocation != null;
                                if (currentLocation!=null)
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                            DEFAULT_ZOOM,
                                            "My Location");

                            }
                            else{
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(com.example.fyp.MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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
//        if (latlng!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

            mMap.clear();

            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(com.example.fyp.MainActivity.this));

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
            Log.d(TAG, "moveCamera: moving camera to lat:" + latlng.latitude + ", lng:" + latlng.longitude+ ", latlng:" +latlng+ ", lng:" +zoom);
//        if (mMap!=null)
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

            mapFragment.getMapAsync(com.example.fyp.MainActivity.this);
        }

    /*private void getLocationPermission(){
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
    }*/

  /*  @Override
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
    }*/

        @Override
        protected void onResume(){
            super.onResume();
//        getDeviceLocation();
            if(checkMapServices()){
                if(mLocationPermissionGranted){
//                initMap();
                    getDeviceLocation();
//                    getUserDetails();
                  //  getLastKnownLocation();
                }
                else{
                    getLocationPermission();
                }
            }
        }

        @Override
        public void onStop(){
            super.onStop();
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.stopAutoManage(com.example.fyp.MainActivity.this);
                mGoogleApiClient.disconnect();
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

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.parkingfine:
                    Toast.makeText(this, "clicked on parking fine", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.findcar:
                    Toast.makeText(this, "find car", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.register:
                    Toast.makeText(this, "Sign up", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                    break;

                case R.id.login:
                    Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
                    Intent intentlogin = new Intent(MainActivity.this, Login.class);
                    startActivity(intentlogin);
                    break;

                case R.id.signout:
                    Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                    signOut();
            }

            drawer.closeDrawer(GravityCompat.START);

            //if false then no item selected
            return true;
        }
    }

