package com.example.entree.recipe;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.net.URL;
import java.util.ArrayList;

class RecipeBuilder {
    private String title = null; //REQUIRED
    private Bitmap image = null; // REQUIRED
    private URL url = null; //REQUIRED
    private String description = null;
    private ArrayList<String> ingredients = null;

    public RecipeBuilder setTitle(String title){
        this.title = title;
        return this;
    }
    public RecipeBuilder setImage(Bitmap image) {
        this.image = image;
        return this;
    }
    public RecipeBuilder setUrl(URL url) {
        this.url = url;
        return this;
    }
    public RecipeBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    public RecipeBuilder setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Recipe buildRecipe() throws NullPointerException {
        if (title == null) {throw new NullPointerException("title is a required parameter");}
        if (image == null) {throw new NullPointerException("image is a required parameter");}
        if (url == null) {throw new NullPointerException("url is a required parameter");}

        return new Recipe(title, image, url, description, ingredients);
    }
}