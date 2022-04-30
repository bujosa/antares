package com.bujosa.antares;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;

import com.bujosa.antares.adapter.MenuAdapter;
import com.bujosa.antares.entity.Menu;
import com.bujosa.antares.entity.Travel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Antares);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String getTravelsJson = sharedPreferences.getString("travels", null);
        Type type = new TypeToken<ArrayList<Travel>>() {}.getType();
        List<Travel> travels = gson.fromJson(getTravelsJson, type);
        if(travels == null){
            String json = gson.toJson(Travel.generateTravels());
            editor.putString("travels",json);
            editor.apply();
        }
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new MenuAdapter(Menu.generateMenu(), this));
    }
}

