package com.example.entree;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.card.MaterialCardView;

public class IngredientView extends EntreeConstraintView
{

    private final int CARD_HEIGHT = 310;
    private final int CARD_WIDTH = 344;



    public IngredientView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TextView text = new TextView(context, attrs);
        text.setText("Ingredient List");
        this.addView(text);

        IngredientCard card = new IngredientCard(context, attrs);

        this.addView(card);

//        card.setOnLongClickListener {
//            card.setChecked(!card.isChecked)
//            true
//        }
    }


}
