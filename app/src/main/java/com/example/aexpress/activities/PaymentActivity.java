package com.example.aexpress.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aexpress.databinding.ActivityPaymentBinding;
import com.example.aexpress.utils.Constants;
public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String oderCode = getIntent().getStringExtra("oderCode");

        binding.webView.setMixedContentAllowed(true);

        binding.webView.loadUrl(Constants.PAYMENT_URL+oderCode);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }
}