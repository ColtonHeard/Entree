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
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.card.MaterialCardView;

public class IngredientCard extends MaterialCardView implements View.OnClickListener
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
    private int leftSideGuideline, rightSideGuideline;

    private ConstraintLayout layout;
    private ConstraintSet set;

    private ImageView image;
    private TextView title, text;

    public IngredientCard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setId(generateViewId());
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

//        setContentPadding(int left, int top, int right, int bottom)
        setRadius(10);

        set = new ConstraintSet();
        layout = new ConstraintLayout(context, attrs);

        this.addView(layout);

        //instantiate objects
        image = new ImageView(layout.getContext(), attrs);
        image.setId(ImageView.generateViewId());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.food_card_test_image));

        //344, 194 -
//        layout.addView(image, new ConstraintLayout.LayoutParams(dpToPx(172, context), dpToPx(97, context)));
        layout.addView(image);

        title = new TextView(layout.getContext(), attrs);
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

        text = new TextView(layout.getContext(), attrs);
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
//        set.setGuidelineBegin(leftSideGuideline, dpToPx(SIDE_MARGIN, getContext()));
//        set.setGuidelineEnd(rightSideGuideline, dpToPx(SIDE_MARGIN, getContext()));

//        set.setGuidelineEnd(imageBottomGuideline, dpToPx(IMAGE_BOTTOM_MARGIN, getContext()));
//        set.setGuidelineEnd(titleBottomGuideline, dpToPx(TITLE_BOTTOM_MARGIN, getContext()));
//        set.setGuidelineEnd(textBottomGuideline, dpToPx(TEXT_BOTTOM_MARGIN, getContext()));
//        set.setGuidelineBegin(sideGuideline, dpToPx(SIDE_MARGIN, getContext()));
    }

    public void enableChecking()
    {
        this.setCheckable(true);
    }

    public void disableChecking()
    {
        this.setChecked(false);
        this.setCheckable(false);
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

    public void deleteCard()
    {
        ((ViewGroup) getParent()).removeView(this);
    }

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
