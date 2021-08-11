package com.example.entree;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.card.MaterialCardView;

/**
 Represents a single Material Design card responsible for displaying the image and basic information about an ingredient.
 */
public class IngredientCard extends MaterialCardView implements View.OnClickListener
{
    /** Constant representing the left side of a view. */
    protected final int LEFT = ConstraintSet.LEFT;

    /** Constant representing the right side of a view. */
    protected final int RIGHT = ConstraintSet.RIGHT;

    /** Constant representing the top side of a view. */
    protected final int TOP = ConstraintSet.TOP;

    /** Constant representing the bottom side of a view. */
    protected final int BOTTOM = ConstraintSet.BOTTOM;

    /** ConstraintSet object responsible for adding and applying constraints between the subviews in this object. */
    protected ConstraintSet set;

    private int imageBottomGuideline;
    private int titleBottomGuideline;
    private int textBottomGuideline;
    private int leftSideGuideline, rightSideGuideline;

    /** The inner ConstraintLayout of this IngredientCard */
    private final ConstraintLayout layout;

    /**
     Creates and lays out a new IngredientCard using the default image.

     @param context The application context to create this RecipeCard in.
     @param attrs The attribute set to initialize this view with.
     */
    public IngredientCard(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.setId(generateViewId());
        this.setCardElevation(2);
        this.setCheckedIcon(AppCompatResources.getDrawable(context, R.drawable.close_icon));

        int[][] states = new int[][] {
                new int[] {android.R.attr.state_checked} // checked
        };

        int[] colors = new int[] {
                getResources().getColor(R.color.entree_dark_red),
        };
        ColorStateList list = new ColorStateList(states, colors);

        this.setCheckedIconTint(list);
        this.setOnClickListener(this);
        this.setRadius(10);

        set = new ConstraintSet();
        layout = new ConstraintLayout(context, attrs);

        this.addView(layout);

        //instantiate objects
        ImageView image = new ImageView(layout.getContext(), attrs);
        image.setId(ImageView.generateViewId());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.food_card_test_image));

        layout.addView(image);

        TextView title = new TextView(layout.getContext(), attrs);
        title.setId(TextView.generateViewId());
        title.setText("Ingredient Name");
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(12);
        title.setGravity(Gravity.BOTTOM | Gravity.LEFT);

        layout.addView(title, new ConstraintLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT));

        String testDescription = "Description";

        for (int i = 0; i < ((int) (Math.random() * 10)); i++)
        {
            testDescription += " details";
        }

        TextView text = new TextView(layout.getContext(), attrs);
        text.setId(TextView.generateViewId());
        text.setText(testDescription);
        text.setTextColor(getResources().getColor(R.color.light_gray));
        text.setTextSize(8);
        text.setMaxLines(3);
        text.setEllipsize(TextUtils.TruncateAt.END);

        layout.addView(text, new ConstraintLayout.LayoutParams(0, 0));

        //layout constraints
        set.clone(layout);

        initializeGuidelines();

        to(image, TOP, this, TOP);
        to(image, LEFT, this, LEFT);
        to(image, BOTTOM, imageBottomGuideline, TOP);
        to(image, RIGHT, this, RIGHT);

        to(title, LEFT, leftSideGuideline, RIGHT);
        to(title, BOTTOM, titleBottomGuideline, TOP);
        to(title, RIGHT, rightSideGuideline, LEFT);
        to(title, TOP, image, BOTTOM);

        to(text, LEFT, leftSideGuideline, RIGHT);
        to(text, BOTTOM, textBottomGuideline, TOP);
        to(text, RIGHT, rightSideGuideline, LEFT);
        to(text, TOP, title, BOTTOM);

        set.applyTo(layout);
    }

    /**
     Initializes and positions the guidelines so they can be used to layout components.
     */
    private void initializeGuidelines()
    {
        imageBottomGuideline = Guideline.generateViewId();
        titleBottomGuideline = Guideline.generateViewId();
        textBottomGuideline = Guideline.generateViewId();
        leftSideGuideline = Guideline.generateViewId();
        rightSideGuideline = Guideline.generateViewId();

        set.create(imageBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(titleBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(textBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(leftSideGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(rightSideGuideline, ConstraintSet.VERTICAL_GUIDELINE);

        set.setGuidelinePercent(imageBottomGuideline, 0.65f);
        set.setGuidelinePercent(titleBottomGuideline, 0.80f);
        set.setGuidelinePercent(textBottomGuideline, 0.95f);
        set.setGuidelinePercent(leftSideGuideline, 0.05f);
        set.setGuidelinePercent(rightSideGuideline, 0.95f);
    }

    /**
     Changes this MaterialCard to be checkable when clicked by the user.
     */
    public void enableChecking()
    {
        this.setCheckable(true);
    }

    /**
     Changes this MaterialCard to be uncheckable and no longer respond to clicks by the user.
     */
    public void disableChecking()
    {
        this.setChecked(false);
        this.setCheckable(false);
    }

    /**
     Helper method that makes a constraint connection from view a to view b.
     Connection starts on a's from side and connects to b's to side.

     @param a The view to start a connection from.
     @param from The side of a to start the constraint connection.
     @param b The view to connect to.
     @param to The side of the view to connect to.
     */
    protected void to(View a, int from, View b, int to)
    {
        set.connect(a.getId(), from, b.getId(), to);
    }

    /**
     Overloaded version allowing for a constraint connection to be made to a guideline instead of a view object.

     @param a The view to start a connection from.
     @param from The side of a to start the constraint connection.
     @param guidelineID The id of the guideline to connect to.
     @param to The side of the guideline to connect to.
     @see #to(View, int, View, int)
     */
    protected void to(View a, int from, int guidelineID, int to)
    {
        set.connect(a.getId(), from, guidelineID, to);
    }

    /**
     Removes this card from it's parent view.
     */
    public void deleteCard()
    {
        ((ViewGroup) getParent()).removeView(this);
    }

    /**
     Handler for the onClick UI event. Will check the card if checking is enabled. Otherwise will open a NutritionView for the ingredient.

     @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        if (isCheckable())
        {
            setChecked(!isChecked());
        }
        else
        {
            //Open more nutrition information
        }
    }
}
