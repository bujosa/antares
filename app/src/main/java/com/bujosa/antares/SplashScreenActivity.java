package com.bujosa.antares;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bujosa.antares.authentication.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashScreenTheme);

        FirebaseApp.initializeApp(SplashScreenActivity.this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(new Intent(SplashScreenActivity.this,
                    LoginActivity.class)),2000);
        }else{
            Toast.makeText(this, "Bienvenido de nuevo " + user.getEmail(), Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this,
                        MainActivity.class));
                finish();
            },2000);
        }
    }
}