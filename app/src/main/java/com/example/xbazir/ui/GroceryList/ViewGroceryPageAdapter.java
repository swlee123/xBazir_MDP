package com.example.xbazir.ui.GroceryList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewGroceryPageAdapter extends FragmentStateAdapter {

    private final int groceryListId;

    public ViewGroceryPageAdapter(@NonNull Fragment fragment, int groceryListId) {
        super(fragment);
        this.groceryListId = groceryListId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("groceryListId", groceryListId);

        if (position == 0) {
            FoodNotBoughtFragment fragment = new FoodNotBoughtFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else {
            FoodBoughtFragment fragment = new FoodBoughtFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }
    @Override
    public int getItemCount() {
        return 2; // Two tabs
    }
}
