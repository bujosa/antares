package com.bujosa.antares;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;



@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashScreenTheme);

        new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(new Intent(SplashScreenActivity.this,
                LoginActivity.class)),2000);

    }
}