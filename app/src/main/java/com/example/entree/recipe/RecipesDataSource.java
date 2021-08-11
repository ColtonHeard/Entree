package com.example.entree.recipe;

import java.util.ArrayList;
import java.util.Enumeration;

public abstract class RecipesDataSource implements Enumeration<Recipe> {
    /**
     * Listener interface for RecipeSearcher results.
     * Specifically implemented by RecipeView to receive search results.
     */
    public interface RecipeDataSourceListener {
        /**
         * Called by RecipeSearcher when it has a list of recipes to pass to it's listener.
         */
        public void onRecipesReady();
    }

    /**
     * The attached RecipeSearcherListener in which to return search results to.
     */
    protected RecipeDataSourceListener listener;
    /**
     * The list of recipes resulting from a search.
     */
    protected ArrayList<Recipe> recipes = new ArrayList<>();
    protected ArrayList<String> ingredientsArray;
    /**
     * The active thread used for the current search.
     */
    protected Thread searchThread;

    /**
     * Initializes this RecipeDataSource with the given RecipeView as a listener.
     *
     * @param recipeListener The RecipeView to return search results to.
     */
    public RecipesDataSource(RecipesDataSource.RecipeDataSourceListener recipeListener) {
        listener = recipeListener;
    }
    protected abstract void getRecipes();
    public abstract void setIngredientsArray(ArrayList<String> ingredientsArray);
}
