package com.example.xbazir.ui.GroceryList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xbazir.R;
import com.example.xbazir.data.entities.GroceryList;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

    private final List<GroceryList> groceryLists; // The list of GroceryList objects
    private final OnGroceryListClickListener clickListener; // Listener for item clicks
    private final OnDeleteListener deleteListener; // Listener for delete actions

    // Constructor
    public GroceryListAdapter(OnGroceryListClickListener clickListener, OnDeleteListener deleteListener) {
        this.groceryLists = new ArrayList<>();
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    // Interface for item clicks
    public interface OnGroceryListClickListener {
        void onItemClick(GroceryList groceryList);
    }

    // Interface for delete actions
    public interface OnDeleteListener {
        void onDelete(GroceryList groceryList);
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery_list, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        GroceryList groceryList = groceryLists.get(position);

        // Set data for each item
        holder.textName.setText(groceryList.list_name);
        holder.textItems.setText(groceryList.food_count + " items");

        // Assign random colors for the card background
        int[] cardColors = {
                holder.itemView.getContext().getResources().getColor(R.color.r1),
                holder.itemView.getContext().getResources().getColor(R.color.r2),
                holder.itemView.getContext().getResources().getColor(R.color.r3),
                holder.itemView.getContext().getResources().getColor(R.color.r4)
        };
        int randomColor = cardColors[new java.util.Random().nextInt(cardColors.length)];
        holder.cardView.setCardBackgroundColor(randomColor);

        // Set click listener for the card
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(groceryList));

        // Set click listener for the delete button
        holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(groceryList));
    }

    @Override
    public int getItemCount() {
        return groceryLists.size();
    }

    // Update the data in the adapter
    public void updateData(List<GroceryList> newGroceryLists) {
        groceryLists.clear();
        groceryLists.addAll(newGroceryLists);
        notifyDataSetChanged(); // Refresh the RecyclerView
    }

    // Remove a grocery list from the adapter
    public void removeGroceryList(GroceryList groceryList) {
        int position = groceryLists.indexOf(groceryList);
        if (position >= 0) {
            groceryLists.remove(position);
            notifyItemRemoved(position);
        }
    }

    public GroceryList getGroceryListAt(int position) {
        return groceryLists.get(position); // Get the grocery list at the specified position
    }

    // ViewHolder for each grocery list item
    static class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textItems;
        ImageButton deleteButton;
        MaterialCardView cardView;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_grocery_list);
            textName = itemView.findViewById(R.id.text_grocery_list_name);
            textItems = itemView.findViewById(R.id.text_grocery_list_items);
            deleteButton = itemView.findViewById(R.id.button_delete_grocery_list);
        }
    }
}
