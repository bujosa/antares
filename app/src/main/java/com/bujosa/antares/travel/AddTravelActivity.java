package com.bujosa.antares.travel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bujosa.antares.R;
import com.bujosa.antares.utils.DatePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddTravelActivity extends AppCompatActivity{

    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    private Uri imageFile;
    private ImageView imageView;

    private EditText titleEditText, descriptionEditText, priceEditText;
    private EditText startDateEditText, endDateEditText,latitudeEditText, longitudeEditText;

    private TravelService travelService;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent = result.getData();

                        assert intent != null;

                        Uri selectedImageUri = intent.getData();

                        if (null != selectedImageUri) {
                            imageFile = selectedImageUri;
                            imageView.setImageURI(selectedImageUri);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        imageView = findViewById(R.id.form_travel_image);
        titleEditText = findViewById(R.id.form_travel_title);
        descriptionEditText = findViewById(R.id.form_travel_description);
        priceEditText = findViewById(R.id.form_travel_price);
        startDateEditText = findViewById(R.id.form_travel_start_date);
        endDateEditText = findViewById(R.id.form_travel_end_date);
        latitudeEditText = findViewById(R.id.form_travel_latitude);
        longitudeEditText = findViewById(R.id.form_travel_longitude);

        startDateEditText.setOnClickListener(l -> datePicker(startDateEditText));

        endDateEditText.setOnClickListener(l -> datePicker(endDateEditText));


        travelService = new TravelService(FirebaseFirestore.getInstance());
    }

    public void saveTravel(View view) {
        Travel travel = new Travel();

        String price = priceEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();

        travel.setTitle(titleEditText.getText().toString());
        travel.setDescription(descriptionEditText.getText().toString());
        travel.setPrice(price.isEmpty() ? 0 : Float.parseFloat(price));
        travel.setLatitude(latitude.isEmpty() ? 0 : Float.parseFloat(latitude));
        travel.setLongitude(longitude.isEmpty() ? 0 : Float.parseFloat(longitude));

        try {
            travel.setStartDate(formatter.parse(startDateEditText.getText().toString()));
            travel.setEndDate(formatter.parse(endDateEditText.getText().toString()));
        } catch (ParseException ignored) {}

        if (!travel.validate()) {
            Toast.makeText(getApplicationContext(),
                    "Porfavor rellanar los campos",
                    Toast.LENGTH_LONG).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("images/" + imageFile.getLastPathSegment());

        storageRef.putFile(imageFile).addOnSuccessListener(taskSnapshot -> {
            if (taskSnapshot.getMetadata() != null) {
                if (taskSnapshot.getMetadata().getReference() != null) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                        travel.setImage(downloadUrl.toString());
                        addTravel(travel);
                    });
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                "Ocurrio un error subiendo la foto " + e.getMessage(),
                Toast.LENGTH_LONG).show());
    }

    private void addTravel(Travel travel) {
        travelService.addTravel(travel).addOnSuccessListener(documentReference -> {
            setResult(RESULT_OK, null);
            finish();
        }).addOnFailureListener(e ->  Toast.makeText(getApplicationContext(),
                "Ocurrio un error guardando la data " + e.getMessage(),
                Toast.LENGTH_LONG).show());
    }

    private void datePicker(EditText editText) {
        DatePicker newDate = new DatePicker((year, month, day) -> editText.setText(String.format(Locale.getDefault(),
                "%d/%d/%d", month + 1, day, year)));

        newDate.show(getSupportFragmentManager(), "datePicker");
    }

    public void pickImage(View view) {
        Intent image = new Intent();
        image.setType("image/*");
        image.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(image, "Selecciona una imagen"));
    }



}