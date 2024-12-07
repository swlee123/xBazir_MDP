package com.example.xbazir.ui.GroceryList;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.xbazir.R;
import com.example.xbazir.databinding.FragmentCreateGroceryListBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// done mvc
public class CreateGroceryListFragment extends Fragment {

    private FragmentCreateGroceryListBinding binding;
    private CreateGroceryListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateGroceryListBinding.inflate(inflater, container, false);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CreateGroceryListViewModel.class);

        // Back button listener
        binding.buttonUndoCreateList.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        // Set up Date Picker for the due date
        binding.editDueDate.setOnClickListener(v -> showDatePicker());

        // Handle "Create List" button click
        binding.buttonCreateList.setOnClickListener(v -> {
            viewModel.setListName(binding.editListName.getText().toString());
            viewModel.setDueDate(binding.editDueDate.getText().toString());

            if (!viewModel.isInputValid()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass data to AddFoodFragment
            Bundle bundle = new Bundle();
            bundle.putString("listName", viewModel.getListName());
            bundle.putString("dueDate", viewModel.getDueDate());

            Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_add_food_to_list, bundle);
        });

        return binding.getRoot();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.getTime());
                    binding.editDueDate.setText(formattedDate);
                    viewModel.setDueDate(formattedDate); // Update ViewModel
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
