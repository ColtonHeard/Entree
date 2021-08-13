package com.example.entree;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InformationView extends EntreeConstraintView implements View.OnClickListener
{
    /** Constant representing the left side of a view. */
    protected final int LEFT = ConstraintSet.LEFT;

    /** Constant representing the right side of a view. */
    protected final int RIGHT = ConstraintSet.RIGHT;

    /** Constant representing the top side of a view. */
    protected final int TOP = ConstraintSet.TOP;

    /** Constant representing the bottom side of a view. */
    protected final int BOTTOM = ConstraintSet.BOTTOM;

    private int imageBottomGuideline;
    private int buttonBottomGuideline;
    private int buttonTopGuideline;
    private int leftSideGuideline, rightSideGuideline;

    private IngredientView ingredientView;

    MaterialButton closeOut;

    /** The inner ConstraintLayout of this IngredientCard */
    private LinearLayout layout;
//    private final ConstraintLayout layout;

    public InformationView(@NonNull Context context, @Nullable AttributeSet attrs, IngredientView parent, FoodData data) {
        super(context, attrs);

        ingredientView = parent;

        layout = new LinearLayout(context, attrs);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(TextView.generateViewId());
        layout.setGravity(Gravity.RIGHT);

        this.setBackgroundColor(getResources().getColor(R.color.white));

        //instantiate objects
        ImageView image = new ImageView(layout.getContext(), attrs);
        image.setId(ImageView.generateViewId());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.food_card_test_image));

        this.addView(image, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT));

        TextView title = new TextView(layout.getContext(), attrs);
        title.setId(TextView.generateViewId());
        title.setText(data.getName());
        title.setTextColor(getResources().getColor(R.color.white));
//        title.setTypeface(title.getTypeface(), Typeface.ITALIC);
        title.setTextSize(40);
        title.setGravity(Gravity.BOTTOM | Gravity.LEFT);

        this.addView(title, new ConstraintLayout.LayoutParams(0, LayoutParams.MATCH_CONSTRAINT));

        closeOut = new MaterialButton(context, attrs, R.attr.iconButtonType);
        closeOut.setId(View.generateViewId());
        closeOut.setIcon(AppCompatResources.getDrawable(context, R.drawable.close_icon));
        closeOut.setGravity(Gravity.START);
        closeOut.setIconSize(dpToPx(36, context));
        closeOut.setOnClickListener(this);

        this.addView(closeOut, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT));

        this.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        this.addView(layout);

        //layout constraints
        set.clone(this);

        initializeGuidelines();

        to(image, TOP, this, TOP);
        to(image, LEFT, this, LEFT);
        to(image, BOTTOM, imageBottomGuideline, TOP);
        to(image, RIGHT, this, RIGHT);

        to(title, LEFT, leftSideGuideline, RIGHT);
        to(title, BOTTOM, imageBottomGuideline, TOP);
        to(title, RIGHT, rightSideGuideline, LEFT);
        to(title, TOP, this, TOP);

        to(closeOut, TOP, this, TOP);
        to(closeOut, LEFT, this, LEFT);
        to(closeOut, BOTTOM, buttonBottomGuideline, TOP);
        to(closeOut, RIGHT, this, RIGHT);

        to(layout, TOP, imageBottomGuideline, BOTTOM);
        to(layout, BOTTOM, this, BOTTOM);
        to(layout, LEFT, this, LEFT);

        set.applyTo(this);

        ConstraintLayout v = (ConstraintLayout) ((LinearLayout) inflate(context, R.layout.nutrition_view, layout)).getChildAt(0);


        ((TextView) v.getViewById(R.id.calorieAmount)).setText(data.getCalories());
        ((TextView) v.getViewById(R.id.fatAmount)).setText(data.getFat());
        ((TextView) v.getViewById(R.id.saturatedFatAmount)).setText(data.getSaturated());
        ((TextView) v.getViewById(R.id.cholesterolAmount)).setText(data.getCholesterol());
        ((TextView) v.getViewById(R.id.sodiumAmount)).setText(data.getSodium());
        ((TextView) v.getViewById(R.id.carbAmount)).setText(data.getCarbs());
        ((TextView) v.getViewById(R.id.fiberAmount)).setText(data.getFiber());
        ((TextView) v.getViewById(R.id.totalSugarAmount)).setText(data.getSugar());
        ((TextView) v.getViewById(R.id.proteinAmount)).setText(data.getProtein());
        ((TextView) v.getViewById(R.id.vitaminDAmount)).setText(data.getVitaminD());
        ((TextView) v.getViewById(R.id.calciumAmount)).setText(data.getCalcium());
        ((TextView) v.getViewById(R.id.ironAmount)).setText(data.getIron());
        ((TextView) v.getViewById(R.id.potassiumAmount)).setText(data.getPotassium());

    }

    private void addHeader(String headerText)
    {
        TextView title = new TextView(layout.getContext(), null);
        title.setId(TextView.generateViewId());
        title.setText(headerText);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(24);
        title.setGravity(Gravity.BOTTOM | Gravity.LEFT);

        LinearLayout.LayoutParams lParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lParam.gravity = Gravity.LEFT;

        title.setLayoutParams(lParam);

        layout.addView(title);
    }

    private void addField(String fieldName, String amount)
    {
        TextInputLayout text = new TextInputLayout(getContext(), null);
        text.setEnabled(false);
        text.setHintEnabled(true);
        text.setHint(fieldName);
        text.addView(new TextInputEditText(getContext(), null));
        text.getEditText().setText(amount);

        LinearLayout.LayoutParams lParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lParam.gravity = Gravity.RIGHT;

        text.setLayoutParams(lParam);

        layout.addView(text);
    }

    private void addHeaderAndField(String headerText, String fieldText)
    {
        LinearLayout line = new LinearLayout(getContext(), null);
        line.setOrientation(LinearLayout.HORIZONTAL);

        TextView title = new TextView(layout.getContext(), null);
        title.setId(TextView.generateViewId());
        title.setText(headerText);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(24);
        title.setGravity(Gravity.BOTTOM | Gravity.LEFT);

        TextInputLayout text = new TextInputLayout(getContext(), null);
        text.setEnabled(false);
        text.setHintEnabled(true);
        text.setHint("");
        text.addView(new TextInputEditText(getContext(), null));
        text.getEditText().setText(fieldText);

        LinearLayout.LayoutParams lParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lParam.gravity = Gravity.LEFT;
        lParam.setMargins(10, 0, 10, 0);

        line.addView(title, lParam);

        lParam.gravity = Gravity.RIGHT;

        line.addView(text, lParam);

        layout.addView(line);
    }

    @Override
    public void onClick(View v) {
        ingredientView.closeInformationView(this);
    }

    private void initializeGuidelines()
    {
        imageBottomGuideline = Guideline.generateViewId();
        leftSideGuideline = Guideline.generateViewId();
        rightSideGuideline = Guideline.generateViewId();
        buttonBottomGuideline = Guideline.generateViewId();
        buttonTopGuideline = Guideline.generateViewId();

        set.create(imageBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(leftSideGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(rightSideGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(buttonBottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(buttonTopGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelinePercent(buttonTopGuideline, 0.1f);
        set.setGuidelineBegin(buttonBottomGuideline, dpToPx(56, getContext()));
        set.setGuidelineBegin(imageBottomGuideline, dpToPx(280, getContext()));
        set.setGuidelinePercent(leftSideGuideline, 0.05f);
        set.setGuidelinePercent(rightSideGuideline, 0.95f);


    }
}

