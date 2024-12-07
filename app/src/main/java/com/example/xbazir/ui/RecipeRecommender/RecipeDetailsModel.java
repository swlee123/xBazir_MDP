package com.example.xbazir.data.models;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsModel {

    private final String cuisineName;
    private final String description;
    private final String cookingTime;
    private final List<String> steps;
    private final List<String> ingredients;
    private final List<String> missingIngredients;

    public RecipeDetailsModel(String cuisineName, String description, String cookingTime,
                              String stepsJson, String ingredientsJson, String missingIngredientsJson) {
        this.cuisineName = cuisineName;
        this.description = description;
        this.cookingTime = cookingTime;
        this.steps = parseJsonArrayOrString(stepsJson);
        this.ingredients = parseJsonArray(ingredientsJson);
        this.missingIngredients = parseJsonArray(missingIngredientsJson);
    }

    public String getCuisineName() {
        return cuisineName;
    }

    public String getDescription() {
        return description;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public List<String> getSteps() {
        return steps;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    private List<String> parseJsonArray(String jsonArrayString) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<String> parseJsonArrayOrString(String steps) {
        List<String> stepsList = new ArrayList<>();
        try {
            if (steps.trim().startsWith("[") && steps.trim().endsWith("]")) {
                JSONArray jsonArray = new JSONArray(steps);
                for (int i = 0; i < jsonArray.length(); i++) {
                    stepsList.add(jsonArray.getString(i).trim());
                }
            } else {
                String[] splitSteps = steps.split("\n");
                for (String step : splitSteps) {
                    stepsList.add(step.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stepsList;
    }
}
