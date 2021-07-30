package com.example.entree;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.compose.ui.platform.ComposeView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MenuBarsView extends EntreeConstraintView implements NavigationView.OnNavigationItemSelectedListener
{

    /*
    Constants for the margin at which guideline's should be placed from the edge of the screen.
     */
    private final int BOTTOM_GUIDELINE_MARGIN = 56;
    private final int TOP_GUIDELINE_MARGIN = 56;

    /*
    Variables for holding the id's of the guideline helper UI objects.
    Should this NutritionView be emptied of all it's components, these must be recreated and reset.
     */
    private int topGuideline;
    private int bottomGuideline;

    /*
    View object that represents the current subView.
     */
    private View subView;

    /*
    View objects for the project's custom views. These will be swapped in and out of the subView.
     */
    private CameraView cameraView;
    private IngredientView ingredientView;
    private RecipeView recipeView;

    private MenuItem cameraMenuItem, ingredientMenuItem, recipeMenuItem;

    /*
    The Material Design toolbar that represents this view's top action bar.
     */
    private MaterialToolbar topBar;

    public MenuBarsView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        //Keep track of view size, may be null, also for resizing components later

        cameraView = new CameraView(context, attrs);
        ingredientView = new IngredientView(context, attrs);
        recipeView = new RecipeView(context, attrs);

        //Create and setup the top action bar
        topBar = new MaterialToolbar(context, attrs);
        topBar.setId(MaterialToolbar.generateViewId());
        topBar.setBackgroundColor(getResources().getColor(R.color.entree_orange));

        topBar.setTitle("THIS SHOULD NOT APPEAR");
        topBar.setTitleTextColor(getResources().getColor(R.color.white));

        this.addView(topBar, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));

        //Create and setup the bottom navigation bar
        BottomNavigationView bottomView = new BottomNavigationView(context);
        bottomView.setId(BottomNavigationView.generateViewId());
        bottomView.setBackgroundColor(getResources().getColor(R.color.entree_orange));
        bottomView.setOnNavigationItemSelectedListener(item -> {
            return onNavigationItemSelected(item);
        });

        Menu bottomMenu = bottomView.getMenu();
        cameraMenuItem = bottomMenu.add(R.string.camera_icon_name);
        ingredientMenuItem = bottomMenu.add(R.string.ingredients_icon_name);
        recipeMenuItem = bottomMenu.add(R.string.recipes_icon_name);

        cameraMenuItem.setIcon(R.drawable.camera_icon);
        ingredientMenuItem.setIcon(R.drawable.list_icon);
        recipeMenuItem.setIcon(R.drawable.recipe_icon);

        this.addView(bottomView);

        set.clone(this);

        //Create guidelines for separating visual objects from the sides of the screen
        initializeGuidelines();

        to(topBar, TOP, this, TOP);
        to(topBar, LEFT, this, LEFT);
        to(topBar, BOTTOM, topGuideline, TOP);
        to(topBar, RIGHT, this, RIGHT);

        to(bottomView, TOP, bottomGuideline, BOTTOM);
        to(bottomView, LEFT, this, LEFT);
        to(bottomView, BOTTOM, this, BOTTOM);
        to(bottomView, RIGHT, this, RIGHT);

        this.setConstraintSet(set);
        set.applyTo(this);

        onNavigationItemSelected(cameraMenuItem);
    }

    /*
    Changes the inner subview to the passed View. The passed view must have had a viewId assigned to it.
     */
    public void changeView(View v)
    {
        if (subView != null)
        {
            this.removeView(subView);
        }
        subView = v;
        this.addView(v, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));

        set.clone(this);

        to(v, TOP, topGuideline, BOTTOM);
        to(v, BOTTOM, bottomGuideline, TOP);
        to(v, LEFT, this, LEFT);
        to(v, RIGHT, this, RIGHT);

        this.setConstraintSet(set);
        set.applyTo(this);
    }

    private void initializeGuidelines()
    {
        topGuideline = Guideline.generateViewId();
        bottomGuideline = Guideline.generateViewId();

        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelineBegin(topGuideline, dpToPx(TOP_GUIDELINE_MARGIN, getContext()));
        set.setGuidelineEnd(bottomGuideline, dpToPx(BOTTOM_GUIDELINE_MARGIN, getContext()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item == cameraMenuItem)
        {
            topBar.setTitle("Camera");
            changeView(cameraView);
            return true;
        }
        else if (item == ingredientMenuItem)
        {
            topBar.setTitle("Ingredient List");
            changeView(ingredientView);
            return true;
        }
        else if (item == recipeMenuItem)
        {
            topBar.setTitle("Recipes");
            changeView(recipeView);
            return true;
        }

        return false;
    }
}
