package com.example.fyp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyp.models.FloatingMarkerTitlesOverlay;
import com.example.fyp.models.PlaceInfo;
import com.example.fyp.ParkingDetails;
import com.example.fyp.R.layout.*;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.example.fyp.AddParkingInfo;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.example.fyp.Images;


import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import uk.co.senab.photoview.PhotoViewAttacher;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener,
        AddCancelParkingBottomSheet.BottomSheetListener, AddParkingInfo.BottomSheetListener,
        parkinginfo_with_edit_delete.BottomSheetListener, parkinginfo_without_editdelete.BottomSheetListener,
        EditParkingInfo.BottomSheetListener, ClusterManager.OnClusterInfoWindowClickListener<MarkerCluster>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MarkerCluster>//, AddParkingInfo.OnDataPass
{

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");


        mMap = googleMap;
        updateMapWithDatabaseMarker();


//            urlpaam6 =  "https://firebasestorage.googleapis.com/v0/b/fypproject.appspot.com/o/ParkingLotB%2Fpbam6.png?alt=media&token=b3816b07-120a-462d-bc04-ae3b7723c32e";
//            urlpapm12 = "https://firebasestorage.googleapis.com/v0/b/fypproject.appspot.com/o/ParkingLotB%2Fpbpm12.png?alt=media&token=56d8e9d4-066f-4b5b-9a2f-993ec78459b1";






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
        private FusedLocationProviderClient fusedLocationClient;
        private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
        private static final int ERROR_DIALOG_REQUEST = 9001;
        private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
        private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
        private static final float DEFAULT_ZOOM = 15f;
        private static final int PLACE_PICKER_REQUEST = 1;
        private static GeoPoint onmaplongclickedpoint;
        private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
                //encompass the entire world
                new LatLng(-40,-168), new LatLng(71,136));

        //widgets
        private AutoCompleteTextView mSearchText;
        private ImageView mRecenter, mInfo, mPlacePicker, mSatelliteView, mSortMarker, mShadedImage, mCancelShadedParking;
        private Button mShadedButton;
        private SeekBar mSeekBar;
        private TextView mTextSeekBar;

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
        private ParkingDetails mParkingDetails;
        private FloatingMarkerTitlesOverlay floatingMarkersOverlay;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private TextInputLayout textInputTitle;
        private TextInputLayout textInputDescription;
        private TextView mTextView;
//        private TextView mtesttext;
        private String mtesttextemailstring;
        private TextView emaildisp;
        //        private TextView emaildisplay;
        private ArrayList mMarkersList;
        private ArrayList mFilteredList;
        private ArrayList mSearchableMarkers;
        private ArrayList mShadedParkingLotsZone;
        private CheckBox mShaded;
        private ClusterManager<MarkerCluster> mClusterManager;
        private String shadedGlobal = "Shaded Parking lot";
        private String freeGlobal = "Free Parking lot";
        private String paidGlobal = "Paid Parking lot";
        private String mspGlobal = "Multistorey Parking lot";
        private String disabledGlobal = "Disabled Parking";
        private String evGlobal = "Electric vehicle lot";
        private String noParking = "No Parking";
        protected MarkerCluster clickedClusterItem;

        private String urlfitam6;  private String urlpaam6;  private String urlpbam6;  private String urlpcam6;
        private String urlfitam7;  private String urlpaam7;  private String urlpbam7;  private String urlpcam7;
        private String urlfitam8;  private String urlpaam8;  private String urlpbam8;  private String urlpcam8;
        private String urlfitam9;  private String urlpaam9;  private String urlpbam9;  private String urlpcam9;
        private String urlfitam10; private String urlpaam10; private String urlpbam10; private String urlpcam10;
        private String urlfitam11; private String urlpaam11; private String urlpbam11; private String urlpcam11;
        private String urlfitpm12;  private String urlpapm12; private String urlpbpm12; private String urlpcpm12;
        private String urlfitpm1;  private String urlpapm1;  private String urlpbpm1;  private String urlpcpm1;
        private String urlfitpm2;  private String urlpapm2;  private String urlpbpm2;  private String urlpcpm2;
        private String urlfitpm3;  private String urlpapm3;  private String urlpbpm3;  private String urlpcpm3;
        private String urlfitpm4;  private String urlpapm4;  private String urlpbpm4;  private String urlpcpm4;
        private String urlfitpm5;  private String urlpapm5;  private String urlpbpm5;  private String urlpcpm5;
        private String urlfitpm6;  private String urlpapm6;  private String urlpbpm6;  private String urlpcpm6;


        SpinnerDialog spinnerDialog;
        SpinnerDialog spinnerForShaded;
        Images mImage;

//        String[] menutitles;
//        TypedArray menuIcons;
//
//        // nav drawer title
//        private CharSequence mDrawerTitle;
//        private CharSequence mTitle;
//        private DrawerLayout mDrawerLayout;
//        private ListView mDrawerList;
//        private ActionBarDrawerToggle mDrawerToggle;
//        private List<RowItem> rowItems;
//        private CustomAdapter adapter;
//


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);
            mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
            mRecenter = (ImageView) findViewById(R.id.ic_recenter);
//            mInfo = (ImageView) findViewById(R.id.place_info);
            mPlacePicker = (ImageView) findViewById(R.id.place_picker);
            mShadedButton = (Button) findViewById(R.id.buttonForShaded);
            mShadedImage = (ImageView) findViewById(R.id.imageForShaded);
            mTextSeekBar = (TextView) findViewById(R.id.textForseekbar);
            mCancelShadedParking = (ImageView) findViewById(R.id.imageForCancelShadedParking);
            mSeekBar = (SeekBar) findViewById(R.id.seekbar);
            mDb = FirebaseFirestore.getInstance();
            textInputTitle = findViewById(R.id.Title);
            textInputDescription = findViewById(R.id.Description);
            mSatelliteView = (ImageView) findViewById(R.id.ic_terrain);
            mSortMarker = (ImageView)findViewById(R.id.ic_sortmarker);
            NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navView.getHeaderView(0);
            emaildisp  = (TextView) headerView.findViewById(R.id.varying_email);
            mMarkersList = new ArrayList();
            mFilteredList = new ArrayList();
            mSearchableMarkers = new ArrayList();
            mShadedParkingLotsZone = new ArrayList();

            View checkbox = (View) navView.getMenu();
            mShaded = findViewById(R.id.shadedcheck);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            Images imageURL = new Images();
            urlfitam6 = imageURL.getUrlFITam6();    urlpaam6 = imageURL.getUrlpaam6();    urlpbam6 = imageURL.getUrlpbam6();    urlpcam6 = imageURL.getUrlpcam6();
            urlfitam7 = imageURL.getUrlFITam7();    urlpaam7 = imageURL.getUrlpaam7();    urlpbam7 = imageURL.getUrlpbam7();    urlpcam7 = imageURL.getUrlpcam7();
            urlfitam8 = imageURL.getUrlFITam8();    urlpaam8 = imageURL.getUrlpaam8();    urlpbam8 = imageURL.getUrlpbam8();    urlpcam8 = imageURL.getUrlpcam8();
            urlfitam9 = imageURL.getUrlFITam9();    urlpaam9 = imageURL.getUrlpaam9();    urlpbam9 = imageURL.getUrlpbam9();    urlpcam9 = imageURL.getUrlpcam9();
            urlfitam10 = imageURL.getUrlFITam10();  urlpaam10 = imageURL.getUrlpaam10();  urlpbam10 = imageURL.getUrlpbam10();  urlpcam10 = imageURL.getUrlpcam10();
            urlfitam11 = imageURL.getUrlFITam11();  urlpaam11 = imageURL.getUrlpaam11();  urlpbam11 = imageURL.getUrlpbam11();  urlpcam11 = imageURL.getUrlpcam11();
            urlfitpm12 = imageURL.getUrlFITpm12();  urlpapm12 = imageURL.getUrlpapm12();  urlpbpm12 = imageURL.getUrlpbpm12();  urlpcpm12 = imageURL.getUrlpcpm12();
            urlfitpm1 = imageURL.getUrlFITpm1();    urlpapm1 = imageURL.getUrlpapm1();    urlpbpm1 = imageURL.getUrlpbpm1();    urlpcpm1 = imageURL.getUrlpcpm1();
            urlfitpm2 = imageURL.getUrlFITpm2();    urlpapm2 = imageURL.getUrlpapm2();    urlpbpm2 = imageURL.getUrlpbpm2();    urlpcpm2 = imageURL.getUrlpcpm2();
            urlfitpm3 = imageURL.getUrlFITpm3();    urlpapm3 = imageURL.getUrlpapm3();    urlpbpm3 = imageURL.getUrlpbpm3();    urlpcpm3 = imageURL.getUrlpcpm3();
            urlfitpm4 = imageURL.getUrlFITpm4();    urlpapm4 = imageURL.getUrlpapm4();    urlpbpm4 = imageURL.getUrlpbpm4();    urlpcpm4 = imageURL.getUrlpcpm4();
            urlfitpm5 = imageURL.getUrlFITpm5();    urlpapm5 = imageURL.getUrlpapm5();    urlpbpm5 = imageURL.getUrlpbpm5();    urlpcpm5 = imageURL.getUrlpcpm5();
            urlfitpm6 = imageURL.getUrlFITpm6();    urlpapm6 = imageURL.getUrlpapm6();    urlpbpm6 = imageURL.getUrlpbpm6();    urlpcpm6 = imageURL.getUrlpcpm6();

            spinnerDialog = new SpinnerDialog(MainActivity.this, mSearchableMarkers, "Find Marker");
            spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
//                    moveCamera();
                    ParkingDetails p = new ParkingDetails();
                    Toast.makeText(MainActivity.this, "Selected " + item , Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: " + position);
                    Log.d(TAG, "onClick: " + mMarkersList.get(position));
                    p = (ParkingDetails) mMarkersList.get(position);

//                    moveCamera(new LatLng(p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude()), DEFAULT_ZOOM);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude, point.longitude), 20f));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude()), DEFAULT_ZOOM) );

                }
            });

            if (FirebaseAuth.getInstance().getUid()==null) {
                final String text = "Help to improve the service by contributing by signing up!";
                emaildisp.setText(text);
            }

            //getLocationPermission();
            initMap();

            /* -----------   Hamburger menu ----------------- */

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Log.d(TAG, "onCreate: Created");

            drawer = findViewById(R.id.drawer_layout);

//            NavigationView navigationView = findViewById(R.id.nav_view);
            navView.setNavigationItemSelectedListener(this);


            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawer.addDrawerListener(toggle);
            //take care of rotating hamburger menu
            toggle.syncState();

//            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
            MenuItem checkboxSP = navView.getMenu().findItem(R.id.shadedcheck);
            MenuItem checkboxFP = navView.getMenu().findItem(R.id.freecheck);
            MenuItem checkboxPP = navView.getMenu().findItem(R.id.paidcheck);
            MenuItem checkboxMSP = navView.getMenu().findItem(R.id.multistoreycheck);
            MenuItem checkboxDP = navView.getMenu().findItem(R.id.disabledcheck);
            MenuItem checkboxEVP = navView.getMenu().findItem(R.id.electriccheck);

            CompoundButton checkboxSPView = (CompoundButton) MenuItemCompat.getActionView(checkboxSP);
            checkboxSPView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b){
                        Toast.makeText(MainActivity.this, "Filtered Shaded Areas", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        shadedGlobal = "uncheck";
                        Toast.makeText(MainActivity.this, "Unfiltered Shaded Areas", Toast.LENGTH_SHORT).show();
                    }
//                    filterMarkers();

                }
            });

            // FREE
            CompoundButton checkboxFPView = (CompoundButton) MenuItemCompat.getActionView(checkboxFP);
            checkboxFPView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mMap.clear();
                    if (b){
                        freeGlobal = "Free Parking lot";
                        Toast.makeText(MainActivity.this, "Filtered Free Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        freeGlobal = "uncheck";
//                        Marker marker = (Marker) mMarkersList.
                        Toast.makeText(MainActivity.this, "Unfiltered Free Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    filterMarkers();
                    updateMapWithFilteredMarkers();

                }
            });

            // PAID
            CompoundButton checkboxPPView = (CompoundButton) MenuItemCompat.getActionView(checkboxPP);
            checkboxPPView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mMap.clear();
                    if (b){
                        paidGlobal = "Paid Parking lot";
                        Toast.makeText(MainActivity.this, "Filtered Paid Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        paidGlobal = "uncheck";
                        Toast.makeText(MainActivity.this, "Unfiltered Paid Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    filterMarkers();
                    updateMapWithFilteredMarkers();

                }
            });

            // MULTISTOREY
            CompoundButton checkboxMSPView = (CompoundButton) MenuItemCompat.getActionView(checkboxMSP);
            checkboxMSPView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mMap.clear();
                    if (b){
                        mspGlobal = "Multistorey Parking lot";
                        Toast.makeText(MainActivity.this, "Filtered Multistorey Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mspGlobal = "uncheck";
                        Toast.makeText(MainActivity.this, "Unfiltered Multistorey Parking Lots", Toast.LENGTH_SHORT).show();

                    }
                    filterMarkers();
                    updateMapWithFilteredMarkers();

                }
            });

            //DISABLED
            CompoundButton checkboxDPView = (CompoundButton) MenuItemCompat.getActionView(checkboxDP);
            checkboxDPView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mMap.clear();
                    if (b){
                        disabledGlobal = "Disabled Parking";
                        Toast.makeText(MainActivity.this, "Filtered Disabled Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        disabledGlobal = "uncheck";
                        Toast.makeText(MainActivity.this, "Unfiltered Disabled Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    filterMarkers();
                    updateMapWithFilteredMarkers();

                }
            });

            // ELECTRIC VEHICLE
            CompoundButton checkboxEVPView = (CompoundButton) MenuItemCompat.getActionView(checkboxEVP);
            checkboxEVPView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mMap.clear();
                    if (b){
                        evGlobal = "Electric vehicle lot";
                        Toast.makeText(MainActivity.this, "Filtered EV Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        evGlobal = "uncheck";
                        Toast.makeText(MainActivity.this, "Unfiltered EV Parking Lots", Toast.LENGTH_SHORT).show();
                    }
                    filterMarkers();
                    updateMapWithFilteredMarkers();

                }
            });

            mSeekBar.setVisibility(View.INVISIBLE);
            mTextSeekBar.setVisibility(View.INVISIBLE);
            mCancelShadedParking.setVisibility(View.INVISIBLE);
            mShadedImage.setVisibility(View.INVISIBLE);


            mSeekBar.incrementProgressBy(1);
            mSeekBar.setMax(12);

            clickShadedButton();
            hideAllShaded();

//            rowItems = new ArrayList<RowItem>();
//            mShaded.setChecked(true);

            Log.d(TAG, "onCreate: synced");
            getSupportActionBar().setDisplayShowTitleEnabled(false);

 //end onCreate
        }

        private void hideAllShaded(){
          mCancelShadedParking.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (mShadedImage.getVisibility() == view.VISIBLE) {
                                        mShadedImage.setVisibility(view.INVISIBLE);
                                        mSeekBar.setVisibility(view.INVISIBLE);
                                        mTextSeekBar.setVisibility(view.INVISIBLE);
                                        mCancelShadedParking.setVisibility(view.INVISIBLE);
                  }
              }
          });
        }

    private void findCarLocation (){
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
                    }
                }
            });
    }

       /* private void findCarLocation (){


    LocationManager locationManager;
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
    }

    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
        Log.d(TAG, "onMapReady: called this network provider method");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double longit = location.getLongitude();
                LatLng latLng = new LatLng(lat, longit);
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List <Address> addressList = geocoder.getFromLocation(lat, longit, 1);
//                        String str = addressList.get(0).getLocality();
//                        str += addressList.get(0).getCountryName();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onMapReady: called GPS provider method");
                double lat = location.getLatitude();
                double longit = location.getLongitude();
                LatLng latLng = new LatLng(lat, longit);
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List <Address> addressList = geocoder.getFromLocation(lat, longit, 1);
//                        String str = addressList.get(0).getLocality();
//                        str += addressList.get(0).getCountryName();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }
}*/


        private void clickShadedButton (){
            mShadedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {



                    mShadedParkingLotsZone.clear();
                    mShadedParkingLotsZone.add("FIT Parking");
                    mShadedParkingLotsZone.add("Parking Lot Zone A ( FSTS, FIT, FENG )");
                    mShadedParkingLotsZone.add("Parking Lot Zone B ( DeTAR, Chancellory, FLC )");
                    mShadedParkingLotsZone.add("Parking Lot Zone C ( FSS, FACA, FCSHD, FMHS, CAIS )");

                    spinnerForShaded = new SpinnerDialog(MainActivity.this, mShadedParkingLotsZone, "Select Shaded Parking Lot Zone");
                    spinnerForShaded.showSpinerDialog();
                    spinnerForShaded.bindOnSpinerListener(new OnSpinerItemClick() {



                        @Override
                        public void onClick(String item, final int position) {
                            Toast.makeText(MainActivity.this, "Selected " + item , Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClick: " + position);
                            PhotoViewAttacher photoView = new PhotoViewAttacher(mShadedImage);
                            photoView.update();



                                mSeekBar.setProgress(0);

                                if (position == 0 || position == 1 || position ==2 || position == 3){
                                    if (mShadedImage.getVisibility() == view.INVISIBLE){
                                        mShadedImage.setVisibility(view.VISIBLE);
                                        mSeekBar.setVisibility(view.VISIBLE);
                                        mTextSeekBar.setVisibility(view.VISIBLE);
                                        mCancelShadedParking.setVisibility(view.VISIBLE);
                                        Log.d(TAG, "onClick:  visible");
                                    }


                                    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                                            switch (progress){
                                                case 0:
                                                    progress = 6;
                                                    mTextSeekBar.setText(" " + progress + " am");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitam6).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpaam6).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbam6).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcam6).into(mShadedImage);
                                                    }

                                                    break;
                                                case 1:
                                                    progress = 7;
                                                    mTextSeekBar.setText(" " + progress + " am");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitam7).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpaam7).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbam7).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcam7).into(mShadedImage);
                                                    }
                                                    break;
                                                case 2:
                                                    progress = 8;
                                                    mTextSeekBar.setText(" " + progress + " am");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitam8).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpaam8).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbam8).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcam8).into(mShadedImage);
                                                    }
                                                    break;
                                                case 3:
                                                    progress = 9;
                                                    mTextSeekBar.setText(" " + progress + " am");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitam9).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpaam9).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbam9).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcam9).into(mShadedImage);
                                                    }
                                                    break;
                                                case 4:
                                                    progress = 10;
                                                    mTextSeekBar.setText(" " + progress + " am");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitam10).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpaam10).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbam10).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcam10).into(mShadedImage);
                                                    }
                                                    break;
                                                case 5:
                                                    progress = 11;
                                                    mTextSeekBar.setText(" " + progress + " am");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitam11).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpaam11).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbam11).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcam11).into(mShadedImage);
                                                    }
                                                    break;
                                                case 6:
                                                    progress = 12;
                                                    mTextSeekBar.setText(" " + progress + " pm");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitpm12).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpapm12).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbpm12).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcpm12).into(mShadedImage);
                                                    }
                                                    break;
                                                case 7:
                                                    progress = 1;
                                                    mTextSeekBar.setText(" " + progress + " pm");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitpm1).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpapm1).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbpm1).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcpm1).into(mShadedImage);
                                                    }
                                                    break;
                                                case 8:
                                                    progress = 2;
                                                    mTextSeekBar.setText(" " + progress + " pm");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitpm2).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpapm2).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbpm2).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcpm2).into(mShadedImage);
                                                    }
                                                    break;
                                                case 9:
                                                    progress = 3;
                                                    mTextSeekBar.setText(" " + progress + " pm");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitpm3).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpapm3).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbpm3).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcpm3).into(mShadedImage);
                                                    }
                                                    break;
                                                case 10:
                                                    progress = 4;
                                                    mTextSeekBar.setText(" " + progress + " pm");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitpm4).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpapm4).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbpm4).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcpm4).into(mShadedImage);
                                                    }
                                                    break;
                                                case 11:
                                                    progress = 5;
                                                    mTextSeekBar.setText(" " + progress + " pm");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitpm5).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpapm5).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbpm5).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcpm5).into(mShadedImage);
                                                    }
                                                    break;
                                                case 12:
                                                    progress = 6;
                                                    mTextSeekBar.setText(" " + progress + " pm");
                                                    if (position == 0){
                                                        Glide.with(getApplicationContext()).load(urlfitpm6).into(mShadedImage);
                                                    }else if (position==1){
                                                        Glide.with(getApplicationContext()).load(urlpapm6).into(mShadedImage);
                                                    }else if (position == 2){
                                                        Glide.with(getApplicationContext()).load(urlpbpm6).into(mShadedImage);
                                                    }else if (position == 3){
                                                        Glide.with(getApplicationContext()).load(urlpcpm6).into(mShadedImage);
                                                    }
                                                    break;
                                            }

                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });
                                }

                            }


                    });

                    Toast.makeText(MainActivity.this, "called image", Toast.LENGTH_SHORT).show();

//                    PhotoViewAttacher photoView = new PhotoViewAttacher(mShadedImage);
//                    if (mShadedImage.getVisibility() == view.INVISIBLE){
//                        photoView.update();
//                        mShadedImage.setVisibility(view.VISIBLE);
//                        mSeekBar.setVisibility(view.VISIBLE);
//                        mTextSeekBar.setVisibility(view.VISIBLE);
//                        mTextImage.setVisibility(view.VISIBLE);
//                        Log.d(TAG, "onClick:  visible");
//                    }else if (mShadedImage.getVisibility() == view.VISIBLE) {
//                        mShadedImage.setVisibility(view.INVISIBLE);
//                        mSeekBar.setVisibility(view.INVISIBLE);
//                        mTextSeekBar.setVisibility(view.INVISIBLE);
//                        mTextImage.setVisibility(view.INVISIBLE);
//
//                        Log.d(TAG, "onClick: invisible");
//                    }
                }
            });
        }

/*
        private void setUpCluster (){
            IconGenerator iconFactory = new IconGenerator(MainActivity.this);
//            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));
            mClusterManager = new ClusterManager<MarkerCluster>(this, mMap);
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);
            mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
            mMap.setOnInfoWindowClickListener(mClusterManager);
            mClusterManager.setOnClusterItemInfoWindowClickListener(this);
           mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerCluster>() {
               @Override
               public boolean onClusterItemClick(MarkerCluster markerCluster) {
                   clickedClusterItem = markerCluster;
                   return false;
               }
           });

//           mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());

            ClusterManager clusterManager = new ClusterManager<>(getApplicationContext(),mMap);
            ClusterRenderer clusterRenderer = new ClusterRenderer(getApplicationContext(), mMap, clusterManager );
            MarkerOptions markerOptions = null;
            for (int i=0; i<mMarkersList.size(); i++){
                ParkingDetails p = (ParkingDetails) mMarkersList.get(i);
                markerOptions = new MarkerOptions().position(new LatLng(p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude()));
//                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(p.getTypeofParking())));
                MarkerCluster markerCluster = new MarkerCluster(markerOptions);
                clusterRenderer.onBeforeClusterItemRendered( markerCluster,markerOptions);
                clusterManager.addItem(markerCluster);
            }
        }*/

        private void filterMarkers() {

//                setUpCluster();

//            Log.d(TAG, "onMapReady: abde" + markerCluster + markerOptions);

            mFilteredList.clear();
            for (int i=0; i<mMarkersList.size(); i++){
                ParkingDetails pd = (ParkingDetails) mMarkersList.get(i);
                if (pd.getTypeofParking().equals(shadedGlobal) || pd.getTypeofParking().equals(freeGlobal)
                        || pd.getTypeofParking().equals(paidGlobal) || pd.getTypeofParking().equals(mspGlobal)
                        || pd.getTypeofParking().equals(disabledGlobal) || pd.getTypeofParking().equals(evGlobal)){
                    mFilteredList.add(((ParkingDetails) mMarkersList.get(i)));
                    Log.d(TAG, "onCreate: parkingtype is " + pd.getTypeofParking() + pd.getTitle());
//                    Log.d(TAG, "filterMarkers: " + mFilteredList);
                }

            }
        }



        private GoogleMap getMap(){
            return mMap;
        }

        private void setUpClusterer(){
            //position the map
            mMap.clear();
//            mClusterManager.clearItems();
//            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));
            mClusterManager = new ClusterManager<MarkerCluster>(this, getMap());
            getMap().setOnCameraIdleListener(mClusterManager);
            getMap().setOnMarkerClickListener(mClusterManager);
            getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Bundle bundle = new Bundle();
                    ParkingDetails pDet = new ParkingDetails();


                    try{

                        for (int i = 0; i < mFilteredList.size(); i++) {
                            pDet = (ParkingDetails) mFilteredList.get(i);
                            LatLng currentMarker = new LatLng(pDet.getGeo_point().getLatitude(), pDet.getGeo_point().getLongitude());
                            String currentMarkerTitle = new String(pDet.getTitle());
                            Log.d(TAG, "onMarkerClick: " + currentMarker + " " + currentMarkerTitle + " " + marker.getPosition());
                            //if the user's marker clicked on the saved in DB marker location
                            if (marker.getPosition().longitude == currentMarker.longitude &&
                                    marker.getPosition().latitude == currentMarker.latitude) {

//                               Snackbar.make(findViewById(android.R.id.content), currentMarkerTitle, Snackbar.LENGTH_LONG).show();
//                               Toast.makeText(MainActivity.this, "this is " + currentMarkerTitle, Toast.LENGTH_SHORT).show();
                                bundle.putSerializable("key", pDet);
                                break;
                            }
                            else {
//                                    return true;
//                                return false;
                            }
                        }
                    }catch (NullPointerException e){
                        Log.d(TAG, "onMarkerClick: " + e.getMessage());
                    }


//                    ParkingDetails pDet = new ParkingDetails();
//                    for (int i = 0;i<mMarkersList.size();i++){
//                        pDet = (ParkingDetails) mMarkersList.get(i);
//                        LatLng currentUser = new LatLng(pDet.getGeo_point().getLatitude(), pDet.getGeo_point().getLongitude());
//                        Log.d(TAG, "onMapReady: putted marker" + pDet.getGeo_point() + "size" + mMarkersList.size());
//                        mMap.addMarker(new MarkerOptions().position(currentUser)).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(pDet.getTitle())));
//                    }

                    if (FirebaseAuth.getInstance().getUid() != null){
                     if (!FirebaseAuth.getInstance().getUid().equals(pDet.getUser_id().toString())){
                        parkinginfo_without_editdelete bottomSheet = new parkinginfo_without_editdelete();
                        bottomSheet.setArguments(bundle);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    }
                    else if (FirebaseAuth.getInstance().getUid().equals(pDet.getUser_id().toString())){
                        parkinginfo_with_edit_delete bottomSheet = new parkinginfo_with_edit_delete();
                        bottomSheet.setArguments(bundle);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    }
                    }else
                    {
                        parkinginfo_without_editdelete bottomSheet = new parkinginfo_without_editdelete();
                        bottomSheet.setArguments(bundle);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    }

//                    Toast.makeText(MainActivity.this, "Clicked on marker", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(android.R.id.content), "clickity click", Snackbar.LENGTH_LONG).show();
//                    marker.remove();
                    return false;
                }






            });
            try{
                readItems();
                Log.d(TAG, "setUpClusterer: read the items");
            }catch (JSONException e){
                Log.d(TAG, "setUpClusterer: failed to read");
                Toast.makeText(this, "can't read markers", Toast.LENGTH_SHORT).show();
            }
        }

//        private void readItems() throws JSONException {
//            InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
//            List<MarkerCluster> items = new MarkerClusterReader().read(inputStream);
//            mClusterManager.addItems(items);
//
//        }

        private void readItems() throws JSONException{
            double lat = 37.41704700015034;
            double lng = -122.074730444444;
            Log.d(TAG, "readItems: abc" + mMarkersList.size());

            mClusterManager.clearItems();

            for (int i = 0; i< mFilteredList.size(); i++ ){
//                double offset = i/60d;
//                lat = lat + offset;
//                lng = lng + offset;

//                ClusterManager clusterManager = new ClusterManager<>(getApplication(),mMap);
                ClusterRenderer clusterRenderer = new ClusterRenderer(getApplication(), mMap, mClusterManager );
//                MarkerOptions markerOptions = null;
//                for (int i=0; i<mMarkersList.size(); i++){
//                    ParkingDetails p = (ParkingDetails) mMarkersList.get(i);
//                    markerOptions = new MarkerOptions().position(new LatLng(p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude()));
////                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(p.getTypeofParking())));
//                    MarkerCluster markerCluster = new MarkerCluster(markerOptions);
//                    clusterRenderer.onBeforeClusterItemRendered( markerCluster,markerOptions);
//                    clusterManager.addItem(markerCluster);
//                }
//            }

                ParkingDetails p = (ParkingDetails) mFilteredList.get(i);
                   IconGenerator iconFactory = new IconGenerator(MainActivity.this);
//                List<MarkerCluster> items = new MarkerClusterReader().read();
//                mMap.addMarker(new MarkerOptions().position(currentUser)).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(pDet.getTitle())));
                LatLng currentUser = new LatLng(p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                                                .position(currentUser)
                                                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(p.getTitle())));


//                mMap.addMarker(markerOptions);
//                MarkerCluster offsetItem = new MarkerCluster(p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude());
                MarkerCluster off = new MarkerCluster(markerOptions);
                Log.d(TAG, "readItems:dd " + off.getIcon());
                clusterRenderer.onBeforeClusterItemRendered(off, markerOptions);
//                MarkerCluster offsetItem = new MarkerCluster(lat, lng);
//                mMap.addMarker(new MarkerOptions().position()).setIcon();
//                  MarkerCluster offsetItem = new MarkerCluster(p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude(), p.getTitle(), p.getDescription());
                mClusterManager.addItem(off);
//                mClusterManager.getMarkerCollection();
                Log.d(TAG, "readItems: " + mMarkersList.size());
            }

        }








        private void getUserDetails (){



            //if (user == logged in) then save to database
            Log.d(TAG, "getUserDetails: ID is " + FirebaseAuth.getInstance().getUid());
         if (FirebaseAuth.getInstance().getUid()!=null){

            if (mUserLocation==null){
                //empty constructor in userloc
                mUserLocation = new UserLocation();
                DocumentReference userRef = mDb.collection(getString(R.string.collection_users))
                        .document(FirebaseAuth.getInstance().getUid()); //collection in firebase

//                final String text = "ss";

                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if(task.isSuccessful()){
                             Log.d(TAG, "onComplete: succesfully added user details");
                             User user = task.getResult().toObject(User.class);
                             mUserLocation.setUser(user);
                             ((UserClient)getApplicationContext()).setUser(user);

                             DocumentSnapshot documentSnapshot = task.getResult();
                             Log.d(TAG, "onComplete: " + documentSnapshot.getString("email") );
                             mtesttextemailstring = documentSnapshot.getString("email");
                             Log.d(TAG, "onComplete: " + mtesttextemailstring);
//                             mtesttext.setText(mtesttextemailstring);
                             if (FirebaseAuth.getInstance().getUid()!=null)
                                 emaildisp.setText(mtesttextemailstring);


//                             mtesttext.setText("ASDASDADSd");


                             getDeviceLocation();

                         }
                    }
                });
            }
            else{
                Log.d(TAG, "getUserDetails: mUserLocation is null");
                getDeviceLocation();
            }

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

              //  getDeviceLocation();
               getUserDetails();

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
//                        getDeviceLocation();
                        getUserDetails();
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
//                        getDeviceLocation();
                        getUserDetails();
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

            mSatelliteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
                        mMap.setMapType(mMap.MAP_TYPE_HYBRID);
                    }
                   else if(mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID){
                        mMap.setMapType(mMap.MAP_TYPE_NORMAL);
                    }
                }
            });

            mSortMarker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinnerDialog.showSpinerDialog();
                }
            });

            //clicked on extra info to prompt the additional info
            /*mInfo.setOnClickListener(new View.OnClickListener() {
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
            });*/


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

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                Bundle bundle = new Bundle();


                @Override

                public boolean onMarkerClick(Marker marker) {
                    //entire class
                    ParkingDetails pDet = new ParkingDetails();


                        try{

                            for (int i = 0; i < mMarkersList.size(); i++) {
                            pDet = (ParkingDetails) mMarkersList.get(i);
                            LatLng currentMarker = new LatLng(pDet.getGeo_point().getLatitude(), pDet.getGeo_point().getLongitude());
                            String currentMarkerTitle = new String(pDet.getTitle());
                            Log.d(TAG, "onMarkerClick: " + currentMarker + " " + currentMarkerTitle + " " + marker.getPosition());
                            //if the user's marker clicked on the saved in DB marker location
                            if (marker.getPosition().longitude == currentMarker.longitude &&
                                    marker.getPosition().latitude == currentMarker.latitude) {

//                               Snackbar.make(findViewById(android.R.id.content), currentMarkerTitle, Snackbar.LENGTH_LONG).show();
//                               Toast.makeText(MainActivity.this, "this is " + currentMarkerTitle, Toast.LENGTH_SHORT).show();
                                bundle.putSerializable("key", pDet);
                                break;
                            }
                            else {
//                                    return true;
//                                return false;
                            }
                        }
                        }catch (NullPointerException e){
                            Log.d(TAG, "onMarkerClick: " + e.getMessage());
                        }


//                    ParkingDetails pDet = new ParkingDetails();
//                    for (int i = 0;i<mMarkersList.size();i++){
//                        pDet = (ParkingDetails) mMarkersList.get(i);
//                        LatLng currentUser = new LatLng(pDet.getGeo_point().getLatitude(), pDet.getGeo_point().getLongitude());
//                        Log.d(TAG, "onMapReady: putted marker" + pDet.getGeo_point() + "size" + mMarkersList.size());
//                        mMap.addMarker(new MarkerOptions().position(currentUser)).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(pDet.getTitle())));
//                    }

                    if (FirebaseAuth.getInstance().getUid().equals(pDet.getUser_id())){
                        parkinginfo_with_edit_delete bottomSheet = new parkinginfo_with_edit_delete();
                        bottomSheet.setArguments(bundle);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    }else{
                        parkinginfo_without_editdelete bottomSheet = new parkinginfo_without_editdelete();
                        bottomSheet.setArguments(bundle);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    }

//                    Toast.makeText(MainActivity.this, "Clicked on marker", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(android.R.id.content), "clickity click", Snackbar.LENGTH_LONG).show();
//                    marker.remove();
                    return false;

                }
            });

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {

                if (FirebaseAuth.getInstance().getUid()!=null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude, point.longitude), 20f));
//                    moveCamera(new LatLng(point.latitude, point.longitude), 20f );
//

                    onmaplongclickedpoint = new GeoPoint(point.latitude, point.longitude);
//
//
                    IconGenerator iconFactory = new IconGenerator(MainActivity.this);
                    Marker tMarker = mMap.addMarker(new MarkerOptions().position(point));
                    tMarker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Marker")));
//
                    MarkerLocation mLoc = new MarkerLocation();
                    mLoc.setMarker(tMarker);
                    Bundle markerRemove = new Bundle();
                    markerRemove.putSerializable("marker", mLoc);

//
                    AddCancelParkingBottomSheet bottomSheet = new AddCancelParkingBottomSheet();
                    bottomSheet.setArguments(markerRemove);
                    bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
//
                     mMarkersList.add(point);
                }
                //only users who login can add a marker
                else{
                    Toast.makeText(MainActivity.this, "Login to add", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                }
            });

            HideSoftKeyboard();
        }


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
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM));
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
                                       saveUserLocation();
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

        private void moveCamera (LatLng latlng, float zoom){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
        }



        private void initMap (){
            Log.d(TAG, "initMap: initializing map ");
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(com.example.fyp.MainActivity.this);
        }


        @Override
        protected void onResume(){
            super.onResume();
//        getDeviceLocation();
            if(checkMapServices()){
                if(mLocationPermissionGranted){
//
//                    getDeviceLocation();
                    getUserDetails();

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

        private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback =
                new ResultCallback<PlaceBuffer>() {
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
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://payportal.unimas.my/epayment/onlinefine/index2.jsp"));
                        startActivity(intent);
                    }catch (NullPointerException e){
                        Toast.makeText(this, "No app can handle this request", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onNavigationItemSelected: " + e.getMessage() );
                    }

                    break;

                case R.id.findcar:
                    Toast.makeText(this, "find car", Toast.LENGTH_SHORT).show();
                    findCarLocation();
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


        // Bottom sheet listener with parking details from there sent here to be stored in database
        //add to database
        @Override
        public void onButtonClicked(String t, String d, String type, String spaces) {
            mMap.clear();
            String input = "Marker title added as " + t;
            Snackbar.make(findViewById(android.R.id.content), input, Snackbar.LENGTH_LONG).show();
//            mMarkersList.add(t);
//            mMarkersList.add(d);
            mParkingDetails = new ParkingDetails();
//            mParkingDetails.setTypeofParking("Shaded");

            if (mParkingDetails != null){
                //mdb is the instantiated firebase                   user location in firebase (== "User Locations")


                CollectionReference locationRef = mDb.collection(getString(R.string.collection_users)).document(FirebaseAuth.getInstance().getUid())
                        .collection(getString(R.string.collection_parking_details)); //doc = IDed by id
                //locationRef.add(mParkingDetails);
                Log.d(TAG, "onButtonClicked: user doc id: "+ FirebaseAuth.getInstance().getUid());
                mParkingDetails.setDescription(d);
                mParkingDetails.setTitle(t);
                mParkingDetails.setTypeofParking(type);
                mParkingDetails.setNumberofParkingspots(spaces);
//                mParkingDetails.setGeo_point(mUserLocation.getGeo_point());
                mParkingDetails.setGeo_point(onmaplongclickedpoint);
                mParkingDetails.setUser_id(FirebaseAuth.getInstance().getUid());
                mParkingDetails.setMarker_id(locationRef.document().getId());


                Log.d(TAG, "onButtonClicked: collection "+locationRef.get());

//                DocumentReference docRef = locationRef.document(locationRef.document().getId());
                DocumentReference docRef = locationRef.document(mParkingDetails.getMarker_id());
                Log.d(TAG, "onButtonClicked: parking id: "+ docRef.getId());
                docRef.set(mParkingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //task successfully inserted into DB
                        if (task.isSuccessful()){
                            Log.d(TAG, "saveUserLocation:  \n inserted into DB." +
                                    "title : " + mParkingDetails.getTitle() +
                                    "desc : " + mParkingDetails.getDescription());
                        }
                    }
                });
                mMap.clear();
                updateMapWithDatabaseMarker();

            }

        }

        //edit added marker
        @Override
        public void onButtonClickedEdit(String t, String d, String type, String spaces, String id, GeoPoint point) {
            CollectionReference locationRef = mDb.collection(getString(R.string.collection_users)).document(FirebaseAuth.getInstance().getUid())
                    .collection(getString(R.string.collection_parking_details));
            mMap.clear();
            mParkingDetails = new ParkingDetails();
            mParkingDetails.setDescription(d);
            mParkingDetails.setTitle(t);
            mParkingDetails.setTypeofParking(type);
            mParkingDetails.setNumberofParkingspots(spaces);
            mParkingDetails.setUser_id(FirebaseAuth.getInstance().getUid());
            mParkingDetails.setMarker_id(id);
            mParkingDetails.setGeo_point(point);


            DocumentReference docRef = locationRef.document(id);
            docRef.set(mParkingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: " + mParkingDetails.getTitle()
                                                       + mParkingDetails.getDescription());
                    }
                }
            });


            updateMapWithDatabaseMarker();

        }

        // save a particular marker's long lat with the type, title and description
                                                         // lat  lng     type           title                   description
        // index the five items as one key e.g. Array[0] = {14, -122, Free Parking, FCSIT Student Parking, For students only}

        public void updateMapWithFilteredMarkers(){
            mMap.clear();
            setUpClusterer();

           /* for(int i=0; i<mFilteredList.size(); i++){
                ParkingDetails p = (ParkingDetails) mFilteredList.get(i);

//                IconGenerator iconFactory = new IconGenerator(MainActivity.this);
//                LatLng filteredMarker = new LatLng (p.getGeo_point().getLatitude(), p.getGeo_point().getLongitude());
//                mMap.addMarker(new MarkerOptions().position(filteredMarker)).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(p.getTitle())));
            }*/
        }

        public void updateMapWithDatabaseMarker(){
            mMap.clear();
            mMarkersList.clear();
            mSearchableMarkers.clear();

            CollectionReference locationRef = mDb.collection(getString(R.string.collection_users));; //doc = IDed by id    //outer loop thru Users > ID
                final CollectionReference innerLocationRef = mDb.collection(getString(R.string.collection_users));        //inner loop Users > ID > Parking Details > ID
                locationRef.get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {   //outer loop thru Users > ID
//                                        ParkingDetails padet = task.getResult().toObject(ParkingDetails.class);
                                        innerLocationRef.document(document.getId())
                                                .collection(getString(R.string.collection_parking_details)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                if (task.isSuccessful()) {

                                                    for (QueryDocumentSnapshot innerDocument : task.getResult()) {  //inner loop Users > ID > Parking Details > ID
                                                        Log.d(TAG, innerDocument.getId() + " idd => " + innerDocument.toObject(ParkingDetails.class));
                                                        ParkingDetails padet = innerDocument.toObject(ParkingDetails.class);
                                                        mMarkersList.add(padet);
                                                        IconGenerator iconFactory = new IconGenerator(MainActivity.this);
//                                                        LatLng currentUser = new LatLng(padet.getGeo_point().getLatitude(), padet.getGeo_point().getLongitude());
//                                                        Log.d(TAG, "onMapReady: putted marker" + padet.getGeo_point() + "size" + mMarkersList.size());
//                                                        mMap.addMarker(new MarkerOptions().position(currentUser)).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(padet.getTitle())));

                                                        mSearchableMarkers.add(padet.getTitle());
                                                        QuerySnapshot qs = (QuerySnapshot) task.getResult();
                                                        List qsl = qs.getDocuments();
//                                                        qsl.get(task.getResult().size()-1);

                                                        Log.d(TAG, "onComplete: abdd" + task.getResult().equals(task.getResult()));
                                                        if (innerDocument.equals(qsl.get(task.getResult().size()-1))){

                                                            filterMarkers();
                                                            setUpClusterer();
                                                            mMap.clear();
                                                            /*for(int i=0; i<mFilteredList.size(); i++) {
                                                                ParkingDetails pd = (ParkingDetails) mFilteredList.get(i);
                                                                LatLng plotMarkers = new LatLng(pd.getGeo_point().getLatitude(), pd.getGeo_point().getLongitude());

//                                                              mMap.addMarker(new MarkerOptions().position(plotMarkers)).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(pd.getTitle())));
                                                            }*/
                                                        }

//                                                        mFilterableMarkers.add(padet.getTypeofParking());



                                                    }
                                                    Log.d(TAG, "onComplete: aaaa" + mSearchableMarkers.size());
                                                    Log.d(TAG, "on click: Array is now " + mMarkersList.toString() + "\n");
                                                    Log.d(TAG, "on click: Array size is now " + mMarkersList.size() + "\n");
//                                                    Log.d(TAG, "onComplete: abcd" + mFilterableMarkers);

                                                    // should be not the right way, adding exponentially more markers
                                                  /*  for (int i = 0;i<mMarkersList.size();i++){
                                                        pDet = (ParkingDetails) mMarkersList.get(i);
                                                        LatLng currentUser = new LatLng(pDet.getGeo_point().getLatitude(), pDet.getGeo_point().getLongitude());
                                                        Log.d(TAG, "onMapReady: putted marker" + pDet.getGeo_point() + "size" + mMarkersList.size());
                                                        mMap.addMarker(new MarkerOptions().position(currentUser)).setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(pDet.getTitle())));

                                                            mSearchableMarkers.add(pDet.getTitle());


                                                    }*/



                                                    }

                                            }
                                        });

                                    }




                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
        }




        @Override
        public void onDismiss(boolean bool) {
            if (mMarkersList.get(mMarkersList.size()-1).getClass() == Marker.class) {
                Marker marker = (Marker) mMarkersList.get(mMarkersList.size() - 1);
                marker.remove();
                marker.remove();
                marker.setVisible(false);
                mMarkersList.remove(mMarkersList.size() - 1);
                Log.d(TAG, "onDismiss: clik");
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(camLat,camLong), DEFAULT_ZOOM));
            }
        }


        @Override
        public void onDelete() {

            updateMapWithDatabaseMarker();
            mMap.clear();
        }


        @Override
        public void onClusterInfoWindowClick(Cluster<MarkerCluster> cluster) {

        }

        @Override
        public void onClusterItemInfoWindowClick(MarkerCluster markerCluster) {
//            Intent placesIntent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
//            String reference = item.getReference();
//
//            placesIntent.putExtra("name", item.getTitle());
//            placesIntent.putExtra("reference", reference);
//            placesIntent.putExtra("sourcelat", myLatitude);
//            placesIntent.putExtra("sourcelng", myLongitude);
//            startActivity(placesIntent);
        }
    }

