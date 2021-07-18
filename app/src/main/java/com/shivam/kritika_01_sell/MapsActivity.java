package com.shivam.kritika_01_sell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.shivam.kritika_01_sell.Upload_New_Product.GPS_REQUEST_CODE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int DEFAULT_ZOOM = 20;
    public static final int PERMISSION_REQUEST_CODE = 9001;
    private static final int PLAY_SERVICES_ERROR_CODE = 9002;
    public static final String TAG = "MyTag";
    private boolean mLocationPermissionGranted;
    private LocationCallback mLocationCallback;

    private FusedLocationProviderClient mLocationClient;

    Toolbar toolbar;

    Double LATITUDE, LONGITUDE;
    String GetLat,GetLong;
    private double Delhi_LAT = 28.630597;
    private double Delhi_LONG = 77.218978;

    String CityName;
    String ShopAddress;

    private GoogleMap mGoogleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        isServicesOk();

        mLocationClient = new FusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                Log.d(TAG, "Location Updates are : " + location.getLatitude() + " , " + location.getLongitude());
                gotoLocation(location.getLatitude(), location.getLongitude());
                LATITUDE = location.getLatitude();
                LONGITUDE = location.getLongitude();
                showMarker(location.getLatitude(), location.getLongitude());
                geoCoder(LATITUDE,LONGITUDE);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
      //  mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void initGoogleMap() {

        if (isServicesOk()) {
            if (isGPSEnabled()) {
                if (checkLocationPermission()) {
                  //  SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();

                   // getCurrentLocation();
                    getLocationUpdates();
                } else {
                    requestLocationPermission();
                }
            }
        }


    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void gotoLocation(double lat, double lng) {

        LatLng latLng = new LatLng(lat, lng);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);

        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private boolean checkLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

    }

    private boolean isServicesOk() {

        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();

        int result = googleApi.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApi.isUserResolvableError(result)) {
            Dialog dialog = googleApi.getErrorDialog(this, result, PLAY_SERVICES_ERROR_CODE, task ->
                    Toast.makeText(this, "Dialog is cancelled by User", Toast.LENGTH_SHORT).show());
            dialog.show();
        } else {
            Toast.makeText(this, "Play services are required by this application", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void showMarker(double lat, double lng) {
        mGoogleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        mGoogleMap.addMarker(markerOptions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        GetLat=String.valueOf(LATITUDE);
        GetLong=String.valueOf(LONGITUDE);


        SharedPreferences sp = getSharedPreferences("LoginInfos", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("GETLAT",GetLat);
        editor.putString("GETLONG",GetLong);
        editor.putString("Locality",CityName);
        editor.putString("SubLocality",ShopAddress);
        editor.commit();

    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if (task.isSuccessful()) {
                    Location location = task.getResult();

                    assert location != null;
                    Log.d(TAG, "Map 2 , " + String.valueOf(location.getLatitude()) + location.getLongitude());

                    LATITUDE = location.getLatitude();
                    LONGITUDE = location.getLongitude();
                    gotoLocation(LATITUDE, LONGITUDE);
                    showMarker(LATITUDE, LONGITUDE);
                    //  LocationEditText.setText(MyLat+","+MyLong);
                    // geoCoder(location.getLatitude(),location.getLongitude());

                } else {
                    Log.d(TAG, "getCurrentLocation: Error: " + task.getException().getMessage());
                    Toast.makeText(MapsActivity.this, "Can't get Location", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void geoCoder(double LAt, double LOng) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        try {

            //28.628170, 77.208960
            //28.630597, 77.218978

            //   double delhi_LONG = 77.218978;
            //   double delhi_LAT = 28.630597;
            List<Address> addressList = geocoder.getFromLocation(LAt, LOng, 1);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

              //  Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();

                CityName=address.getLocality();
                ShopAddress=address.getSubLocality();
                /*
                 (CityName.equals("")){
                  //  CityNameEditText.setText(address.getLocality());
                }

                if (ShopAddress.equals("")){
                    ShopAddressEditText.setText(address.getSubLocality());
                }

                 */

                Log.d(TAG, "geoLocate: Locality: " + address.getLocality() + " " + address.getSubLocality());
            }

            for (Address address : addressList) {
                Log.d(TAG, "geoLocate: Address: " + address.getAddressLine(address.getMaxAddressLineIndex()));
            }


        } catch (IOException e) {


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);


        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.CurrentLocation) {

            initGoogleMap();


        }

        return super.onOptionsItemSelected(item);


    }

    private boolean isGPSEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (providerEnabled) {
            return true;
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permissions")
                    .setMessage("GPS is required for accessing the Shop location. Please enable GPS.")
                    .setPositiveButton("OK", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();

        }
        return false;
    }

    private void getLocationUpdates() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);  // 1000*10 = 10 seconds
        locationRequest.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationCallback != null) {
            mLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }
}

  /*
        Intent intent = new Intent(MapsActivity.this, Upload_New_Product.class);
        intent.putExtra("Latitude",LATITUDE);
        intent.putExtra("Longitude",LONGITUDE);
        startActivity(intent);
        finish();

         */

       /*
        Intent intent = new Intent();
        intent.putExtra("Latitude",LATITUDE);
        intent.putExtra("Longitude",LONGITUDE);
        setResult(Activity.RESULT_OK, intent);
        finish();

        */