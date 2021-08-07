package com.example.entree;

import java.util.ArrayList;

/**
 * Recipe scraper class that makes use of Google's search API and Recipe Schema to find relevant recipes from a list of ingredients.
 */
public class RecipeSearcher
{
    /** The attached RecipeSearcherListener in which to return search results to. */
    private RecipeSearcherListener listener;

    /** The list of recipes resulting from a search. */
    private ArrayList<Recipe> recipes;

    /** The active thread used for the current search. */
    private Thread searchThread;

    /**
     * Listener interface for RecipeSearcher results.
     * Specifically implemented by RecipeView to receive search results.
     */
    public interface RecipeSearcherListener
    {
        /**
         * Called by RecipeSearcher when it has a list of recipes to pass to it's listener.
         *
         * @param recipes The list of found recipes.
         */
        public void onRecipesReady(ArrayList<Recipe> recipes);
    }

    /**
     * Initializes this RecipeSearcher with the given RecipeView as a listener.
     *
     * @param recipeListener The RecipeView to return search results to.
     */
    public RecipeSearcher(RecipeView recipeListener)
    {
        listener = recipeListener;
        recipes = new ArrayList<>();

        // TODO Any additional setup
    }

    /**
     * Called by the RecipeView when a new Recipe list is needed.
     * Calls beginSearchingForRecipes() with a resultOffset of 0.
     * @param ingredients The list of ingredients to search for relevant recipes for.
     */
    public void searchForRecipes(ArrayList<String> ingredients)
    {
        beginSearchingForRecipes(ingredients, 0);
    }

    /**
     * Called by the RecipeView for additional Recipe objects.
     *
     * @param ingredients The list of ingredients to search for relevant recipes for.
     * @param resultOffset Specifies the number of results to offset the returned results by.
     */
    public void searchForAdditionalRecipes(ArrayList<String> ingredients, int resultOffset)
    {
        beginSearchingForRecipes(ingredients, resultOffset);
    }

    /**
     * Begins the search process for a list of recipes.
     * The results of the search should be limited to 20 recipes maximum and stored in recipes before calling returnRecipeResults().
     * Stops any previous search if it is still active when this method is called.
     *
     * @param ingredients The list of ingredients to search for relevant recipes for.
     * @param resultOffset Specifies the number of results to offset the returned results by.
     */
    private void beginSearchingForRecipes(ArrayList<String> ingredients, int resultOffset)
    {
        if (searchThread != null)
        {
            searchThread.stop();
        }

        searchThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // Clear the last list of recipes, if any.
                recipes = new ArrayList<>();

                // TODO Add code for recipe searching here.

                // TODO ensure to add all recipes to the recipes ArrayList before calling this
                returnRecipeResults();
            }
        });

        searchThread.run();
    }


    /**
     * Calls the attached RecipeSearcherListener and passes the found list of recipes.
     */
    private void returnRecipeResults()
    {
        listener.onRecipesReady(recipes);
    }

}
