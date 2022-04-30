package com.bujosa.antares;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("mytravels", null);
        Type type = new TypeToken<ArrayList<Travel>>() {}.getType();
        List<Travel> travels = gson.fromJson(json, type);
        recyclerView.setAdapter(new TravelAdapter(travels, this, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }
}
