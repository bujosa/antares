package com.bujosa.antares;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.bujosa.antares.utils.MenuAdapter;
import com.bujosa.antares.utils.Menu;

public class MainActivity extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Antares);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new MenuAdapter(Menu.generateMenu(), this));
    }
}

