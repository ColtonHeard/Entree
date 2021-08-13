package com.example.entree;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.example.entree.recipe.Recipe;
import com.example.entree.recipe.RecipesDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 Represents a double list view where the top section contains a horizontally scrolling list of IngredientChips and the bottom section contains a vertically scrolling list of RecipeCards.
 */
public class RecipeView extends EntreeConstraintView implements View.OnClickListener, View.OnLongClickListener, RecipesDataSource.RecipeDataSourceListener, View.OnScrollChangeListener
{

    private int chipTextGuideline, dividerTopGuideline, dividerBottomGuideline, recipeTextGuideline, leftGuideline, rightGuideline, topGuideline, bottomGuideline;

    /** Horizontal LinearLayouts responsible for containing the IngredientChips. */
    private final LinearLayout topChipContainer, bottomChipContainer;

    /** Vertical Linearlayout responsible for containing the RecipeCards. */
    private final LinearLayout recipeList;

    /** The ScrollView that contains recipe results. */
    private final ScrollView recipeScroll;

    /** LayoutParams for specifying how chips and the EmptyText view should be layed out. */
    private final LinearLayout.LayoutParams chipParams, emptyTextParams, recipeCardParams;

    /** TextView for when no IngredientChips are selected or no results can be found from the web scraper. */
    private final TextView recipeEmptyText;

    /** Image shown to the user while waiting for recipe results to be returned. */
    private final ImageView loadingImage;

    /** The animation applied to the loading image. */
    private final RotateAnimation loadingAnimation;

    /** ArrayList containing all of the IngredientChips in this view. */
    private ArrayList<IngredientChip> chips;

    /** ArrayList containing all the RecipeCards in this view. */
    private ArrayList<Recipe> recipes;

    /** Reference to the application's MainActivity */
    private final MainActivity mainActivity;

    /** The data source to load new recipes from. */
    private final RecipesDataSource recipesDataSource;

    /** Represents the number of IngredientChips currently added to this view. */
    private int count;

    /** Represents if the view currently has a list of RecipeResults */
    private boolean hasResults;

    private boolean loading;

    /** The last total number of IngredientChips that were selected. */
    private int lastSelectedCount;

    /**
     * Creates a new RecipeView with the appropriate layout.
     *
     * @param context The application context to initialize this view in.
     * @param attrs The attribute set to apply to this view.
     * @param main A reference to the application's MainActivity.
     */
    public RecipeView(@NonNull Context context, @Nullable AttributeSet attrs, MainActivity main)
    {
        super(context, attrs);

        this.setOnLongClickListener(this);

        chips = new ArrayList<>();
        recipes = null;
        mainActivity = main;
        recipesDataSource = RecipesDataSource.makeDataSource(RecipesDataSource.Sources.ALL_RECIPES, this);
        hasResults = false;
        lastSelectedCount = 0;

        count = 0;
        chipParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        chipParams.setMargins(8,0,8,0);

        emptyTextParams = new LinearLayout.LayoutParams(1200, 1200);
        emptyTextParams.gravity = Gravity.CENTER;

        recipeCardParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 350);
        recipeCardParams.gravity = Gravity.CENTER;
        recipeCardParams.setMargins(0, 10, 0, 10);

        // Begin initializing view components
        TextView chipText = new TextView(context, attrs);
        chipText.setId(TextView.generateViewId());
        chipText.setText("Search Ingredients");
        chipText.setTextSize(24);
        chipText.setGravity(Gravity.LEFT);
        chipText.setTextColor(getResources().getColor(R.color.black));
        this.addView(chipText, new ConstraintLayout.LayoutParams(0, 0));

        HorizontalScrollView chipScroll = new HorizontalScrollView(context, attrs);
        chipScroll.setId(ScrollView.generateViewId());
        chipScroll.setHorizontalScrollBarEnabled(false);
        this.addView(chipScroll, new ConstraintLayout.LayoutParams(0, 0));

        LinearLayout linearContainer = new LinearLayout(context, attrs);
        linearContainer.setOrientation(LinearLayout.VERTICAL);

        topChipContainer = new LinearLayout(context, attrs);
        topChipContainer.setOrientation(LinearLayout.HORIZONTAL);

        bottomChipContainer = new LinearLayout(context, attrs);
        bottomChipContainer.setOrientation(LinearLayout.HORIZONTAL);

        linearContainer.addView(topChipContainer);
        linearContainer.addView(bottomChipContainer);

        chipScroll.addView(linearContainer);

        View dividerView = new View(context, attrs);
        dividerView.setId(View.generateViewId());
        dividerView.setBackgroundColor(getResources().getColor(R.color.black));
        this.addView(dividerView, new ConstraintLayout.LayoutParams(0, 0));

        TextView recipeText = new TextView(context, attrs);
        recipeText.setId(TextView.generateViewId());
        recipeText.setText("Recipes");
        recipeText.setTextSize(24);
        recipeText.setGravity(Gravity.LEFT);
//        recipeText.setTypeface(recipeText.getTypeface(), Typeface.BOLD);
        recipeText.setTextColor(getResources().getColor(R.color.black));
        this.addView(recipeText, new ConstraintLayout.LayoutParams(0, 0));

        recipeScroll = new ScrollView(context, attrs);
        recipeScroll.setId(ScrollView.generateViewId());
        recipeScroll.setOnScrollChangeListener(this);
//        recipeScroll.setBackgroundColor(getResources().getColor(R.color.light_gray));
        this.addView(recipeScroll, new ConstraintLayout.LayoutParams(0, 0));


        recipeList = new LinearLayout(context, attrs);
        recipeList.setOrientation(LinearLayout.VERTICAL);

        recipeScroll.addView(recipeList);

        recipeEmptyText = new TextView(context, attrs);
        recipeEmptyText.setText("No results: Add ingredients or change the search ingredients");
        recipeEmptyText.setTextColor(getResources().getColor(R.color.light_gray));
        recipeEmptyText.setGravity(Gravity.CENTER);
        recipeEmptyText.setMaxLines(100);
        recipeEmptyText.setTextSize(18);

        recipeList.addView(recipeEmptyText, emptyTextParams);

        loadingImage = new ImageView(context, attrs);
        loadingImage.setImageDrawable(getResources().getDrawable(R.drawable.loading_icon));
//        loadingImage.setImageResource(R.drawable.loading_icon);

        loadingAnimation = new RotateAnimation(0, 359.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        loadingAnimation.setDuration(3000);
        loadingAnimation.setRepeatCount(Animation.INFINITE);
        loadingAnimation.setInterpolator(new LinearInterpolator());

        /*
        Begin setting layout constraints
         */
        set.clone(this);

        initializeGuidelines();

        to(chipText, TOP, topGuideline, BOTTOM);
        to(chipText, LEFT, leftGuideline, RIGHT);
        to(chipText, BOTTOM, chipTextGuideline, TOP);
        to(chipText, RIGHT, rightGuideline, LEFT);

        to(chipScroll, TOP, chipTextGuideline, BOTTOM);
        to(chipScroll, LEFT, leftGuideline, RIGHT);
        to(chipScroll, BOTTOM, dividerTopGuideline, TOP);
        to(chipScroll, RIGHT, rightGuideline, LEFT);

        to(dividerView, TOP, dividerTopGuideline, BOTTOM);
        to(dividerView, LEFT, this, LEFT);
        to(dividerView, BOTTOM, dividerBottomGuideline, TOP);
        to(dividerView, RIGHT, this, RIGHT);

        to(recipeText, TOP, dividerBottomGuideline, BOTTOM);
        to(recipeText, LEFT, leftGuideline, RIGHT);
        to(recipeText, BOTTOM, recipeTextGuideline, TOP);
        to(recipeText, RIGHT, rightGuideline, LEFT);

        to(recipeScroll, TOP, recipeTextGuideline, BOTTOM);
        to(recipeScroll, LEFT, leftGuideline, RIGHT);
        to(recipeScroll, BOTTOM, bottomGuideline, TOP);
        to(recipeScroll, RIGHT, rightGuideline, LEFT);

        /*
        Apply layout constraints and save ConstraintSet to layout
         */
        this.setConstraintSet(set);
        set.applyTo(this);

        addChip("Broccoli");
        addChip("Kale");
        addChip("Egg");
        addChip("Banana");
        addChip("Pork");
        addChip("Milk");
        addChip("Onion");
        addChip("Mushroom");
        addChip("Lettuce");
    }

    /**
     Adds an ingredient chip with the given label. The first two ingredients are added to the top LinearLayout, the second two to the bottom.
     All subsequent chips are added as follows: if there are an even number of chips currently, it is added to the bottom, otherwise it is added to the top LinearLayout.
     This results in a bottom heavy look to the chips.

     @param label The label to set the IngredientChip's text to.
     */
    public void addChip(String label)
    {
        IngredientChip chip = new IngredientChip(getContext(), null, this);
        chip.setChipText(label);

        if (count < 2)
        {
            topChipContainer.addView(chip, chipParams);
        }
        else if (count < 4)
        {
            bottomChipContainer.addView(chip, chipParams);
        }
        else if ((count % 2) == 0)
        {
            bottomChipContainer.addView(chip, chipParams);
        }
        else
        {
            topChipContainer.addView(chip, chipParams);
        }

        chips.add(chip);
        count++;
    }

    /**
    Initializes and positions the guidelines so they can be used to layout components.
     */
    private void initializeGuidelines()
    {
        chipTextGuideline = Guideline.generateViewId();
        dividerTopGuideline = Guideline.generateViewId();
        dividerBottomGuideline = Guideline.generateViewId();
        recipeTextGuideline = Guideline.generateViewId();
        leftGuideline = Guideline.generateViewId();
        rightGuideline = Guideline.generateViewId();
        topGuideline = Guideline.generateViewId();
        bottomGuideline = Guideline.generateViewId();

        set.create(chipTextGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(dividerTopGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(dividerBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(recipeTextGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(leftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(rightGuideline, ConstraintSet.VERTICAL_GUIDELINE);

        set.setGuidelinePercent(chipTextGuideline, 0.08f);
        set.setGuidelinePercent(dividerTopGuideline, 0.24f);
        set.setGuidelinePercent(dividerBottomGuideline, 0.241f);
        set.setGuidelinePercent(recipeTextGuideline, 0.291f);
        set.setGuidelinePercent(topGuideline, 0.03f);
        set.setGuidelinePercent(bottomGuideline, 0.97f);
        set.setGuidelinePercent(leftGuideline, 0.03f);
        set.setGuidelinePercent(rightGuideline, 0.97f);
    }

    /**
     Returns a string containing the names of all selected ingredients in the form "one+two+three+...".
     Intended for use with the web scraper.
     */
    private ArrayList<String> getSelectedIngredients()
    {
        ArrayList<String> ingredients = new ArrayList<>();

        for (IngredientChip chip: chips)
        {
            if (chip.isChecked()) {
                if (ingredients.equals(""))
                {
                    ingredients.add(chip.getText().toString().toLowerCase());
                }
                else
                {
                    ingredients.add(chip.getText().toString().toLowerCase());
                }
            }
        }

        return ingredients;

//        String ingredients = "";
//
//        for (IngredientChip chip: chips)
//        {
//            if (chip.isChecked()) {
//                if (ingredients.equals(""))
//                {
//                    ingredients += chip.getText().toString().toLowerCase();
//                }
//                else
//                {
//                    ingredients += "+" + chip.getText().toString().toLowerCase();
//                }
//            }
//        }
//
//        return ingredients;
    }

    /**
    Runs the web scraper to search for recipes with the currently selected ingredients. Results are stored in the recipes variable before calling this class's OnLongClick method.
     */
    private void getWebsite() {

        if (!loading)
        {
            if (lastSelectedCount != getSelectedCount()) {
                recipeList.removeAllViews();
                lastSelectedCount = getSelectedCount();
                recipesDataSource.setIngredients(getSelectedIngredients());
            }

//            disableChips();
            loading = true;
            recipeList.addView(loadingImage);
            loadingImage.startAnimation(loadingAnimation);

            recipesDataSource.getRecipes();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final StringBuilder builder = new StringBuilder();
//                ArrayList<RecipeCard> foundRecipes = new ArrayList<>();
//
//                try {
//                    Log.d("JSoupURL", "https://www.google.com/search?q=" + getSelectedIngredients() + "+recipes");
//                    Document doc = Jsoup.connect("https://www.google.com/search?q=" + getSelectedIngredients() + "+recipes").get();
//                    String title = doc.title();
//                    Elements links = doc.select("a[href]");
//
//                    builder.append(title).append("\n");
//
//                    int skipCount = 0;
//                    for(Element link: links)
//                    {
//                        if (skipCount < 4)
//                        {
//                            skipCount++;
//                            continue;
//                        }
//
//                        if(!link.text().equals("Images") && !link.text().equals("Videos") && !link.text().equals("News") && !link.text().equals("Maps")
//                                && !link.text().equals("Shopping") && !link.text().equals("Books") && !link.text().equals("Flights") && !link.text().equals("More results")
//                                && !link.text().equals("Feedback") && !link.text().equals(""))
//                        {
//                            builder.append("------------------").append("\n").append("Link : ").append(link.attr("href"))
//                                    .append("\n").append("Title : ").append(link.text()).append("\n");
//
//                            RecipeCard card = new RecipeCard(getContext(), getSelf(),null, link.attr("href"), link.text());
//                            foundRecipes.add(card);
//
//                        }
//                    }
//                } catch (IOException e)
//                {
//                    builder.append("Error :").append(e.getMessage()).append("\n");
//                }
//
//                mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 14; i++)
//                        {
//                            foundRecipes.remove(foundRecipes.size() - 1);
//                        }
//                        recipes = foundRecipes;
//                        performLongClick();
//                    }
//                });
//            }
//        }).start();
    }

    private RecipeView getSelf()
    {
        return this;
    }

    /**
     Called by one of the RecipeCard subviews. Opens a browser to the com.example.entree.recipe using the application's MainActivity.

     @param intent Should be a ACTION_VIEW intent with a Uri attatched.
     */
    public void openBrowserIntent(Intent intent)
    {
        mainActivity.startActivity(intent);
    }

    private int getSelectedCount()
    {
        int count = 0;
        for (IngredientChip chip: chips)
        {
            if (chip.isChecked()) {
                count++;
            }
        }
        return count;
    }

    private void enableChips()
    {
        for (IngredientChip chip: chips)
        {
            chip.setCheckable(true);
        }
    }

    private void disableChips()
    {
        for (IngredientChip chip: chips)
        {
            chip.setCheckable(false);
        }
    }

    /**
     UI handler method which calls the web scraper after an IngredientChip has been selected.

     @param v The view that received the click.
     */
    @Override
    public void onClick(View v)
    {
        getWebsite();
    }

    /**
     Called by the web scraper to signal when scraping has finished. Updates the UI with the found recipes.

     @param v The view that received the long click.
     */
    @Override
    public boolean onLongClick(View v)
    {
        loadingImage.clearAnimation();
        recipeList.removeView(loadingImage);
        int count = getSelectedCount();

        if (count == 0)
        {
            recipeList.removeAllViews();
            recipeList.addView(recipeEmptyText, emptyTextParams);
        }
        else if (recipes != null)
        {
            for (Recipe recipe: recipes)
            {
                recipeList.addView(new RecipeCard(getContext(), this, null, recipe), recipeCardParams);
            }
            recipeList.removeView(recipeEmptyText);
        }
        loading = false;
//        enableChips();
        return true;
    }

    @Override
    public void onRecipesReady(ArrayList<Recipe> recipes)
    {
        this.recipes = recipes;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLongClick(null);
            }
        });
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
    {
        int diff = ((recipeScroll.getBottom() - recipeScroll.getTop()) - (recipeScroll.getHeight() + scrollY));
//        int diff = recipeScroll.getMaxScrollAmount() - scrollY;

        Log.d("RecipeScroll", "diff is " + diff);
        // if diff is zero, then the bottom has been reached
        if (!loading && lastSelectedCount != 0 && diff == 0) {
            getWebsite();
        }
    }
}
