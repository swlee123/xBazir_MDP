package com.example.xbazir.utils;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeApiUtil {

    public interface RecipeApiCallback {
        void onSuccess(JSONObject result);
        void onError(String error);
    }
    public static void fetchRecipeFromApi(String apiUrl, RecipeApiCallback callback) {
        new AsyncTask<Void, Void, JSONObject>() {
            private String errorMessage = null;

            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder responseBuilder = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            responseBuilder.append(inputLine);
                        }
                        in.close();

                        return new JSONObject(responseBuilder.toString());
                    } else {
                        errorMessage = "Error: HTTP " + responseCode;
                    }
                } catch (Exception e) {
                    errorMessage = "Exception: " + e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    callback.onSuccess(result);
                } else {
                    callback.onError(errorMessage);
                }
            }
        }.execute();


    }
}
