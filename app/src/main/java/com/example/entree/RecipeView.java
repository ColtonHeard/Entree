package com.example.entree;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.fonts.FontStyle;
import android.util.AttributeSet;
import android.view.Gravity;
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

public class RecipeView extends EntreeConstraintView
{

    private int chipTextGuideline, dividerGuideline, recipeTextGuideline, leftGuideline, rightGuideline, topGuideline, bottomGuideline;

    LinearLayout topChipContainer, bottomChipContainer;
    LinearLayout.LayoutParams chipParams, emptyTextParams;

    private int count;

    public RecipeView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        count = 0;
        chipParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        chipParams.setMargins(8,0,8,0);

        emptyTextParams = new LinearLayout.LayoutParams(1200, 1200);
        emptyTextParams.gravity = Gravity.CENTER;

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

        LinearLayout recipeList = new LinearLayout(context, attrs);
        recipeList.setOrientation(LinearLayout.VERTICAL);

        recipeScroll.addView(recipeList);

        TextView recipeEmptyText = new TextView(context, attrs);
        recipeEmptyText.setText("No results: Add ingredients or change the search ingredients");
        recipeEmptyText.setTextColor(getResources().getColor(R.color.black));
        recipeEmptyText.setGravity(Gravity.CENTER);
        recipeEmptyText.setMaxLines(2);
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
        to(chipScroll, BOTTOM, dividerGuideline, TOP);
        to(chipScroll, RIGHT, rightGuideline, LEFT);

        to(recipeText, TOP, dividerGuideline, BOTTOM);
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

        for (int i = 0; i < 10; i++)
        {
            addChip();
        }
    }

    public void addChip()
    {
        IngredientChip chip = new IngredientChip(getContext(), null);

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

        count++;
    }

    private void initializeGuidelines()
    {
        chipTextGuideline = Guideline.generateViewId();
        dividerGuideline = Guideline.generateViewId();
        recipeTextGuideline = Guideline.generateViewId();
        leftGuideline = Guideline.generateViewId();
        rightGuideline = Guideline.generateViewId();
        topGuideline = Guideline.generateViewId();
        bottomGuideline = Guideline.generateViewId();

        set.create(chipTextGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(dividerGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(recipeTextGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(leftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(rightGuideline, ConstraintSet.VERTICAL_GUIDELINE);

        set.setGuidelinePercent(chipTextGuideline, 0.08f);
        set.setGuidelinePercent(dividerGuideline, 0.24f);
        set.setGuidelinePercent(recipeTextGuideline, 0.29f);
        set.setGuidelinePercent(topGuideline, 0.03f);
        set.setGuidelinePercent(bottomGuideline, 0.97f);
        set.setGuidelinePercent(leftGuideline, 0.03f);
        set.setGuidelinePercent(rightGuideline, 0.97f);
    }

}
