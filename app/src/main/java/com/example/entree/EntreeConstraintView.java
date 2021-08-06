package com.example.entree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

/**
 Class responsible for supplying helper methods in creating and laying out constraint views.
 Project view classes should generally subclass here and make use of the functionality.
 */
public class EntreeConstraintView extends ConstraintLayout
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

    /**
     Initializes the ConstraintView with a given application context and attribute set.
     Also generates an id for this view so it is compatible for use in other constraint layouts.

     @param context The application context in which to initialize the view.
     @param attrs The attribute set to initialize the view with.
     */
    public EntreeConstraintView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        setId(generateViewId());

        set = new ConstraintSet();
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
     Helper method for converting from density-independent pixels to screen space pixels.
     All of Material Design's layouts are measured in dp, use this method when working with UI components requiring a specific size.

     @param dp The size in density-independent pixels to convert.
     @param context The application context the view is running in.
     */
    protected int dpToPx(int dp, Context context)
    {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
