package com.example.entree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.fonts.FontStyle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.chip.Chip;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class RecipeView extends EntreeConstraintView implements View.OnClickListener, View.OnLongClickListener
{

    private int chipTextGuideline, dividerTopGuideline, dividerBottomGuideline, recipeTextGuideline, leftGuideline, rightGuideline, topGuideline, bottomGuideline;

    private LinearLayout topChipContainer, bottomChipContainer, recipeList;
    private LinearLayout.LayoutParams chipParams, emptyTextParams;
    private TextView recipeEmptyText;
    private ArrayList<IngredientChip> chips;
    private ArrayList<RecipeCard> recipes;
    private MainActivity mainActivity;
    private LinearLayout.LayoutParams recipeCardParams;

    private static final Executor executor = new Executor() {
        @Override
        public void execute(Runnable command)
        {
            command.run();
        }
    };

    private int count;

    public RecipeView(@NonNull Context context, @Nullable AttributeSet attrs, MainActivity main)
    {
        super(context, attrs);

        this.setOnLongClickListener(this);

        chips = new ArrayList<>();
        recipes = null;
        mainActivity = main;

        count = 0;
        chipParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        chipParams.setMargins(8,0,8,0);

        emptyTextParams = new LinearLayout.LayoutParams(1200, 1200);
        emptyTextParams.gravity = Gravity.CENTER;

        recipeCardParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 350);
        recipeCardParams.gravity = Gravity.CENTER;
        recipeCardParams.setMargins(0, 10, 0, 10);


        /*
        Being initializing view components
         */
        TextView chipText = new TextView(context, attrs);
        chipText.setId(TextView.generateViewId());
        chipText.setText("Search Ingredients");
        chipText.setTextSize(24);
        chipText.setGravity(Gravity.LEFT);
//        chipText.setTypeface(chipText.getTypeface(), Typeface.BOLD);
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

        ScrollView recipeScroll = new ScrollView(context, attrs);
        recipeScroll.setId(ScrollView.generateViewId());
//        recipeScroll.setBackgroundColor(getResources().getColor(R.color.light_gray));
        this.addView(recipeScroll, new ConstraintLayout.LayoutParams(0, 0));

        recipeList = new LinearLayout(context, attrs);
        recipeList.setOrientation(LinearLayout.VERTICAL);

        recipeScroll.addView(recipeList);

        recipeEmptyText = new TextView(context, attrs);
        recipeEmptyText.setText("No results: Add ingredients or change the search ingredients");
        recipeEmptyText.setTextColor(getResources().getColor(R.color.black));
        recipeEmptyText.setGravity(Gravity.CENTER);
        recipeEmptyText.setMaxLines(100);
        recipeEmptyText.setTextSize(18);

        recipeList.addView(recipeEmptyText, emptyTextParams);

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

//        for (int i = 0; i < 10; i++)
//        {
//            addChip();
//        }
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

    private String getSelectedIngredients()
    {
        String ingredients = "";

        for (IngredientChip chip: chips)
        {
            if (chip.isChecked()) {
                if (ingredients.equals(""))
                {
                    ingredients += chip.getText().toString().toLowerCase();
                }
                else
                {
                    ingredients += "+" + chip.getText().toString().toLowerCase();
                }
            }
        }

        return ingredients;
    }

    private void getWebsite()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                ArrayList<RecipeCard> foundRecipes = new ArrayList<>();

                try {
                    Log.d("JSoupURL", "https://www.google.com/search?q=" + getSelectedIngredients() + "+recipes");
                    Document doc = Jsoup.connect("https://www.google.com/search?q=" + getSelectedIngredients() + "+recipes").get();
                    String title = doc.title();
                    Elements links = doc.select("a[href]");

                    builder.append(title).append("\n");

                    int skipCount = 0;
                    for(Element link: links)
                    {
                        if (skipCount < 4)
                        {
                            skipCount++;
                            continue;
                        }

                        if(!link.text().equals("Images") && !link.text().equals("Videos") && !link.text().equals("News") && !link.text().equals("Maps")
                                && !link.text().equals("Shopping") && !link.text().equals("Books") && !link.text().equals("Flights") && !link.text().equals("More results")
                                && !link.text().equals("Feedback") && !link.text().equals(""))
                        {
                            builder.append("------------------").append("\n").append("Link : ").append(link.attr("href"))
                                    .append("\n").append("Title : ").append(link.text()).append("\n");

                            RecipeCard card = new RecipeCard(getContext(), null, link.attr("href"), link.text());
                            foundRecipes.add(card);

                        }
                    }
                } catch (IOException e)
                {
                    builder.append("Error :").append(e.getMessage()).append("\n");
                }

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recipes = foundRecipes;
                        performLongClick();
                    }
                });
            }
        }).start();
    }

//    private void triggerLongClick()
//    {
//        this.performLongClick();
//    }

    @Override
    public void onClick(View v)
    {
        getWebsite();
    }

    @Override
    public boolean onLongClick(View v)
    {
        if (recipes != null)
        {
            recipeList.removeAllViews();
            for (RecipeCard card: recipes)
            {
                recipeList.addView(card, recipeCardParams);
            }
//            recipes = null;
//            recipeEmptyText.setText("Heck");
        }
        return true;
    }
}
