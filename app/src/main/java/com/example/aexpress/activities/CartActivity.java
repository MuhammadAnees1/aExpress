package com.example.aexpress.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aexpress.adaptors.CartAdaptor;
import com.example.aexpress.databinding.ActivityCartBinding;
import com.example.aexpress.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdaptor adaptor;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        products = new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();

        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

            adaptor = new CartAdaptor(this, products, new CartAdaptor.CartListener() {
                @Override
                public void onQuantityChanged() {
                    binding.subtotal.setText(String.format("PKR %.2f",cart.getTotalPrice()));
                }
            });


            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
            binding.cartList.setLayoutManager(layoutManager);
            binding.cartList.addItemDecoration(itemDecoration);
            binding.cartList.setAdapter(adaptor);
            binding.subtotal.setText(String.format("PKR %.2f",cart.getTotalPrice()));


            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CartActivity.this,CheckoutActivity.class));
                }
            });

            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}