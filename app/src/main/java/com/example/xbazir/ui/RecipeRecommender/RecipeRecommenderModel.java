package com.example.xbazir.ui.RecipeRecommender;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecommenderModel {

    private List<String> addedIngredients;
    private List<String> addedAllergens;
    private List<String> selectedFoodTypes;

    public RecipeRecommenderModel() {
        this.addedIngredients = new ArrayList<>();
        this.addedAllergens = new ArrayList<>();
        this.selectedFoodTypes = new ArrayList<>();
    }

    public List<String> getAddedIngredients() {
        return addedIngredients;
    }

    public void addIngredient(String ingredient) {
        if (!addedIngredients.contains(ingredient)) {
            addedIngredients.add(ingredient);
        }
    }

    public void removeIngredient(String ingredient) {
        addedIngredients.remove(ingredient);
    }

    public List<String> getAddedAllergens() {
        return addedAllergens;
    }

    public void addAllergen(String allergen) {
        if (!addedAllergens.contains(allergen)) {
            addedAllergens.add(allergen);
        }
    }

    public void removeAllergen(String allergen) {
        addedAllergens.remove(allergen);
    }

    public List<String> getSelectedFoodTypes() {
        return selectedFoodTypes;
    }

    public void addFoodType(String foodType) {
        if (!selectedFoodTypes.contains(foodType)) {
            selectedFoodTypes.add(foodType);
        }
    }

    public void removeFoodType(String foodType) {
        selectedFoodTypes.remove(foodType);
    }
}
