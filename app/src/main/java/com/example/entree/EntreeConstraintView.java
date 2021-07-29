package com.example.entree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class EntreeConstraintView extends ConstraintLayout
{
    /*
    Constants for specifying which side of a view to connect a constraint to.
     */
    protected final int LEFT = ConstraintSet.LEFT;
    protected final int RIGHT = ConstraintSet.RIGHT;
    protected final int TOP = ConstraintSet.TOP;
    protected final int BOTTOM = ConstraintSet.BOTTOM;

    //Object responsible for adding constraints between the subviews in this object.
    protected ConstraintSet set;

    public EntreeConstraintView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        setId(generateViewId());

        set = new ConstraintSet();
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

    protected int dpToPx(int dp, Context context)
    {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
