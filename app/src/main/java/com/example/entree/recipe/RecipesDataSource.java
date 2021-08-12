package com.example.entree.recipe;

import java.util.ArrayList;

public abstract class RecipesDataSource {
    enum Sources {
        ALL_RECIPES
    }

    /**
     * Listener interface for RecipeSearcher results.
     * Specifically implemented by RecipeView to receive search results.
     */
    public interface Listener {
        /**
         * Called by RecipeSearcher when it has a list of recipes to pass to it's listener.
         */
        void onRecipesReady(ArrayList<Recipe> recipes);
    }

    public static RecipesDataSource makeDataSource(Sources type, Listener listener) {
        switch (type) {
            case ALL_RECIPES:
                return new AllRecipesDataSource(listener);
            default:
                return null;
        }
    }

    /**
     * The attached RecipeSearcherListener in which to return search results to.
     */
    protected Listener listener;
    /**
     * The list of recipes resulting from a search.
     */
    protected ArrayList<Recipe> recipesCache = new ArrayList<>();
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
    public RecipesDataSource(Listener recipeListener) {
        listener = recipeListener;
    }
    public abstract void getRecipes();
    public abstract void setIngredients(ArrayList<String> ingredientsArray);
}
