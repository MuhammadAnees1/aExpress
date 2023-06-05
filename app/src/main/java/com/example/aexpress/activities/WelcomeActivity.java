package com.example.aexpress.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.aexpress.databinding.ActivityWelcomeBinding;


public class WelcomeActivity extends AppCompatActivity {
        ActivityWelcomeBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            if (isConnected()) {
                Toast.makeText(this, "internet connected", Toast.LENGTH_SHORT).show();
                // Proceed with the activity
            } else {
                // Show a toast message indicating no internet connection
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WelcomeActivity.this, NoInternetConnection.class);
                startActivity(intent);
                finish();
                // Optionally, you can finish the activity or take appropriate action
            }


            binding.welcomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        private boolean isConnected() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
            return false;
        }
    }
