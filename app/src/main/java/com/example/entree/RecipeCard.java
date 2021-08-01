package com.example.entree;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.card.MaterialCardView;

public class RecipeCard extends MaterialCardView implements View.OnClickListener
{
    /*
    Constants for specifying which side of a view to connect a constraint to.
     */
    protected final int LEFT = ConstraintSet.LEFT;
    protected final int RIGHT = ConstraintSet.RIGHT;
    protected final int TOP = ConstraintSet.TOP;
    protected final int BOTTOM = ConstraintSet.BOTTOM;

    private int leftGuideline, topGuideline, rightGuideline, bottomGuideline;
    private int imageRightGuideline, textLeftGuideline, titleBottomGuideline;

    private ConstraintLayout layout;
    private ConstraintSet set;

    private ImageView image;
    private TextView title, text;

    private String link;

    public RecipeCard(Context context, AttributeSet attrs, String recipeLink, String recipeTitle)
    {
        super(context, attrs);
        setId(generateViewId());
        this.setCardElevation(6);
        this.setOnClickListener(this);
//        this.setBackgroundColor(getResources().getColor(R.color.light_gray));

        setRadius(10);

        set = new ConstraintSet();
        layout = new ConstraintLayout(context, attrs);
        link = recipeLink;

        this.addView(layout);

        //instantiate objects
        image = new ImageView(layout.getContext(), attrs);
        image.setId(ImageView.generateViewId());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.foodimage));

        layout.addView(image, new ConstraintLayout.LayoutParams(0, 0));

        title = new TextView(layout.getContext(), attrs);
        title.setId(TextView.generateViewId());
        title.setText(recipeTitle);
//        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTextSize(20);
        title.setMaxLines(1);
        title.setGravity(Gravity.TOP | Gravity.LEFT);
        title.setEllipsize(TextUtils.TruncateAt.END);

        layout.addView(title, new ConstraintLayout.LayoutParams(0, 0));

        String testDescription = "Filler text";

        for (int i = 0; i < ((int) (Math.random() * 80)); i++)
        {
            testDescription += " text";
        }

        text = new TextView(layout.getContext(), attrs);
        text.setId(TextView.generateViewId());
        text.setText(testDescription);
        text.setTextColor(getResources().getColor(R.color.light_gray));
        text.setTextSize(12);
        text.setMaxLines(3);
        text.setGravity(Gravity.TOP | Gravity.LEFT);
        text.setEllipsize(TextUtils.TruncateAt.END);

        layout.addView(text, new ConstraintLayout.LayoutParams(0, 0));

        //layout constraints
        set.clone(layout);

        initializeGuidelines();

        to(image, TOP, topGuideline, BOTTOM);
        to(image, LEFT, leftGuideline, RIGHT);
        to(image, BOTTOM, bottomGuideline, TOP);
        to(image, RIGHT, imageRightGuideline, LEFT);

        to(title, LEFT, textLeftGuideline, RIGHT);
        to(title, BOTTOM, titleBottomGuideline, TOP);
        to(title, RIGHT, rightGuideline, LEFT);
        to(title, TOP, topGuideline, BOTTOM);

        to(text, LEFT, textLeftGuideline, RIGHT);
        to(text, BOTTOM, bottomGuideline, TOP);
        to(text, RIGHT, rightGuideline, LEFT);
        to(text, TOP, titleBottomGuideline, BOTTOM);

        layout.setConstraintSet(set);
        set.applyTo(layout);
    }

//    private int leftGuideline, topGuideline, rightGuideline, bottomGuideline;
//    private int imageRightGuideline, titleBottomGuideline;

    private void initializeGuidelines()
    {
        leftGuideline = Guideline.generateViewId();
        rightGuideline = Guideline.generateViewId();
        imageRightGuideline = Guideline.generateViewId();
        topGuideline = Guideline.generateViewId();
        bottomGuideline = Guideline.generateViewId();
        titleBottomGuideline = Guideline.generateViewId();
        textLeftGuideline = Guideline.generateViewId();

        set.create(leftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(rightGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(imageRightGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(textLeftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(titleBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelinePercent(leftGuideline, 0.02f);
        set.setGuidelinePercent(rightGuideline, 0.98f);
        set.setGuidelinePercent(imageRightGuideline, 0.25f);
        set.setGuidelinePercent(textLeftGuideline, 0.29f);
        set.setGuidelinePercent(topGuideline, 0.05f);
        set.setGuidelinePercent(bottomGuideline, 0.95f);
        set.setGuidelinePercent(titleBottomGuideline, 0.4f);
    }

    /*
    Helper method that makes a constraint connection from view a to view b.
    Connection starts on a's from side and connects to b's to side.
     */
    protected void to(View a, int from, View b, int to)
    {
        set.connect(a.getId(), from, b.getId(), to);
    }

    /*
    Overloaded version allowing for a constraint connection to be made to a guideline instead of a view object.
     */
    protected void to(View a, int from, int guidelineID, int to)
    {
        set.connect(a.getId(), from, guidelineID, to);
    }

    @Override
    public void onClick(View v)
    {
        //Open link
    }
}

