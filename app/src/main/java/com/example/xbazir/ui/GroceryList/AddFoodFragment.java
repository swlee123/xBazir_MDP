package com.example.xbazir.ui.GroceryList;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.xbazir.R;
import com.example.xbazir.data.entities.Food;
import com.example.xbazir.databinding.FragmentAddFoodBinding;

import java.util.ArrayList;

// done mvc
public class AddFoodFragment extends Fragment {

    private FragmentAddFoodBinding binding;
    private FoodAdapter adapter;
    private AddFoodViewModel viewModel;

    private String listName;
    private String dueDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddFoodBinding.inflate(inflater, container, false);

        // Retrieve listName and dueDate from arguments
        if (getArguments() != null) {
            listName = getArguments().getString("listName", "Unnamed List");
            dueDate = getArguments().getString("dueDate", "");
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AddFoodViewModel.class);

        // Set up RecyclerView
        adapter = new FoodAdapter(new ArrayList<>());
        binding.recyclerViewFoodList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewFoodList.setAdapter(adapter);

        // Observe LiveData from ViewModel
        viewModel.getFoodList().observe(getViewLifecycleOwner(), foodList -> {
            adapter.updateData(foodList);
            updateFoodListUI(foodList.isEmpty());
        });

        // Button to create the grocery list
        binding.buttonCreateGroceryList.setOnClickListener(v -> {
            if (adapter.getItemCount() == 0) {
                Toast.makeText(getContext(), "Add at least one food item", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.createGroceryList(listName, dueDate);
                Toast.makeText(getContext(), "Grocery list created!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.nav_gallery);
            }
        });

        // Floating Action Button for adding food
        binding.buttonAddFood.setOnClickListener(v -> showAddFoodDialog());

        return binding.getRoot();
    }

    private void updateFoodListUI(boolean isEmpty) {
        if (isEmpty) {
            binding.recyclerViewFoodList.setVisibility(View.GONE);
            binding.imageEmptyFood.setVisibility(View.VISIBLE);
            binding.textNoFood.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerViewFoodList.setVisibility(View.VISIBLE);
            binding.imageEmptyFood.setVisibility(View.GONE);
            binding.textNoFood.setVisibility(View.GONE);
        }
    }

    private void showAddFoodDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_food, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.button_dialog_add_food).setOnClickListener(v -> {
            String name = ((android.widget.EditText) dialogView.findViewById(R.id.edit_dialog_food_name)).getText().toString();
            String quantityStr = ((android.widget.EditText) dialogView.findViewById(R.id.edit_dialog_food_quantity)).getText().toString();
            String remarks = ((android.widget.EditText) dialogView.findViewById(R.id.edit_dialog_food_remarks)).getText().toString();

            if (name.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Food food = new Food();
            food.food_name = name;
            food.food_quantity = Integer.parseInt(quantityStr);
            food.remarks = remarks;

            viewModel.addFood(food);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
