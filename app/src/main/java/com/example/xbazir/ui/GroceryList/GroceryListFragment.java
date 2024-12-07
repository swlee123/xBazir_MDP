package com.example.xbazir.ui.GroceryList;

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
import com.example.xbazir.data.entities.GroceryList;
import com.example.xbazir.databinding.FragmentGroceryListBinding;

// Done Converting to MVC
public class GroceryListFragment extends Fragment {

    private FragmentGroceryListBinding binding;
    private GroceryListAdapter adapter;
    private GroceryListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroceryListBinding.inflate(inflater, container, false);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(GroceryListViewModel.class);

        // Set up RecyclerView
        adapter = new GroceryListAdapter(this::navigateToViewGrocery, this::deleteGroceryList);
        binding.recyclerViewGroceryLists.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewGroceryLists.setAdapter(adapter);

        // Observe LiveData from ViewModel
        viewModel.getGroceryLists().observe(getViewLifecycleOwner(), groceryLists -> {
            if (groceryLists != null && !groceryLists.isEmpty()) {
                binding.textEmptyState.setVisibility(View.GONE);
                binding.recyclerViewGroceryLists.setVisibility(View.VISIBLE);
                adapter.updateData(groceryLists);
            } else {
                binding.textEmptyState.setVisibility(View.VISIBLE);
                binding.recyclerViewGroceryLists.setVisibility(View.GONE);
            }
        });

        // Load grocery lists
        viewModel.loadGroceryLists();

        // Add new grocery list button
        binding.cardViewButtonTwo.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.nav_create_grocery)
        );

        return binding.getRoot();
    }

    private void deleteGroceryList(GroceryList groceryList) {
        viewModel.deleteGroceryList(groceryList);
        Toast.makeText(getContext(), "Deleted Grocery List", Toast.LENGTH_SHORT).show();
    }

    private void navigateToViewGrocery(GroceryList groceryList) {
        Bundle bundle = new Bundle();
        bundle.putInt("groceryListId", groceryList.list_id);
        Navigation.findNavController(requireView()).navigate(R.id.action_groceryList_to_viewGrocery, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
