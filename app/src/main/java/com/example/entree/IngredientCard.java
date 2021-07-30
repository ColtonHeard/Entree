package com.example.entree;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.card.MaterialCardView;

public class IngredientCard extends MaterialCardView
{
    /*
    Constants for specifying which side of a view to connect a constraint to.
     */
    protected final int LEFT = ConstraintSet.LEFT;
    protected final int RIGHT = ConstraintSet.RIGHT;
    protected final int TOP = ConstraintSet.TOP;
    protected final int BOTTOM = ConstraintSet.BOTTOM;

    private final int IMAGE_BOTTOM_MARGIN = 116;
    private final int TITLE_BOTTOM_MARGIN = 72;
    private final int TEXT_BOTTOM_MARGIN = 44;
    private final int SIDE_MARGIN = 16;

    private int imageBottomGuideline;
    private int titleBottomGuideline;
    private int textBottomGuideline;
    private int sideGuideline;

    private ConstraintLayout layout;
    private ConstraintSet set;

    private ImageView image;
    private TextView title, text;

    public IngredientCard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setId(generateViewId());

//        setContentPadding(int left, int top, int right, int bottom)
        setRadius(10);

        set = new ConstraintSet();
        layout = new ConstraintLayout(context, attrs);

        this.addView(layout);

        //instantiate objects
        image = new ImageView(layout.getContext(), attrs);
        image.setId(ImageView.generateViewId());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.foodimage));

        layout.addView(image);

        title = new TextView(layout.getContext(), attrs);
        title.setId(TextView.generateViewId());
        title.setText("Ingredient Name");
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(17);

        layout.addView(title);

        text = new TextView(layout.getContext(), attrs);
        text.setId(TextView.generateViewId());
        text.setText("Description");
        text.setTextColor(getResources().getColor(R.color.light_gray));
        text.setTextSize(15);

        layout.addView(text);

        //layout constraints
        set.clone(layout);

        initializeGuidelines();

        to(image, TOP, this, TOP);
        to(image, LEFT, this, LEFT);
        to(image, BOTTOM, imageBottomGuideline, TOP);
        to(image, RIGHT, this, RIGHT);

        to(title, LEFT, sideGuideline, RIGHT);
        to(title, BOTTOM, titleBottomGuideline, TOP);

        to(text, LEFT, sideGuideline, RIGHT);
        to(text, BOTTOM, textBottomGuideline, TOP);

        set.applyTo(layout);
    }

    private void initializeGuidelines()
    {
        imageBottomGuideline = Guideline.generateViewId();
        titleBottomGuideline = Guideline.generateViewId();
        textBottomGuideline = Guideline.generateViewId();
        sideGuideline = Guideline.generateViewId();

        set.create(imageBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(titleBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(textBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(sideGuideline, ConstraintSet.VERTICAL_GUIDELINE);

        set.setGuidelinePercent(imageBottomGuideline, 0.75f);
        set.setGuidelinePercent(titleBottomGuideline, 0.85f);
        set.setGuidelinePercent(textBottomGuideline, 0.95f);
        set.setGuidelineBegin(sideGuideline, dpToPx(SIDE_MARGIN, getContext()));

//        set.setGuidelineEnd(imageBottomGuideline, dpToPx(IMAGE_BOTTOM_MARGIN, getContext()));
//        set.setGuidelineEnd(titleBottomGuideline, dpToPx(TITLE_BOTTOM_MARGIN, getContext()));
//        set.setGuidelineEnd(textBottomGuideline, dpToPx(TEXT_BOTTOM_MARGIN, getContext()));
//        set.setGuidelineBegin(sideGuideline, dpToPx(SIDE_MARGIN, getContext()));
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

    private int dpToPx(int dp, Context context)
    {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
