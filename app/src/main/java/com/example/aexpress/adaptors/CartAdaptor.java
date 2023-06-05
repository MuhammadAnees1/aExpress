package com.example.aexpress.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aexpress.R;
import com.example.aexpress.activities.CartActivity;
import com.example.aexpress.activities.CheckoutActivity;
import com.example.aexpress.databinding.ItemCartBinding;
import com.example.aexpress.databinding.QuantityDialogBinding;
import com.example.aexpress.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.CartViewHolder> {
    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;
    public interface CartListener {
        public void onQuantityChanged();
    }
    public CartAdaptor(Context context, ArrayList<Product> products,CartListener cartListener){
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
    Product product = products.get(position);
        Glide.with(context).load(product.getImage()).into(holder.binding.image);
    holder.binding.name.setText(product.getName());
    holder.binding.price.setText("PKR"+product.getPrice());
    holder.binding.quantity.setText(product.getQuantity()+"item(s)");

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(quantityDialogBinding.getRoot())
                    .create();

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));

            quantityDialogBinding.productName.setText(product.getName());
            quantityDialogBinding.productStock.setText("Stock:" +product.getStock());
            quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));

            int stock = product.getStock();

            quantityDialogBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int quantity = product.getQuantity();
                    quantity++;

                    if(quantity>product.getStock()) {
                        Toast.makeText(context, "Max stock available: "+ product.getStock(), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        product.setQuantity(quantity);
                        quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                    }

                        notifyDataSetChanged();
                    cart.updateItem(product, product.getQuantity());
                    cartListener.onQuantityChanged();
                }
            });
            quantityDialogBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = product.getQuantity();
                    if(quantity > 1)
                        quantity--;
                    product.setQuantity(quantity);
                    quantityDialogBinding.quantity.setText(String.valueOf(quantity));

                        notifyDataSetChanged();
                    cart.updateItem(product, product.getQuantity());
                    cartListener.onQuantityChanged();
                }
            });
            quantityDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
//                        notifyDataSetChanged();
//                        cart.updateItem(product, product.getQuantity());
//                        cartListener.onQuantityChanged();
                }
            });

            dialog.show();
        }
    });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Call a method to handle item deletion
                deleteItem(holder);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ItemCartBinding binding;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemCartBinding.bind(itemView);
        }
    }
    private void deleteItem(CartViewHolder holder) {
        int position = holder.getBindingAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            Product deletedProduct = products.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Item");
            builder.setMessage("Are you sure you want to delete this item?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Remove the item from the list
                    products.remove(position);
                    notifyDataSetChanged();

                    // Update your cart or perform any other necessary actions
                    cart.removeItem(deletedProduct);
                    cartListener.onQuantityChanged();

                    // Update the subtotal
                    BigDecimal totalPrice = cart.getTotalPrice();
                    if (context instanceof CartActivity) {
                        CartActivity cartActivity = (CartActivity) context;
                        cartActivity.binding.subtotal.setText(String.format("PKR %.2f", totalPrice));
                    }

                    if (context instanceof CheckoutActivity) {
                        CheckoutActivity checkoutActivity = (CheckoutActivity) context;
                        checkoutActivity.binding.total.setText("PKR " + totalPrice);
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }
    }
}