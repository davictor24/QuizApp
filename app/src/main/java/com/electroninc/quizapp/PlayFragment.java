package com.electroninc.quizapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment {

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String json;
        try {
            InputStream is = getActivity().getAssets().open("categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            ArrayList<Category> categories = (ArrayList<Category>) extractCategories(json);

            RecyclerView recyclerView = view.findViewById(R.id.categories);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);
            recyclerView.setAdapter(categoryAdapter);

        } catch (IOException ex) {
            Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_LONG).show();
        }
    }

    private List<Category> extractCategories(String json) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding categories to
        List<Category> categories = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        try {

            // Create a JSONArray from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(json);

            // For each category in the categoryArray, create an {@link Category} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {
                // Get a single category at position i within the list of categories
                JSONObject currentCategory = baseJsonResponse.getJSONObject(i);

                // Extract the value for the key called "name"
                String categoryName = currentCategory.getString("name");

                // Extract the value for the key called "id"
                int categoryId = currentCategory.getInt("id");

                // Create a new {@link Category} object
                Category category = new Category(R.drawable.material_design, categoryName, categoryId);

                // Add the new {@link Category} to the list of categories.
                categories.add(category);
            }

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }

        // Sort categories alphabetically
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category lhs, Category rhs) {
                return lhs.getCategoryName().compareTo(rhs.getCategoryName());
            }
        });

        // Return the list of categories
        return categories;
    }

}
