package com.example.xbazir.ui.RecipeRecommender;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xbazir.R;
import com.example.xbazir.data.models.RecipeDetailsModel;

import java.util.List;


// RecipeDetailsFragment : Controller
// RecipeDetailsModel : Model
// fragment_Recipe_details.xml : View

public class RecipeDetailsFragment extends Fragment {

    private TextView tvCuisineName, tvDescription, tvCookingTime;
    private LinearLayout layoutSteps, layoutIngredients, layoutMissingIngredients;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        // Initialize views
        tvCuisineName = rootView.findViewById(R.id.text_cuisine_name);
        tvDescription = rootView.findViewById(R.id.text_description);
        tvCookingTime = rootView.findViewById(R.id.text_cooking_time);
        layoutSteps = rootView.findViewById(R.id.layout_steps);
        layoutIngredients = rootView.findViewById(R.id.layout_ingredients);
        layoutMissingIngredients = rootView.findViewById(R.id.layout_missing_ingredients);

        // Load data from arguments
        if (getArguments() != null) {
            RecipeDetailsModel model = new RecipeDetailsModel(
                    getArguments().getString("cuisineName"),
                    getArguments().getString("description"),
                    getArguments().getString("cookingTime"),
                    getArguments().getString("steps"),
                    getArguments().getString("ingredients"),
                    getArguments().getString("missingIngredients")
            );
            updateUI(model);
        } else {
            Toast.makeText(requireContext(), "No recipe details available", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void updateUI(RecipeDetailsModel model) {
        tvCuisineName.setText(model.getCuisineName());
        tvDescription.setText(model.getDescription());
        tvCookingTime.setText(model.getCookingTime());

        populateList(layoutSteps, model.getSteps(), "Step ");
        populateList(layoutIngredients, model.getIngredients(), "- ");
        populateList(layoutMissingIngredients, model.getMissingIngredients(), "- ");
    }

    private void populateList(LinearLayout layout, List<String> items, String prefix) {
        layout.removeAllViews();
        for (String item : items) {
            TextView textView = new TextView(requireContext());
            textView.setText(prefix + item);
            textView.setTextColor(Color.parseColor("#03346E")); // Set desired color
            textView.setTextSize(14); // Set text size
            textView.setPadding(8, 8, 8, 8);

            // Set the font to match
            Typeface typeface = Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL);
            textView.setTypeface(typeface);
            layout.addView(textView);
        }
    }
}
