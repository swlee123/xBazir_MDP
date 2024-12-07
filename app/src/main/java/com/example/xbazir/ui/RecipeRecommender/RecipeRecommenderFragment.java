package com.example.xbazir.ui.RecipeRecommender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.xbazir.R;
import com.example.xbazir.data.dao.FoodDao;
import com.example.xbazir.data.database.AppDatabase;
import com.example.xbazir.utils.RecipeApiUtil;
import com.google.android.material.chip.Chip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// done mvc
public class RecipeRecommenderFragment extends Fragment {

    private RecipeRecommenderModel model;
    private ProgressDialog loadingDialog;
    private FoodDao foodDao;

    private ConstraintLayout constraintLayout;
    private Flow flowIngredients, flowAllergens, flowFoodTypes;
    private List<Integer> ingredientChipIds = new ArrayList<>();
    private List<Integer> allergenChipIds = new ArrayList<>();
    private List<Integer> foodTypeChipIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_recommender_landing, container, false);

        // Initialize model
        model = new RecipeRecommenderModel();

        // Initialize database access
        AppDatabase db = AppDatabase.getInstance(requireContext());
        foodDao = db.foodDao();

        // Get references to flows and parent layout
        constraintLayout = rootView.findViewById(R.id.constraint_layout);
        flowIngredients = rootView.findViewById(R.id.flow_ingredients);
        flowAllergens = rootView.findViewById(R.id.flow_allergens);
        flowFoodTypes = rootView.findViewById(R.id.flow_food_types);

        // Set up UI interactions
        setupUI(rootView);

        return rootView;
    }

    private void setupUI(View rootView) {
        rootView.findViewById(R.id.button_add_ingredients).setOnClickListener(v -> showIngredientDialog());
        rootView.findViewById(R.id.button_add_allergen).setOnClickListener(v -> showAllergenDialog());
        rootView.findViewById(R.id.button_add_food_type).setOnClickListener(v -> showFoodTypeDialog());
        rootView.findViewById(R.id.button_generate_recipe).setOnClickListener(v -> generateRecipe());
    }


    private void showIngredientDialog() {
        new Thread(() -> {
            List<String> ingredients = foodDao.getFoodWithRemainingQuantity(); // Fetch ingredient names from database

            requireActivity().runOnUiThread(() -> {
                if (ingredients.isEmpty()) {
                    Toast.makeText(requireContext(), "No available ingredients", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select Ingredient");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, ingredients);

                builder.setAdapter(adapter, (dialog, which) -> {
                    String selectedIngredient = ingredients.get(which);
                    model.addIngredient(selectedIngredient);
                    addChipToFlow(selectedIngredient, flowIngredients, model.getAddedIngredients());
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }).start();
    }



    private void showFoodTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Food Type");

        // Create an EditText for user input
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set dialog buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String foodType = input.getText().toString().trim();
            if (!foodType.isEmpty()) {
                model.addFoodType(foodType); // Add to model
                addChipToFlow(foodType, flowFoodTypes, model.getSelectedFoodTypes()); // Add chip to flow
            } else {
                Toast.makeText(requireContext(), "Food Type cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.show();
    }


    private void showAllergenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Allergen");

        // Create an EditText for user input
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set dialog buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String allergen = input.getText().toString().trim();
            if (!allergen.isEmpty()) {
                model.addAllergen(allergen); // Add to model
                addChipToFlow(allergen, flowAllergens, model.getAddedAllergens()); // Add chip to flow
            } else {
                Toast.makeText(requireContext(), "Allergen cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.show();
    }


    private void addChipToFlow(String text, Flow flow, List<String> list) {

        // Create a new Chip
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setChipBackgroundColorResource(R.color.purple_200);
        chip.setTextColor(getResources().getColor(R.color.black));
        chip.setId(View.generateViewId()); // Ensure unique ID
        chip.setTag(text); // Set a tag for later identification

        // Set layout parameters for the Chip
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        chip.setLayoutParams(layoutParams);

        // Handle chip close action
        chip.setOnCloseIconClickListener(v -> {
            constraintLayout.removeView(chip); // Remove chip from layout
            list.remove(text); // Remove from the local list
            if (list == model.getAddedIngredients()) {
                model.removeIngredient(text); // Remove from ingredients in model
            } else if (list == model.getAddedAllergens()) {
                model.removeAllergen(text); // Remove from allergens in model
            } else if (list == model.getSelectedFoodTypes()) {
                model.removeFoodType(text); // Remove from food types in model
            }
            updateFlowReferences(flow, list); // Update the flow references
        });

        // Add the chip to the parent layout
        constraintLayout.addView(chip);
        list.add(text); // Add text to the list
        updateFlowReferences(flow, list); // Update the flow references
    }

    private void updateFlowReferences(Flow flow, List<String> list) {
        List<Integer> chipIds = new ArrayList<>();

        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (child instanceof Chip && list.contains(child.getTag())) {
                chipIds.add(child.getId());
            }
        }

        int[] ids = new int[chipIds.size()];
        for (int i = 0; i < chipIds.size(); i++) {
            ids[i] = chipIds.get(i);
        }

        flow.setReferencedIds(ids); // Update the Flow with the new chip IDs
    }


    private void generateRecipe() {
        if (model.getAddedIngredients().isEmpty()) {
            Toast.makeText(requireContext(), "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingDialog = ProgressDialog.show(requireContext(), "Generating Recipe", "Please wait...");

        String baseUrl = "https://95rimgz8qb.execute-api.ap-southeast-2.amazonaws.com/queryRecipeFromGPT";
        String ingredientsParam = String.join(",", model.getAddedIngredients());
        String allergensParam = String.join(",", model.getAddedAllergens());
        String foodTypeParam = model.getSelectedFoodTypes().isEmpty() ? "Any" : model.getSelectedFoodTypes().get(0);

        String apiUrl = baseUrl + "?ingredients=" + ingredientsParam
                + "&allergens=" + allergensParam
                + "&food_type=" + foodTypeParam;

        RecipeApiUtil.fetchRecipeFromApi(apiUrl, new RecipeApiUtil.RecipeApiCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("cuisineName", result.getString("Cuisine"));
                    bundle.putString("description", result.getString("Description"));
                    bundle.putString("steps", result.getString("Steps"));
                    bundle.putString("cookingTime", result.getString("Cooking_Time"));
                    bundle.putString("ingredients", result.getJSONArray("Ingredients").toString());
                    bundle.putString("missingIngredients", result.getJSONArray("Key_Ingredients_Lacking").toString());

                    Navigation.findNavController(requireView()).navigate(R.id.nav_recipe_result, bundle);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                Toast.makeText(requireContext(), "Error fetching recipe: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
