package com.bujosa.antares.travel;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.bujosa.antares.MainActivity;
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

    TextView title, secondTitle, description, price, startDate, endDate;
    ImageView imageView;
    Button buyButton, mapButton, imageButton;

    private static final int DEFAULT_ZOOM = 17;

    private Travel travel;

    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        travel = (Travel) getIntent().getSerializableExtra("Travel");

        title = findViewById(R.id.detailTitleTextView);
        secondTitle = findViewById(R.id.itemTextView);
        description = findViewById(R.id.detailDescriptionTextView);
        price = findViewById(R.id.detailPriceTextView);
        imageView = findViewById(R.id.detailImageView);
        startDate = findViewById(R.id.detailStartDateTextView);
        endDate = findViewById(R.id.detailEndDateTextView);
        buyButton = findViewById(R.id.buyButton);
        imageButton = findViewById(R.id.itemImageButton);
        mapButton = findViewById(R.id.itemMapButton);



        String priceResult = "" + travel.getPrice();
        Picasso.get()
                .load(travel.getImage())
                .placeholder(android.R.drawable.ic_dialog_map)
                .error(android.R.drawable.ic_dialog_alert)
                .into(imageView);

        title.setText(travel.getTitle());
        secondTitle.setText(travel.getTitle());
        description.setText(travel.getDescription());
        price.setText(priceResult);
        startDate.setText(formatter.format(travel.getStartDate()));
        endDate.setText(formatter.format(travel.getEndDate()));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);

        assert mapFragment != null;

        mapFragment.getMapAsync(this);

        buyButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(this,"Haz comprado este viaje", Toast.LENGTH_LONG);
            toast.show();
            SystemClock.sleep(2000);
            startActivity(new Intent(this,
                    MainActivity.class));
        });

        imageButton.setOnClickListener(view -> {
            findViewById(R.id.cardItemMapView).setVisibility(View.GONE);
            findViewById(R.id.cardItemView).setVisibility(View.VISIBLE);
        });

        mapButton.setOnClickListener(view -> {
            findViewById(R.id.cardItemMapView).setVisibility(View.VISIBLE);
            findViewById(R.id.cardItemView).setVisibility(View.GONE);
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(travel.getLatitude(), travel.getLongitude());

        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(travel.getTitle()));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
    }
}