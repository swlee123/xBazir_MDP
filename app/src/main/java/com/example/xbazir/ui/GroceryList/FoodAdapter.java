package com.example.xbazir.ui.GroceryList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xbazir.R;
import com.example.xbazir.data.entities.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final List<Food> foodList; // Adapter's internal list of food items


    public FoodAdapter(List<Food> initialFoodList) {
        this.foodList = new ArrayList<>(initialFoodList);
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        // Set food details
        holder.foodName.setText(food.food_name);
        holder.foodQuantity.setText("Quantity: " + food.food_quantity);
        holder.foodRemarks.setText("Remarks: " + (food.remarks.isEmpty() ? "None" : food.remarks));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    /**
     * Update the food list with new data and refresh the RecyclerView.
     *
     * @param newFoodList The updated list of food items.
     */
    public void updateData(List<Food> newFoodList) {
        foodList.clear();
        foodList.addAll(newFoodList);
        notifyDataSetChanged(); // Refresh the RecyclerView
    }


    /**
     * Remove a food item from the adapter and refresh.
     *  no time to implment yet
     * @param position The position of the item to remove.
     */
    public void removeFood(int position) {
        if (position >= 0 && position < foodList.size()) {
            foodList.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodQuantity, foodRemarks;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.text_food_name);
            foodQuantity = itemView.findViewById(R.id.text_food_quantity);
            foodRemarks = itemView.findViewById(R.id.text_food_remarks);
        }
    }
}
