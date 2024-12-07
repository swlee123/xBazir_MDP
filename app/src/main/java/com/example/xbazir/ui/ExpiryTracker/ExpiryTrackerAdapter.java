package com.example.xbazir.ui.ExpiryTracker;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xbazir.R;
import com.example.xbazir.data.entities.Food;

import java.util.List;
public class ExpiryTrackerAdapter extends RecyclerView.Adapter<ExpiryTrackerAdapter.FoodViewHolder> {

    private final List<Food> foodList;
    private final Context context;
    private final OnFoodActionListener foodActionListener;

    public ExpiryTrackerAdapter(List<Food> foodList, Context context, OnFoodActionListener foodActionListener) {
        this.foodList = foodList;
        this.context = context;
        this.foodActionListener = foodActionListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_expiry, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        int remainingQuantity = food.food_quantity - food.used - food.spoiled;
        int remainingDays = foodActionListener.calculateRemainingDays(food.expiry_date);

        holder.foodName.setText(food.food_name);
        holder.foodQuantity.setText("Remaining: " + remainingQuantity + "/" + food.food_quantity);
        holder.boughtDate.setText("Bought on: " + food.bought_date);
        holder.remainingDays.setText("Days to expiry: " + remainingDays);

        if (remainingDays <= 3) {
            holder.remainingDays.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.remainingDays.setTextColor(context.getResources().getColor(android.R.color.black));
        }

        holder.spoiledQuantity.setText("Spoiled: " + food.spoiled);

        // Handle "Set Used" button click
        holder.usedButton.setOnClickListener(v -> openInputDialog("Set Used Quantity", remainingQuantity, usedQuantity -> {
            if (usedQuantity > 0 && usedQuantity <= remainingQuantity) {
                foodActionListener.onUsedQuantitySet(food.food_id, usedQuantity);
            } else {
                Toast.makeText(context, "Invalid quantity entered!", Toast.LENGTH_SHORT).show();
            }
        }));

        // Handle "Set Spoiled" button click
        holder.spoiledButton.setOnClickListener(v -> openInputDialog("Set Spoiled Quantity", remainingQuantity, spoiledQuantity -> {
            if (spoiledQuantity > 0 && spoiledQuantity <= remainingQuantity) {
                foodActionListener.onSpoiledQuantitySet(food.food_id, spoiledQuantity);
            } else {
                Toast.makeText(context, "Invalid quantity entered!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void openInputDialog(String title, int maxQuantity, OnQuantitySetListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Set", (dialog, which) -> {
            try {
                int quantity = Integer.parseInt(input.getText().toString());
                if (quantity > 0 && quantity <= maxQuantity) {
                    listener.onQuantitySet(quantity);
                } else {
                    Toast.makeText(context, "Enter a valid quantity (1-" + maxQuantity + ").", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid input.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void updateData(List<Food> newFoodList) {
        foodList.clear();
        foodList.addAll(newFoodList);
        notifyDataSetChanged();
    }

    public interface OnFoodActionListener {
        void onUsedQuantitySet(int foodId, int usedQuantity);
        void onSpoiledQuantitySet(int foodId, int spoiledQuantity);
        int calculateRemainingDays(String expiryDate);
    }

    interface OnQuantitySetListener {
        void onQuantitySet(int quantity);
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodQuantity, boughtDate, remainingDays, spoiledQuantity;
        Button usedButton, spoiledButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.text_food_name);
            foodQuantity = itemView.findViewById(R.id.text_food_quantity);
            spoiledQuantity = itemView.findViewById(R.id.text_spoiled_quantity);
            boughtDate = itemView.findViewById(R.id.text_bought_date);
            remainingDays = itemView.findViewById(R.id.text_remaining_days);
            usedButton = itemView.findViewById(R.id.button_set_used);
            spoiledButton = itemView.findViewById(R.id.button_set_spoiled);
        }
    }
}
