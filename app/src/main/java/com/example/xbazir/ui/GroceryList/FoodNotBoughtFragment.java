package com.example.xbazir.ui.GroceryList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xbazir.R;

// done mvc
public class FoodNotBoughtFragment extends Fragment {

    private RecyclerView recyclerView;
    private FoodinViewGroceryAdapter adapter;
    private FoodNotBoughtViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_bought, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFoodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(FoodNotBoughtViewModel.class);

        // Observe data from ViewModel
        viewModel.getFoodList().observe(getViewLifecycleOwner(), foods -> {
            adapter = new FoodinViewGroceryAdapter(foods, false);
            recyclerView.setAdapter(adapter);
        });

        // Load data
        int groceryListId = getArguments() != null ? getArguments().getInt("groceryListId") : -1;
        viewModel.loadFood(groceryListId);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int groceryListId = getArguments() != null ? getArguments().getInt("groceryListId") : -1;
        viewModel.loadFood(groceryListId); // Refresh the food list when the fragment is resumed
    }
}
