package com.maxwellthomasadventureappproject2.phoneapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationCameraTransitionListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.maxwellthomasadventureappproject2.api.models.User;
import com.maxwellthomasadventureappproject2.api.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    MarkerView userMarker;
    ArrayList<Point> points = new ArrayList<>();
    Uri imageUri;
    LocationManager locationManager;
    ViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        findViewById(R.id.logout).setOnClickListener((view) -> {
            viewModel.signOut();
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }

                        MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);

                        GeoJsonSource source = new GeoJsonSource("points",
                                FeatureCollection.fromFeatures(new Feature[]{
                                        Feature.fromGeometry(
                                                LineString.fromLngLats(points)
                                        )
                                })
                        );

                        style.addSource(source);

                        style.addLayer(new LineLayer("line-layer", "points").withProperties(
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineColor(Color.parseColor("#0000ff"))
                        ));

                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                return true;
                            }
                        });

                        LocationComponent locationComponent = mapboxMap.getLocationComponent();

                        locationComponent.activateLocationComponent(
                                LocationComponentActivationOptions.builder(MainActivity.this, style)
                                        .useSpecializedLocationLayer(true)
                                        .build()
                        );

                        locationComponent.setLocationComponentEnabled(true);

                        locationComponent.setCameraMode(CameraMode.TRACKING_GPS_NORTH, new OnLocationCameraTransitionListener() {
                            @Override
                            public void onLocationCameraTransitionFinished(int cameraMode) {
                                locationComponent.zoomWhileTracking(17, 100);
                            }

                            @Override
                            public void onLocationCameraTransitionCanceled(int cameraMode) {

                            }
                        });

                        LocationComponentOptions options = locationComponent.getLocationComponentOptions().toBuilder()
                                .trackingGesturesManagement(true)
                                .trackingInitialMoveThreshold(500)
                                .build();

                        locationComponent.applyStyle(options);

                        locationComponent.addOnCameraTrackingChangedListener(new OnCameraTrackingChangedListener() {
                            @Override
                            public void onCameraTrackingDismissed() {
                                System.out.println("DONE TRACKING");
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(() -> {
                                        locationComponent.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
                                    });
                                }).start();
                            }

                            @Override
                            public void onCameraTrackingChanged(int currentMode) {
                                System.out.println("TRACKING CHANGED " + currentMode);
                            }
                        });

                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                points.add(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
                                source.setGeoJson(
                                        FeatureCollection.fromFeatures(new Feature[]{
                                                        Feature.fromGeometry(
                                                                LineString.fromLngLats(points)
                                                        )
                                                }
                                        )
                                );
                            }

                        });

                        findViewById(R.id.fab).setOnClickListener((view) -> {
                            // create a file pointer
                            Location location;
                            ContentResolver resolver = getContentResolver();
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "my_image_"+timeStamp+".jpg");
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                            // tell the camera app to store the image at that file pointer
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 0);
                            // add image to main view
                            if( mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
                                ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setImageURI(imageUri);
                                imageView.setMaxHeight(48);
                                imageView.setMaxWidth(48);
                                LatLng photoLocation =  new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(),  mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
                                userMarker = new MarkerView(photoLocation, imageView);
                                markerViewManager.addMarker(userMarker);
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}