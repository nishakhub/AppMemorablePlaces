package com.rajramchandani.appmemorableplaces;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location last=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                center(last,"your location");
            }
        }
    }

    public void center(Location location, String title) {
        LatLng userlocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userlocation).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 10));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        Intent intent = getIntent();
        if (intent.getIntExtra("position", 0) == 0) {

            //zoom in on users location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    center(location, "your location");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (Build.VERSION.SDK_INT < 23) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            else
            {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    Location last=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    center(last,"your location");
                }
                else
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
            }
        }

        else
        {
            Location place=new Location(LocationManager.GPS_PROVIDER);
            place.setLatitude(MainActivity.locations.get(intent.getIntExtra("position",0)).latitude);
            place.setLongitude(MainActivity.locations.get(intent.getIntExtra("position",0)).longitude);


            center(place,MainActivity.arr.get(intent.getIntExtra("position",0)));

        }


    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String add="";
        try {
            List<Address> list=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(list!=null && list.size()>0)
            {
                if(list!=null &&list.size()>0)
                {
                    if(list.get(0).getSubThoroughfare()!=null)
                    {
                        add+=list.get(0).getSubThoroughfare()+", ";
                    }
                    if(list.get(0).getThoroughfare()!=null)
                    {
                        add+=list.get(0).getThoroughfare()+", ";
                    }
                    if(list.get(0).getSubLocality()!=null)
                    {
                        add+=list.get(0).getSubLocality()+", ";
                    }
                    if(list.get(0).getAddressLine(1)!=null)
                    {
                        add+=list.get(0).getAddressLine(1)+", ";
                    }
                    if(list.get(0).getSubAdminArea()!=null)
                    {
                        add+=list.get(0).getSubAdminArea()+", ";
                    }
                    if(list.get(0).getLocality()!=null)
                    {
                        add+=list.get(0).getLocality()+", ";
                    }
                    if(list.get(0).getAdminArea()!=null)
                    {
                        add+=list.get(0).getAdminArea()+", ";
                    }

                    if(list.get(0).getCountryName()!=null)
                    {
                        add+=list.get(0).getCountryName()+", ";
                    }
                    if(list.get(0).getPostalCode()!=null)
                    {
                        add+=list.get(0).getPostalCode();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(add));
        Toast.makeText(this,"your memorable place added!!",Toast.LENGTH_SHORT).show();
        MainActivity.arr.add(add);
        MainActivity.locations.add(latLng);
        MainActivity.adapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.rajramchandani.appmemorableplaces",Context.MODE_PRIVATE);
        ArrayList<String > latitude=new ArrayList<String>();
        ArrayList<String > longitude=new ArrayList<String>();
        for(LatLng coordinates:MainActivity.locations)
        {
            latitude.add(Double.toString(coordinates.latitude));
            longitude.add(Double.toString(coordinates.longitude));
        }
        try {
            sharedPreferences.edit().putString("arr",ObjectSerializer.serialize(MainActivity.arr)).apply();
            sharedPreferences.edit().putString("latitude",ObjectSerializer.serialize(latitude)).apply();
            sharedPreferences.edit().putString("longitude",ObjectSerializer.serialize(longitude)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}




