package com.example.entree;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 Represents a list-type view containing all of the detected or user-input ingredients in Material Design cards.
 Has functionality for editing and adjusting the contained list.
 */
public class IngredientView extends EntreeConstraintView
{

    private final int CARD_HEIGHT = 600;
    private final int CARD_WIDTH = 344;
    private final int TOP_MARGIN = 60;
    private final int SIDE_MARGIN = 20;
    private final int MIDDLE_MARGIN = 10;
    private final int CARD_MARGIN = 40;

    private int topGuideline, bottomHelperGuideline;
    private int firstLeftGuideline, firstRightGuideline;
    private int secondLeftGuideline, secondRightGuideline;
    private int leftMiddleGuideline, rightMiddleGuideline;

    /** ArrayList of all IngredientCards currently added to this view. */
    private ArrayList<IngredientCard> cards;

    /** THe number of IngredientCards currently contained in this view. */
    private int count;

    /** Boolean depicting whether or not the view is in edit mode. */
    private boolean editing;

    /** The information view for a specific ingredient or list of ingredients that is currently being displayed. */
    private View informationView;

    private HashMap<String, FoodData> foodMap;

    /**
     Initializes the view and adds 5 dummy cards.

     @param context The application context to create this RecipeCard in.
     @param attrs The attribute set to initialize this view with.
     */
    public IngredientView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

//        this.setBackgroundColor(getResources().getColor(R.color.entree_red));

        count = 0;
        editing = false;
        cards = new ArrayList<>();
        foodMap = new HashMap<>();

        //Setup Constraints
        set.clone(this);

//        ImageView box = new ImageView(context, attrs);
//        box.setId(View.generateViewId());
//        box.setImageDrawable(getResources().getDrawable(R.drawable.food_icon));

//        this.addView(box);

        initializeGuidelines();

//        to(box, TOP, bottomHelperGuideline, BOTTOM);
//        to(box, BOTTOM, this, BOTTOM);
//        to(box, LEFT, this, LEFT);
//        to(box, RIGHT, this, RIGHT);

        this.setConstraintSet(set);
        set.applyTo(this);

        readFoodData();

        for (String key: foodMap.keySet())
        {
            addCard(new IngredientCard(context, attrs, foodMap.get(key)));
        }
    }

    /**
     Adds a new IngredientCard with a random description to this view and lays it out accordingly.
     */
    public void addCard()
    {
        IngredientCard card = new IngredientCard(getContext(), null, new FoodData());
        if (editing) {
            card.disableChecking();
        }

        cards.add(card);
        this.addView(card, new ConstraintLayout.LayoutParams(0, 0));

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
            to(card, RIGHT, leftMiddleGuideline, LEFT);
        }
        else
        {
            to(card, TOP, topLine, BOTTOM);
            to(card, LEFT, rightMiddleGuideline, RIGHT);
            to(card, BOTTOM, bottomLine, TOP);
            to(card, RIGHT, secondRightGuideline, LEFT);
        }

        count++;

        this.setConstraintSet(set);
        set.applyTo(this);
    }

    /**
     Adds the passed IngredientCard to this view and lays it out accordingly.

     @param card The IngredientCard to add to this view.
     */
    private void addCard(IngredientCard card)
    {
        this.addView(card, new ConstraintLayout.LayoutParams(0, 0));
        if (!cards.contains(card))
        {
            cards.add(card);
        }

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
            to(card, RIGHT, leftMiddleGuideline, LEFT);
        }
        else
        {
            to(card, TOP, topLine, BOTTOM);
            to(card, LEFT, rightMiddleGuideline, RIGHT);
            to(card, BOTTOM, bottomLine, TOP);
            to(card, RIGHT, secondRightGuideline, LEFT);
        }

        count++;

        this.setConstraintSet(set);
        set.applyTo(this);
    }

    private void readFoodData()
    {
        // Read the raw csv file
        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getResources().openRawResource(R.raw.food), Charset.forName("UTF-8"))
        );


        String line = "";

        try
        {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null)
            {
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");

                // Read the data
                FoodData data = new FoodData();
                data.setArray(tokens);

                // Add the read food to the FoodData HashMap
                foodMap.put(data.getName().toLowerCase(), data);
            }

            reader.close();
        }
        catch (IOException e)
        {
            // Logs error with priority level
            Log.wtf("MyActivityError", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }

    /**
     Rebinds the constraints of all cards. Called after cards are removed via edit mode.
     */
    private void relayoutCards()
    {
        count = 0;
        for (IngredientCard card: cards)
        {
            set.clear(card.getId());
            card.deleteCard();
            addCard(card);
        }
    }

    /**
     Toggles edit mode, either enabling it and checking functionality on all contained IngredientCards or removing any checked IngredientCards.
     */
    public void enableEditing()
    {
        editing = !editing;
        if (!editing)
        {
            removeSelectedCards();
            relayoutCards();
        }

        toggleEditing(editing);
    }

    /**
     Returns a boolean depicting whether or not the view is currently in edit mode.

     @return Boolean showing if the view is currently in edit mode.
     */
    public boolean isEditing()
    {
        return editing;
    }

    /**
     Changes checking functionality on all of this view's IngredientCards.

     @param val Whether or not checking functionality is enabled.
     */
    private void toggleEditing(boolean val)
    {
        for (IngredientCard card: cards)
        {
            if (val)
            {
                card.enableChecking();
            }
            else
            {
                card.disableChecking();
            }
        }
    }

    /**
     Removes all of the currently checked/selected cards from the IngredientView.
     */
    private void removeSelectedCards()
    {
        ArrayList<IngredientCard> toRemove = new ArrayList<>();
        for (IngredientCard card: cards)
        {
            if (card.isChecked())
            {
                set.clear(card.getId());
                card.deleteCard();
                count--;
                this.setConstraintSet(set);
                toRemove.add(card);
            }
        }
        cards.removeAll(toRemove);
        requestLayout();
    }

    public void openInformationView(View v)
    {
        ((MenuBarsView) this.getParent().getParent()).hideIngredientActionButtons();
        informationView = v;
        ScrollView parent = (ScrollView) this.getParent();
        parent.removeView(this);
        parent.addView(informationView);parent.scrollTo(0, 0);
    }

    public void closeInformationView(View v)
    {
        ScrollView parent = (ScrollView) v.getParent();
        parent.removeView(informationView);
        parent.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        ((MenuBarsView) this.getParent().getParent()).showIngredientActionButtons();
    }

    /**
     Initializes and positions the guidelines so they can be used to layout components.
     */
    private void initializeGuidelines()
    {
        topGuideline = Guideline.generateViewId();
        firstLeftGuideline = Guideline.generateViewId();
        firstRightGuideline = Guideline.generateViewId();
        secondLeftGuideline = Guideline.generateViewId();
        secondRightGuideline = Guideline.generateViewId();
        leftMiddleGuideline = Guideline.generateViewId();
        rightMiddleGuideline = Guideline.generateViewId();
        bottomHelperGuideline = Guideline.generateViewId();

        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(firstLeftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(firstRightGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(secondLeftGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(secondRightGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(leftMiddleGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(rightMiddleGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(bottomHelperGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelineBegin(topGuideline, dpToPx(TOP_MARGIN, getContext()));
        set.setGuidelineBegin(firstLeftGuideline, dpToPx(SIDE_MARGIN, getContext()));
        set.setGuidelineBegin(firstRightGuideline, dpToPx(SIDE_MARGIN + CARD_WIDTH, getContext()));
        set.setGuidelineEnd(secondLeftGuideline, dpToPx(SIDE_MARGIN + CARD_WIDTH, getContext()));
        set.setGuidelineEnd(secondRightGuideline, dpToPx(SIDE_MARGIN, getContext()));
        set.setGuidelinePercent(leftMiddleGuideline, 0.48f);
        set.setGuidelinePercent(rightMiddleGuideline, 0.52f);
        set.setGuidelinePercent(bottomHelperGuideline, 0.99f);
    }

}
