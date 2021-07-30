package com.example.entree;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.card.MaterialCardView;

public class IngredientView extends EntreeConstraintView
{

    private final int CARD_HEIGHT = 50;
    private final int CARD_WIDTH = 100;
    private final int TOP_MARGIN = 60;
    private final int SIDE_MARGIN = 40;
    private final int MIDDLE_MARGIN = 10;
    private final int CARD_MARGIN = 10;

    private int topGuideline;
    private int firstLeftGuideline, firstRightGuideline;
    private int secondLeftGuideline, secondRightGuideline;

    private LinearLayout left, right;

    private int count;

    public IngredientView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        count = 0;

        //Initialize Content
        left = new LinearLayout(context, attrs);
        left.setId(LinearLayout.generateViewId());
        left.setBackgroundColor(getResources().getColor(R.color.light_gray));
        left.setOrientation(LinearLayout.VERTICAL);

        this.addView(left);

        right = new LinearLayout(context, attrs);
        right.setId(LinearLayout.generateViewId());
        left.setBackgroundColor(getResources().getColor(R.color.entree_red));
        left.setOrientation(LinearLayout.VERTICAL);


        this.addView(right);

//        card.setOnLongClickListener {
//            card.setChecked(!card.isChecked)
//            true
//        }

        //Setup Constraints
        set.clone(this);

        initializeGuidelines();

        to(left, TOP, topGuideline, BOTTOM);
        to(left, LEFT, firstLeftGuideline, RIGHT);
        to(left, RIGHT, firstRightGuideline, LEFT);

        to(right, TOP, topGuideline, BOTTOM);
        to(right, LEFT, secondLeftGuideline, RIGHT);
        to(right, RIGHT, secondRightGuideline, LEFT);

        this.setConstraintSet(set);
        set.applyTo(this);

//        addCard();
//        addCard();
//        addCard();
    }

    private void addCard()
    {
        IngredientCard card = new IngredientCard(getContext(), null);
        this.addView(card);

        set.clone(this);

        int cardsBefore = 0;

        for (int i = count; i > 0; )
        {
            i -= 2;

            if (i >= 0)
                cardsBefore++;
        }

        Log.d("CardsBefore", "" + cardsBefore);

        int topLineMargin = TOP_MARGIN + ((CARD_HEIGHT + CARD_MARGIN) * cardsBefore);
        int bottomLineMargin = topLineMargin + CARD_HEIGHT;

        Log.d("CardsBefore", "" + topLineMargin);
        Log.d("CardsBefore", "" + bottomLineMargin);

        int topLine = Guideline.generateViewId();
        int bottomLine = Guideline.generateViewId();
        set.create(topLine, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomLine, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.setGuidelineBegin(topLine, topLineMargin);
        set.setGuidelineBegin(bottomLine, bottomLineMargin);

        if (count == 0 || (count % 2 == 0))
        {
            to(card, TOP, topLine, BOTTOM);
            to(card, LEFT, firstLeftGuideline, RIGHT);
            to(card, BOTTOM, bottomLine, TOP);
            to(card, RIGHT, firstRightGuideline, LEFT);
        }
        else
        {
            to(card, TOP, topLine, BOTTOM);
            to(card, LEFT, secondLeftGuideline, RIGHT);
            to(card, BOTTOM, bottomLine, TOP);
            to(card, RIGHT, secondRightGuideline, LEFT);
        }

        count++;

        this.setConstraintSet(set);
        set.applyTo(this);
    }

    private void initializeGuidelines()
    {
        topGuideline = Guideline.generateViewId();
        firstLeftGuideline = Guideline.generateViewId();
        firstRightGuideline = Guideline.generateViewId();
        secondLeftGuideline = Guideline.generateViewId();
        secondRightGuideline = Guideline.generateViewId();

        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(firstLeftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(firstRightGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(secondLeftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(secondRightGuideline, ConstraintSet.VERTICAL_GUIDELINE);

//        set.setGuidelineBegin(topGuideline, TOP_MARGIN);
//        set.setGuidelineBegin(firstLeftGuideline, SIDE_MARGIN);
//        set.setGuidelinePercent(firstRightGuideline, 0.45f);
//        set.setGuidelinePercent(secondLeftGuideline, 0.55f);
//        set.setGuidelineEnd(secondRightGuideline, SIDE_MARGIN);

        set.setGuidelineBegin(topGuideline, TOP_MARGIN);
        set.setGuidelineBegin(firstLeftGuideline, SIDE_MARGIN);
        set.setGuidelineBegin(firstRightGuideline, SIDE_MARGIN + CARD_WIDTH);
        set.setGuidelineEnd(secondLeftGuideline, SIDE_MARGIN + CARD_WIDTH);
        set.setGuidelineEnd(secondRightGuideline, SIDE_MARGIN);

//        set.setGuidelineBegin(topGuideline, dpToPx(TOP_MARGIN, getContext()));
//        set.setGuidelineBegin(firstLeftGuideline, dpToPx(SIDE_MARGIN, getContext()));
//        set.setGuidelineBegin(firstRightGuideline, dpToPx(SIDE_MARGIN + CARD_WIDTH, getContext()));
//        set.setGuidelineEnd(secondLeftGuideline, dpToPx(SIDE_MARGIN + CARD_WIDTH, getContext()));
//        set.setGuidelineEnd(secondRightGuideline, dpToPx(SIDE_MARGIN, getContext()));
    }

}
