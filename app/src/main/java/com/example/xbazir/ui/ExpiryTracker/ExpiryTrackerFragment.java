package com.example.xbazir.ui.ExpiryTracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xbazir.R;
import com.example.xbazir.data.entities.Food;

import java.util.List;

// done mvc
public class ExpiryTrackerFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpiryTrackerAdapter adapter;
    private ExpiryTrackerViewModel viewModel;
    private ImageView imageEmptyFridge;
    private TextView textEmptyFridge;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expiry_tracker, container, false);

        // Initialize RecyclerView and Empty State Views
        recyclerView = view.findViewById(R.id.recyclerViewExpiryTracker);
        imageEmptyFridge = view.findViewById(R.id.image_empty_fridge);
        textEmptyFridge = view.findViewById(R.id.text_empty_fridge);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ExpiryTrackerViewModel.class);

        // Observe LiveData from ViewModel
        viewModel.getFoodList().observe(getViewLifecycleOwner(), this::updateUI);

        // Load data
        viewModel.loadFoodData();

        return view;
    }

    private void updateUI(List<Food> foodList) {
        if (foodList != null && !foodList.isEmpty()) {
            Log.d("ExpiryTrackerFragment", "Food list is not empty, updating UI");
            // Show RecyclerView and hide empty state
            imageEmptyFridge.setVisibility(View.GONE);
            textEmptyFridge.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Set up RecyclerView adapter
            if (adapter == null) {
                adapter = new ExpiryTrackerAdapter(foodList, requireContext(), new ExpiryTrackerAdapter.OnFoodActionListener() {
                    @Override
                    public void onUsedQuantitySet(int foodId, int usedQuantity) {
                        viewModel.updateUsedQuantity(foodId,usedQuantity);
                    }

                    @Override
                    public void onSpoiledQuantitySet(int foodId, int spoiledQuantity) {
                        // Handle update logic
                        viewModel.updateSpoiledQuantity(foodId,spoiledQuantity);
                    }

                    @Override
                    public int calculateRemainingDays(String expiryDate) {
                        return viewModel.calculateRemainingDays(expiryDate);
                    }
                });
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(foodList); // Update existing adapter data
            }
        } else {
            Log.d("ExpiryTrackerFragment", "Food list is empty, showing empty state");
            // Show empty state and hide RecyclerView
            imageEmptyFridge.setVisibility(View.VISIBLE);
            textEmptyFridge.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
    }


}
