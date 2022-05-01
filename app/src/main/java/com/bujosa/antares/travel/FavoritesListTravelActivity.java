package com.bujosa.antares.travel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bujosa.antares.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class FavoritesListTravelActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imageButton;
    TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_travels);

        imageButton=findViewById(R.id.imageButton);
        textView=findViewById(R.id.textView2);

        textView.setText("Lugares Favoritos");

        imageButton.setVisibility(View.INVISIBLE);
        recyclerView=findViewById(R.id.listRecyclerView);

        TravelService travelService = new TravelService(FirebaseFirestore.getInstance());

        FilterInputs filterInputs = new FilterInputs();

        filterInputs.setFavorite(true);

        travelService.loadTravels(filterInputs).addOnSuccessListener(documentSnapshots -> {
            List<Travel> types = documentSnapshots.toObjects(Travel.class);
            recyclerView.setAdapter(new TravelAdapter(types, this, false));
        }).addOnFailureListener(e -> showMessage("Error loading trips. Please verify. " + e.getMessage()));


        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
