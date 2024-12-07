package com.example.xbazir.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.xbazir.R;
import com.example.xbazir.databinding.FragmentHomeBinding;
import com.google.android.material.card.MaterialCardView;

// done mvc
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize buttons
        MaterialCardView groceryListCard = rootView.findViewById(R.id.card_view_button_one);
        MaterialCardView expiryTrackerCard = rootView.findViewById(R.id.card_view_button_two);
        MaterialCardView recipeRecommenderCard = rootView.findViewById(R.id.card_view_button_three);


        // Set up navigation using NavController
        groceryListCard.setOnClickListener(v -> {
            Log.d("CurrentActivityDebug", "Current Activity: " + requireActivity().getClass().getSimpleName());

            NavController navController = Navigation.findNavController(requireView());


            navController.navigate(R.id.nav_gallery);
        });

        expiryTrackerCard.setOnClickListener(v -> {
            Log.d("CurrentActivityDebug", "Current Activity: " + requireActivity().getClass().getSimpleName());

            NavController navController = Navigation.findNavController(requireView());

            navController.navigate(R.id.nav_slideshow);
        });

        recipeRecommenderCard.setOnClickListener(v -> {
            Log.d("CurrentActivityDebug", "Current Activity: " + requireActivity().getClass().getSimpleName());

            NavController navController = Navigation.findNavController(requireView());

            navController.navigate(R.id.nav_recipe_recommender);
        });

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
