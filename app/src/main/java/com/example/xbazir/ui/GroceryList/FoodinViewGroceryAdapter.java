package com.example.xbazir.ui.GroceryList;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xbazir.R;
import com.example.xbazir.data.database.AppDatabase;
import com.example.xbazir.data.entities.Food;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// done mvc
public class FoodinViewGroceryAdapter extends RecyclerView.Adapter<FoodinViewGroceryAdapter.FoodViewHolder> {

    private final List<Food> foodList;
    private final boolean isBoughtTab;

    public FoodinViewGroceryAdapter(List<Food> foodList, boolean isBoughtTab) {
        this.foodList = foodList;
        this.isBoughtTab = isBoughtTab; // True for "Bought" tab, false for "Not Bought" tab
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_grocery, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.foodName.setText(food.food_name);
        holder.foodQuantity.setText("Quantity: " + food.food_quantity);
        holder.foodRemarks.setText("Remarks: " + food.remarks);

        // Configure for "Not Bought" or "Bought" tab
        if (isBoughtTab) {
            // For "Bought" Tab
            holder.broughtCheckbox.setVisibility(View.GONE);
            holder.expiryDateText.setVisibility(View.VISIBLE);
            holder.expiryDateText.setText("Expiry Date: " + (food.expiry_date != null ? food.expiry_date : "Not Set"));
            holder.submitExpiryButton.setVisibility(View.GONE);
        } else {
            // For "Not Bought" Tab
            holder.broughtCheckbox.setVisibility(View.VISIBLE);
            holder.broughtCheckbox.setChecked(food.is_bought);
            holder.expiryDateText.setVisibility(food.is_bought ? View.VISIBLE : View.GONE);
            holder.submitExpiryButton.setVisibility(food.is_bought ? View.VISIBLE : View.GONE);

            // Handle checkbox logic
            holder.broughtCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    holder.expiryDateText.setVisibility(View.VISIBLE);
                    holder.submitExpiryButton.setVisibility(View.VISIBLE);
                    holder.expiryDateText.setOnClickListener(v -> showDatePickerDialog(holder.itemView.getContext(), food, holder.expiryDateText));
                } else {
                    holder.expiryDateText.setVisibility(View.GONE);
                    holder.submitExpiryButton.setVisibility(View.GONE);
                }
            });

            // Handle submit expiry date button
            holder.submitExpiryButton.setOnClickListener(v -> {
                if (food.expiry_date != null && !food.expiry_date.isEmpty()) { // Validate expiry_date
                    food.is_bought = true;

                    // Set the bought date to the current date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    food.bought_date = dateFormat.format(new Date());

                    updateFoodInDatabase(holder.itemView.getContext(), food); // Persist changes to the database
                    foodList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(holder.itemView.getContext(), "Food marked as bought", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(holder.itemView.getContext(), "Please select an expiry date first", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    private void showDatePickerDialog(Context context, Food food, TextView expiryDateText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
                    food.expiry_date = formattedDate;
                    expiryDateText.setText("Expiry Date: " + formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateFoodInDatabase(Context context, Food food) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.foodDao().updateFoodExpiryDateAndStatus(food.food_id, food.expiry_date, food.is_bought, food.bought_date);
        }).start();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodQuantity, foodRemarks, expiryDateText;
        CheckBox broughtCheckbox;
        Button submitExpiryButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.text_food_name);
            foodQuantity = itemView.findViewById(R.id.text_food_quantity);
            foodRemarks = itemView.findViewById(R.id.text_food_remarks);
            broughtCheckbox = itemView.findViewById(R.id.checkbox_brought);
            expiryDateText = itemView.findViewById(R.id.text_expiry_date);
            submitExpiryButton = itemView.findViewById(R.id.button_submit_expiry);
        }
    }
}
