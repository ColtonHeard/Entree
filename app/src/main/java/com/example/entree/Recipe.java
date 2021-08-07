package com.example.entree;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;

/**
 * Represents a single food recipe. Contains information about the recipe's name, image, website link, description, ingredients, source, and calories.
 */
public class Recipe
{
    /** Static reference to the application's MainActivity. Set by the RecipeView class during it's initialization. */
    private static MainActivity applicationActivity;

    private String recipeTitle;
    private Drawable recipeImage;
    private Uri websiteLink;
    private String recipeDescription;
    private HashMap<String, Integer> recipeIngredients;
    private HashMap<String, String> ingredientUnits;
    private String recipeSource;
    private int recipeCalories;

    /**
     * Empty constructor that initializes the recipeIngredients and ingredientUnits maps.
     * All other class variables remain null.
     */
    public Recipe()
    {
        recipeTitle = null;
        recipeImage = null;
        websiteLink = null;
        recipeDescription = null;
        recipeIngredients = new HashMap<>();
        ingredientUnits = new HashMap<>();
        recipeSource = null;
        recipeCalories = 0;
    }

    /**
     * More detailed constructor that initializes the Recipe’s title, image, and websiteLink with the passed content.
     * All other class variables besides the recipeIngredients and ingredientUnits maps remain null.
     * All parameters must not be null.
     * Leaves the recipeImage untouched if Uri is invalid.
     *
     * @param title The title of this Recipe.
     * @param imageUri The Uri to the image of this Recipe.
     * @param websiteUri The Uri to the origin website of this Recipe.
     */
    public Recipe(@NonNull String title, @NonNull Uri imageUri, @NonNull Uri websiteUri)
    {
        recipeTitle = title;
        recipeImage = null;
        loadImageFromUri(imageUri);
        websiteLink = websiteUri;
        recipeDescription = null;
        recipeIngredients = new HashMap<>();
        ingredientUnits = new HashMap<>();
        recipeSource = null;
        recipeCalories = 0;
    }

    /**
     * Sets the title of this recipe to the passed String.
     *
     * @param title The name/title of this Recipe.
     */
    public void setTitle(String title)
    {
        recipeTitle = title;
    }

    /**
     * Takes the input uri and loads the associated image from the link.
     * Image is loaded as a BitmapDrawable object and then stored in the recipeImage class variable.
     * Leaves the recipeImage untouched if Uri is invalid.
     *
     * @param imageUri The Uri to the Recipe's image.
     */
    public void setImageUri(Uri imageUri)
    {
        loadImageFromUri(imageUri);
    }

    /**
     * Loads the image associated with the given Uri and stores it in recipeImage as a BitmapDrawable.
     * Leaves the recipeImage untouched if Uri is invalid.
     *
     * @param imageUri The Uri to the image for this Recipe.
     */
    private void loadImageFromUri(Uri imageUri)
    {
        try
        {
            Bitmap imageBitmap = null;
            imageBitmap = MediaStore.Images.Media.getBitmap(applicationActivity.getContentResolver(), imageUri);
            recipeImage = new BitmapDrawable(applicationActivity.getResources(), imageBitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("RecipeError", "Unable to load Recipe image link from uri: " + imageUri.toString());
        }
    }


    /**
     * Sets the websiteUri of this Recipe to the passed Uri.
     *
     * @param websiteUri The Uri to this Recipe's source website.
     */
    public void setWebsiteUri(Uri websiteUri)
    {
        websiteLink = websiteUri;
    }

    /**
     * Sets the description of this Recipe to the passed String.
     *
     * @param description A short text description of this Recipe.
     */
    public void setDescription(String description)
    {
        recipeDescription = description;
    }

    /**
     * Adds the ingredient to this Recipe with the passed amount and measurement unit.
     *
     * @param ingredientName The name of the ingredient.
     * @param amount The numerical amount of the ingredient.
     * @param measurementUnit The unit of measurement used for this ingredient.
     */
    public void addIngredient(String ingredientName, int amount, String measurementUnit)
    {
        recipeIngredients.put(ingredientName, amount);
        ingredientUnits.put(ingredientName, measurementUnit);
    }

    /**
     * Sets the source of this Recipe to the passed String.
     *
     * @param source The author/organization that wrote the recipe, or otherwise the origin website name.
     */
    public void setSource(String source)
    {
        recipeSource = source;
    }

    /**
     * Sets the calories of this Recipe to the passed Integer.
     *
     * @param calories The number of calories in this Recipe.
     */
    public void setCalories(int calories)
    {
        recipeCalories = calories;
    }

    /**
     * Set's the static reference to the application's MainActivity.
     * Called specifically by RecipeView during it's initialization.
     *
     * @param main The reference to the application's MainActivity.
     */
    public static void setApplicationActivity(MainActivity main)
    {
        applicationActivity = main;
    }

    /*
    Getters for all class variables.
     */

    public String getRecipeTitle()
    {
        return recipeTitle;
    }

    public Drawable getRecipeImage()
    {
        return recipeImage;
    }

    public Uri getWebsiteLink()
    {
        return websiteLink;
    }

    public String getRecipeDescription()
    {
        return recipeDescription;
    }

    public HashMap<String, Integer> getRecipeIngredients()
    {
        return recipeIngredients;
    }

    public HashMap<String, String> getIngredientUnits()
    {
        return ingredientUnits;
    }

    public String getRecipeSource()
    {
        return recipeSource;
    }

    public int getRecipeCalories()
    {
        return recipeCalories;
    }


}
