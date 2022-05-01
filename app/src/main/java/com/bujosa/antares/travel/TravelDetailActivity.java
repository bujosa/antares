package com.bujosa.antares.travel;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bujosa.antares.MainActivity;
import com.bujosa.antares.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class TravelDetailActivity extends AppCompatActivity {

    TextView title, secondTitle, description, price, startDate, endDate;
    ImageView imageView;
    Button buyButton;

    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);

        Travel travel = (Travel) getIntent().getSerializableExtra("Travel");

        title = findViewById(R.id.detailTitleTextView);
        secondTitle = findViewById(R.id.itemTextView);
        description = findViewById(R.id.detailDescriptionTextView);
        price = findViewById(R.id.detailPriceTextView);
        imageView = findViewById(R.id.detailImageView);
        startDate = findViewById(R.id.detailStartDateTextView);
        endDate = findViewById(R.id.detailEndDateTextView);
        buyButton = findViewById(R.id.buyButton);

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

        buyButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(this,"Haz comprado este viaje", Toast.LENGTH_LONG);
            toast.show();
            SystemClock.sleep(2000);
            startActivity(new Intent(this,
                    MainActivity.class));
        });

    }
}