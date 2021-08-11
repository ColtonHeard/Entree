package com.example.entree;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.chip.Chip;

/**
 Represents a single Material Design chip that is responsible for a single ingredient.
 */
public class IngredientChip extends Chip
{
    /** The randomly generated label for this chip. Should be used as a placeholder and REMOVED later. */
    private final String randomLabel;

    /**
     Initializes this chip as checkable and adds the passed RecipeView as an OnClickListener. Generates a random label for the chip.

     @param context The application context to create this RecipeCard in.
     @param attrs The attribute set to initialize this view with.
     @param recipe The containing RecipeView for this IngredientChip.
     */
    public IngredientChip(Context context, AttributeSet attrs, RecipeView recipe)
    {
        super(context, attrs);

        this.setOnClickListener(recipe);
        this.setCheckable(true);
        this.setCheckedIconVisible(true);
        this.setCheckedIcon(AppCompatResources.getDrawable(getContext(), R.drawable.dark_check_icon));

        int[][] states = new int[][] {
                new int[] {android.R.attr.state_checked}, // checked
                new int[] {-android.R.attr.state_checked} // unchecked
        };

        int[] colors = new int[] {
                getResources().getColor(R.color.entree_dark_red),
                getResources().getColor(R.color.entree_dark_orange)
        };
        ColorStateList list = new ColorStateList(states, colors);

        this.setChipBackgroundColor(list);

        String text = "Ingred";

        for (int i = 0; i < ((int) (Math.random() * 4)); i++)
        {
            text += "-indent";
        }

        randomLabel = text;
        this.setText(text);
    }

    /**
     Changes the text on this Chip to the passed String. If text is null, defaults back to it's random label.

     @param text The text to change this chip's label to.
     */
    public void setChipText(String text)
    {
        if (text != null) {
            this.setText(text);
        }
        else
        {
            this.setText(randomLabel);
        }
    }
}
