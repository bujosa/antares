package com.bujosa.antares.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bujosa.antares.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class TravelActivity extends FragmentActivity implements OnMapReadyCallback {

    TextView title, description, price, startDate, endDate;
    ImageView imageView;
    Button  mapButton, imageButton, extendMapButton;

    private static final int DEFAULT_ZOOM = 5;

    private Travel travel;

    private GoogleMap googleMap;

    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        travel = (Travel) getIntent().getSerializableExtra("Travel");

        title = findViewById(R.id.detailTitleTextView);
        description = findViewById(R.id.detailDescriptionTextView);
        price = findViewById(R.id.detailPriceTextView);
        imageView = findViewById(R.id.detailImageView);
        startDate = findViewById(R.id.detailStartDateTextView);
        endDate = findViewById(R.id.detailEndDateTextView);
        imageButton = findViewById(R.id.itemImageButton);
        mapButton = findViewById(R.id.itemMapButton);
        extendMapButton = findViewById(R.id.extendMapButton);



        String priceResult = "" + travel.getPrice();
        Picasso.get()
                .load(travel.getImage())
                .placeholder(android.R.drawable.ic_dialog_map)
                .error(android.R.drawable.ic_dialog_alert)
                .into(imageView);

        title.setText(travel.getTitle());
        description.setText(travel.getDescription());
        price.setText(priceResult);
        startDate.setText(formatter.format(travel.getStartDate()));
        endDate.setText(formatter.format(travel.getEndDate()));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);

        assert mapFragment != null;

        mapFragment.getMapAsync(this);

        imageButton.setOnClickListener(view -> {
            findViewById(R.id.cardItemMapView).setVisibility(View.GONE);
            findViewById(R.id.cardItemView).setVisibility(View.VISIBLE);
        });

        mapButton.setOnClickListener(view -> {
            findViewById(R.id.cardItemMapView).setVisibility(View.VISIBLE);
            findViewById(R.id.cardItemView).setVisibility(View.GONE);
        });

        extendMapButton.setOnClickListener(view -> {
            showDialog();
            extendMapButton.setVisibility(View.GONE);
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(travel.getLatitude(), travel.getLongitude());

        this.googleMap = googleMap;

        this.googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(travel.getTitle()));

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
        requireLocationPermission();
        uiSettings();
    }

    public void buyTravel(View view){
        Toast.makeText(this,"Haz comprado este viaje", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE_LOCATION) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        uiSettings();
    }

    public void showDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View extendMap = getLayoutInflater().inflate(R.layout.extend_map, null);
        Button exitExtendMapButton = extendMap.findViewById(R.id.exitExtendMapButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.extendMapView);

        assert mapFragment != null;

        mapFragment.getMapAsync(this);

        exitExtendMapButton.setOnClickListener(l -> {
            mapFragment.onDestroyView();
            dialog.dismiss();
        });

        dialogBuilder.setView(extendMap);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void requireLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void uiSettings() {
        try {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                requireLocationPermission();
            }
        } catch (SecurityException ignored) {}
    }
}