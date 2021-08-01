package com.example.entree;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.chip.Chip;

public class IngredientChip extends Chip
{

    private String randomLabel;

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
