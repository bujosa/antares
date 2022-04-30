package com.bujosa.antares;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bujosa.antares.adapter.TravelAdapter;
import com.bujosa.antares.entity.Travel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TravelActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imageButton;
    private List<Travel> travels;
    private AlertDialog dialog;
    private int minPrice = 0, maxPrice = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_travels);

        recyclerView = findViewById(R.id.listRecyclerView);
        imageButton = findViewById(R.id.imageButton);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("travels", null);

        Type type = new TypeToken<ArrayList<Travel>>() {}.getType();
        travels = gson.fromJson(json, type);

        imageButton.setOnClickListener(view -> showDialog());
        changesRecycleView();
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
            changesRecycleView();
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

    public void changesRecycleView(){
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
}
