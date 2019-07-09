package com.riyadhbank.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.riyadhbank.Custom.WorkaroundMapFragment;
import com.riyadhbank.Modal.LocationModal;
import com.riyadhbank.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class LocationMapFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    View view;
    LatLng move_lat_long;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    GoogleMap googleMap;
    Marker marker;
    private Location myLocation;
    Location mylocation;
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private boolean gps_enabled = false, network_enabled = false;

    ArrayList<LocationModal> locationArrayList;
    ScrollView scrollView;
    WorkaroundMapFragment workaroundMapFragment;
    SupportMapFragment supportMapFragment;

    public LocationMapFragment(ScrollView scrollView, ArrayList<LocationModal> locationArrayList) {
        this.scrollView = scrollView;
        this.locationArrayList = locationArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location_map, container, false);

        workaroundMapFragment = ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map));
        workaroundMapFragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        //supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(AppIndex.API).addApi(LocationServices.API).build();

        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpGoogleClient();
        gpsIsEnable();

        return view;

    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private void gpsIsEnable() {
        workaroundMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setPadding(0, 0, 0, 0);

                myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                /*if (myLocation != null) {
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                            .zoom(12)
                            .build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                    showCurrentLocation();
                }*/

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (marker != null) {
                            if (marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                            }
                        }
                    }
                });

                try {
                    LatLng latLng = new LatLng(Double.valueOf(locationArrayList.get(0).getLatitude()), Double.valueOf(locationArrayList.get(0).getLongitude()));
                    CameraPosition position = new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(6)
                            .build();

                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

                    for (int i = 0; i < locationArrayList.size(); i++) {
                        createMarker(Double.valueOf(locationArrayList.get(i).getLatitude()), Double.valueOf(locationArrayList.get(i).getLongitude()),
                                locationArrayList.get(i).getLocationname(), locationArrayList.get(i).getAddress(), R.drawable.ic_marker);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private synchronized void setUpGoogleClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    protected void createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
        move_lat_long = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(move_lat_long)
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }


    private void getMyLocation() {
        try {
            if (googleApiClient != null) {
                if (googleApiClient.isConnected()) {
                    int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                        mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(3000);
                        locationRequest.setFastestInterval(3000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest);
                        builder.setAlwaysShow(true);
                        LocationServices.FusedLocationApi
                                .requestLocationUpdates(googleApiClient, locationRequest, this);
                        PendingResult result =
                                LocationServices.SettingsApi
                                        .checkLocationSettings(googleApiClient, builder.build());
                        result.setResultCallback(new ResultCallback() {

                            @Override
                            public void onResult(@NonNull Result result) {
                                {
                                    final Status status = result.getStatus();
                                    switch (status.getStatusCode()) {
                                        case LocationSettingsStatusCodes.SUCCESS:
                                            int permissionLocation = ContextCompat
                                                    .checkSelfPermission(getActivity(),
                                                            Manifest.permission.ACCESS_FINE_LOCATION);
                                            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                                mylocation = LocationServices.FusedLocationApi
                                                        .getLastLocation(googleApiClient);
                                               /* CameraPosition position = new CameraPosition.Builder()
                                                        .target(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()))
                                                        .zoom(12)
                                                        .build();
                                                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));*/
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            try {
                                                status.startResolutionForResult(getActivity(),
                                                        REQUEST_CHECK_SETTINGS_GPS);
                                            } catch (IntentSender.SendIntentException e) {
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            break;
                                    }
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showCurrentLocation() {
        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            //To create marker in map
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Location");
            //adding marker to the map
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

}
