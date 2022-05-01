package com.bujosa.antares.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bujosa.antares.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TravelActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imageButton;
    private AlertDialog dialog;
    private int minPrice = 0, maxPrice = 9999;
    private TravelService travelService;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadTravels();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_travels);

        recyclerView = findViewById(R.id.listRecyclerView);
        imageButton = findViewById(R.id.imageButton);

        travelService = new TravelService(FirebaseFirestore.getInstance());

        imageButton.setOnClickListener(view -> showDialog());

        loadTravels();
    }

    public void showDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View filterView = getLayoutInflater().inflate(R.layout.popup, null);
        EditText minPriceFilter = filterView.findViewById(R.id.minPriceEditText);
        EditText maxPriceFilter = filterView.findViewById(R.id.maxPriceEditText);
        Button filterButton = filterView.findViewById(R.id.applyFilterButton);
        Button cancelButton = filterView.findViewById(R.id.cancelFilterButton);

        cancelButton.setOnClickListener(view -> dialog.cancel());

        filterButton.setOnClickListener(view -> {
            dialog.hide();
            minPrice = Integer.parseInt(String.valueOf(minPriceFilter.getText()));
            maxPrice = Integer.parseInt(String.valueOf(maxPriceFilter.getText()));
            loadTravels();
        });

        dialogBuilder.setView(filterView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public List<Travel> matchCriteria(List<Travel> resultTravel, List<Travel> travels){
        for(int i = 0; i < travels.size(); i++){
            Travel currentTravel = travels.get(i);
            System.out.println(minPrice);
            System.out.println(maxPrice);
            if(currentTravel.getPrice()>= minPrice && currentTravel.getPrice()<= maxPrice){
                resultTravel.add(currentTravel);
            }
        }

        return resultTravel;
    }

    private void loadTravels() {
        travelService.loadTravels(null).addOnSuccessListener(documentSnapshots -> {
            List<Travel> types = documentSnapshots.toObjects(Travel.class);
            changesRecycleView(new ArrayList<>(types));
//                setDismissibleRecycle();
            showMessage(types.size() + " trips loaded successfully");
        }).addOnFailureListener(e -> showMessage("Error loading trips. Please verify. " + e.getMessage()));
    }


    public void addTravel(View view) {
        Intent intent = new Intent(this, AddTravelActivity.class);
        activityResultLauncher.launch(intent);
    }

    public void changesRecycleView(ArrayList<Travel> travels){
        List<Travel> resultTravel =  new ArrayList<>();
        resultTravel = matchCriteria(resultTravel, travels);
        recyclerView = findViewById(R.id.listRecyclerView);
        if(resultTravel.size() == travels.size()){
            recyclerView.setAdapter(new TravelAdapter(resultTravel, this, true));
        }else{
            recyclerView.setAdapter(new TravelAdapter(resultTravel, this, false));
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
