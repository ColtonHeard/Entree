package com.example.entree;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.compose.ui.platform.ComposeView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

/*
Represents the application's main view elements including the top action bar and bottom navigation bar.
All other application subviews are placed between them in the remaining screen space.
 */
public class MenuBarsView extends EntreeConstraintView implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{

    /** Constants for the margin at which the bottom guideline should be placed from the bottom of the screen. */
    private final int BOTTOM_GUIDELINE_MARGIN = 56;

    /** Constants for the margin at which the top guideline should be placed from the top of the screen. */
    private final int TOP_GUIDELINE_MARGIN = 56;

    /*
    Variables for holding the id's of the guideline helper UI objects.
    Should this NutritionView be emptied of all it's components, these must be recreated and reset.
     */
    private int topGuideline;
    private int bottomGuideline;

    /** View object that represents the current subView. */
    private View subView;

    /*
     View objects for the project's custom views. These will be swapped in and out of the subView.
     */
    private final CameraView cameraView;
    private final ScrollView ingredientScroll;
    private final IngredientView ingredientView;
    private final RecipeView recipeView;

    /*
     MenuItem objects for each of the application's views. Are placed in the bottom navigation bar.
     */
    private final MenuItem cameraMenuItem, ingredientMenuItem, recipeMenuItem;

    /*
     MaterialButton objects representing action buttons that are swapped in and out of the top action bar based on the active view.
     */
    MaterialButton addIngredient, editIngredients, ingredientMore;
    MaterialButton cameraMore;

    /*
    The Material Design toolbar that represents this view's top action bar.
     */
    private final MaterialToolbar topBar;
    private final BottomNavigationView bottomView;
    private final AppCompatActivity activity;

    /**
     Creates and lays out the application menu bars and sets the CameraView as the currently open subview.

     @param context The application context to create this RecipeCard in.
     @param attrs The attribute set to initialize this view with.
     @param mainActivity The application's MainActivity.
     */
    public MenuBarsView(@NonNull Context context, @Nullable AttributeSet attrs, MainActivity mainActivity)
    {
        super(context, attrs);
        activity = mainActivity;
        
        //Keep track of view size, may be null, also for resizing components later

        cameraView = new CameraView(context, attrs, mainActivity);
        ingredientView = new IngredientView(context, attrs);
        ingredientScroll = new ScrollView(context, attrs);
        ingredientScroll.setId(ScrollView.generateViewId());
        ingredientScroll.addView(ingredientView);

        recipeView = new RecipeView(context, attrs, mainActivity);

        //Create and setup the top action bar
        topBar = new MaterialToolbar(context, attrs);
        topBar.setId(MaterialToolbar.generateViewId());
        topBar.setBackgroundColor(getResources().getColor(R.color.entree_orange));
        topBar.setOverflowIcon(AppCompatResources.getDrawable(context, R.drawable.more_icon));
        topBar.showOverflowMenu();

        topBar.setTitle("THIS SHOULD NOT APPEAR");
        topBar.setTitleTextColor(getResources().getColor(R.color.white));

        this.addView(topBar, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));

        /*
        Create action bar buttons for the Ingredient List screen.
         */
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;

        cameraMore = new MaterialButton(context, attrs, R.attr.iconButtonType);
        cameraMore.setIcon(AppCompatResources.getDrawable(context, R.drawable.more_icon));
        cameraMore.setLayoutParams(layoutParams);
        cameraMore.setGravity(Gravity.END);
        cameraMore.setOnClickListener(this);

        ingredientMore = new MaterialButton(context, attrs, R.attr.iconButtonType);
        ingredientMore.setIcon(AppCompatResources.getDrawable(context, R.drawable.more_icon));
        ingredientMore.setLayoutParams(layoutParams);
        ingredientMore.setGravity(Gravity.END);
        ingredientMore.setOnClickListener(this);

        editIngredients = new MaterialButton(context, attrs, R.attr.iconButtonType);
        editIngredients.setIcon(AppCompatResources.getDrawable(context, R.drawable.edit_icon));
        editIngredients.setLayoutParams(layoutParams);
        editIngredients.setGravity(Gravity.END);
        editIngredients.setOnClickListener(this);

        addIngredient = new MaterialButton(context, attrs, R.attr.iconButtonType);
        addIngredient.setIcon(AppCompatResources.getDrawable(context, R.drawable.add_icon));
        addIngredient.setLayoutParams(layoutParams);
        addIngredient.setGravity(Gravity.END);
        addIngredient.setOnClickListener(this);

        //Create and setup the bottom navigation bar
        bottomView = new BottomNavigationView(context);
        bottomView.setId(BottomNavigationView.generateViewId());
        bottomView.setBackgroundColor(getResources().getColor(R.color.entree_orange));
        bottomView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

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

    /**
     Changes the inner subview to the passed View. The passed view must have a view id assigned to it.

     @param v The view to change the subview to.
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

    /**
     Initializes and positions the guidelines so they can be used to layout components.
     */
    private void initializeGuidelines()
    {
        topGuideline = Guideline.generateViewId();
        bottomGuideline = Guideline.generateViewId();

        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelineBegin(topGuideline, dpToPx(TOP_GUIDELINE_MARGIN, getContext()));
        set.setGuidelineEnd(bottomGuideline, dpToPx(BOTTOM_GUIDELINE_MARGIN, getContext()));
    }

    /**
     Method that handles changing the subview. Performs any cleanup work needed such as ensuring colors are correct for the view.

     @param v The view to switch the subview to.
     */
    private void switchView(View v)
    {
        setColors(getResources().getColor(R.color.entree_orange));
        changeView(v);
    }

    /**
     Changes the colors of the navigation bars and the phone's status bar to the passed color.

     @param color A @ColorInt representing the color to change to.
     */
    private void setColors(@ColorInt int color)
    {
        topBar.setBackgroundColor(color);
        bottomView.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     UI handler method for when a navigation item in the bottom navigation bar is selected.
     Takes the passed MenuItem and changes the active subview to that MenuItems associated view.

     @param item The MenuItem that was tapped by the user.
     @returns A boolean showing whether or not a recognized MenuItem was clicked.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item == cameraMenuItem)
        {
            topBar.setTitle("Camera");
            switchView(cameraView);
            removeAllActionButtons();
            topBar.addView(cameraMore);
            return true;
        }
        else if (item == ingredientMenuItem)
        {
            topBar.setTitle("Ingredient List");
            switchView(ingredientScroll);
            removeAllActionButtons();
            topBar.addView(ingredientMore);
            topBar.addView(editIngredients);
            topBar.addView(addIngredient);

            if (ingredientView.isEditing())
            {
                setColors(getResources().getColor(R.color.entree_red));
            }

            return true;
        }
        else if (item == recipeMenuItem)
        {
            topBar.setTitle("Recipes");
            switchView(recipeView);
            removeAllActionButtons();
            return true;
        }

        return false;
    }

    /**
     Removes all action buttons from the top action bar.
     */
    private void removeAllActionButtons()
    {
        topBar.removeView(addIngredient);
        topBar.removeView(ingredientMore);
        topBar.removeView(cameraMore);
        topBar.removeView(editIngredients);
    }

    /**
     UI handler method responsible for processing clicks on the action bar buttons.

     @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        if (v == addIngredient)
        {
            ingredientView.addCard();
            ingredientScroll.computeScroll();
            ingredientScroll.fullScroll(ScrollView.FOCUS_DOWN);
        }
        else if (v == ingredientMore)
        {
            // INGREDIENT LIST ADDITIONAL OPTIONS
        }
        else if (v == editIngredients)
        {
            ingredientView.enableEditing();
            if (ingredientView.isEditing())
            {
                setColors(getResources().getColor(R.color.entree_red));
            }
            else
            {
                setColors(getResources().getColor(R.color.entree_orange));
            }
        }
        else if (v == cameraMore)
        {
            // CAMREA ADDITOINAL OPTIONS
        }
    }
}
