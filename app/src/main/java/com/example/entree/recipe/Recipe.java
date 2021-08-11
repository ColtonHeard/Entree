package com.example.entree.recipe;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.ArrayList;

public class Recipe
{
    final String title;
    final Bitmap image;
    final URL url;
    final String description;
    final ArrayList<String> ingredients;

    public Recipe(String title,
                  Bitmap image,
                  URL url,
                  String description,
                  ArrayList<String> ingredients) {

        this.title = title;
        this.image = image;
        this.url = url;
        this.description = description;
        this.ingredients = ingredients;
    }
}

