package com.example.entree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import org.w3c.dom.Text;

/**
 WIP class for displaying an ingredient's nutritional information.
 */
public class NutritionView extends ConstraintLayout
{
    /*
    Constants for the margin at which guideline's should be placed from the edge of the screen.
    Also has constants for specifying which side of a view to connect a constraint to.
     */
    private final int GUIDELINE_MARGIN = 20;

    private final int LEFT = ConstraintSet.LEFT;
    private final int RIGHT = ConstraintSet.RIGHT;
    private final int TOP = ConstraintSet.TOP;
    private final int BOTTOM = ConstraintSet.BOTTOM;

    //Object responsible for adding constraints between the subviews in this object.
    private ConstraintSet set;

    /*
    Variables for holding the id's of the guideline helper UI objects.
    Should this NutritionView be emptied of all it's components, these must be recreated and reset.
     */
    private int leftGuideline;
    private int leftIndentedGuideline;
    private int leftDoubleIndentedGuideline;
    private int rightGuideline;

    /*
    Creates a new NutritionView object and creates the basic layout.
     */
    public NutritionView(Context context, AttributeSet atts)
    {
        super(context, atts);

        set = new ConstraintSet();
        set.clone(this);

        //Keep track of view size, may be null, also for resizing components later

        //Create guidelines for separating visual objects from the sides of the screen
        initializeGuidelines();

        //Begin creating the basic subview objects and constraining them.
        TextView nutritionFactsLabel = new TextView(context);
        setupTextViewForBold(nutritionFactsLabel, "Nutrition Facts", 30, 50);
        to(nutritionFactsLabel, LEFT, this, LEFT);
        to(nutritionFactsLabel, RIGHT, this, RIGHT);
        to(nutritionFactsLabel, TOP, this, TOP);
        set.setHorizontalBias(nutritionFactsLabel.getId(), 0.5f);

        View nutritionFactsDivider = new View(context);
        setupDivider(context, nutritionFactsDivider, nutritionFactsLabel, 2);

        TextView servingPerLabel = new TextView(context);
        setupTextView(servingPerLabel, "0 servings per container", 16, 20);
        to(servingPerLabel, TOP, nutritionFactsDivider, BOTTOM);
        to(servingPerLabel, LEFT, leftGuideline, RIGHT);

        TextView servingSizeLabel = new TextView(context);
        setupTextViewForBold(servingSizeLabel, "Serving size", 20, 25);
        to(servingSizeLabel, TOP, servingPerLabel, BOTTOM);
        to(servingSizeLabel, LEFT, leftGuideline, RIGHT);





        //addView(View arg0, ViewGroup.LayoutParams arg1);
        //invalidate();
        //requestLayout();
        //View v = R.layout.
    }

    /*
    Helper method that makes a constraint connection from view a to view b.
    Connection starts on a's from side and connects to b's to side.
     */
    private void to(View a, int from, View b, int to)
    {
        set.connect(a.getId(), from, b.getId(), to);
    }

    /*
    Overloaded version allowing for a constraint connection to be made to a guideline instead of a view object.
     */
    private void to(View a, int from, int guidelineID, int to)
    {
        set.connect(a.getId(), from, guidelineID, to);
    }

    private void initializeGuidelines()
    {
        leftGuideline = Guideline.generateViewId();
        leftIndentedGuideline = Guideline.generateViewId();
        leftDoubleIndentedGuideline = Guideline.generateViewId();
        rightGuideline = Guideline.generateViewId();

        set.create(leftGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(leftIndentedGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(leftDoubleIndentedGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(rightGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelineBegin(leftGuideline, GUIDELINE_MARGIN);
        set.setGuidelineBegin(leftIndentedGuideline, GUIDELINE_MARGIN * 2);
        set.setGuidelineBegin(leftDoubleIndentedGuideline, GUIDELINE_MARGIN * 3);
        set.setGuidelineEnd(rightGuideline, GUIDELINE_MARGIN);
    }

    private void setupDivider(Context context, View view, View topConnectingView, int dividerHeight)
    {
        view.setBackground(new ColorDrawable(context.getResources().getColor(R.color.black)));
        view.setMinimumHeight(dividerHeight);
        to(view, LEFT, leftGuideline, RIGHT);
        to(view, RIGHT, rightGuideline, LEFT);
        to(view, TOP, topConnectingView, BOTTOM);
    }

    private void setupTextViewForBold(TextView view, String text, int textSize, int viewHeight)
    {
        setupTextView(view, text, textSize, viewHeight);
        view.setTypeface(view.getTypeface(), Typeface.BOLD);
    }

    private void setupTextView(TextView view, String text, int textSize, int viewHeight)
    {
        view.setId(TextView.generateViewId());
        view.setHeight(viewHeight);
        view.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        view.setText(text);
        view.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    Boolean dataHasNotBeenRead = true;

//    public void searchClick(View view)
//    {
//        if (dataHasNotBeenRead)
//        {
//            readFoodData();
//            dataHasNotBeenRead = false;
//        }
//        FoodData food = null;
//
//        if ((food = foodMap.get(IngredientSearch.getText().toString().toLowerCase())) != null)
//        {
//            setupLabel(food);
//        }
//        else
//        {
//            setupLabel(new FoodData());
//        }
//    }
//
//    private void setupLabel(FoodData food){
//        calorieAmount.setText(food.getCalories());
//        fatAmount.setText(food.getFat());
//        saturatedFatAmount.setText(food.getSaturated());
//        cholesterolAmount.setText(food.getCholesterol());
//        sodiumAmount.setText(food.getSodium());
//        carbAmount.setText(food.getCarbs());
//        fiberAmount.setText(food.getFiber());
//        totalSugarAmount.setText(food.getSugar());
//        proteinAmount.setText(food.getProtein());
//        vitaminDAmount.setText(food.getVitaminD());
//        calciumAmount.setText(food.getCalcium());
//        ironAmount.setText(food.getIron());
//        potassiumAmount.setText(food.getPotassium());
//
//        if(food.getName().equals("None")){
//            nutritionFactsLabel.setText("Nutrition Facts");
//        } else {
//            nutritionFactsLabel.setText("Nutrition Facts: " + food.getName());
//        }
//    }

}
