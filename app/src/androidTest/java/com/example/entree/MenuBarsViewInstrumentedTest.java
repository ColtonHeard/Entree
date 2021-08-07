package com.example.entree;

import android.content.Context;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.android.material.appbar.MaterialToolbar;

@RunWith(AndroidJUnit4.class)
public class MenuBarsViewInstrumentedTest
{
    @Test
    public void testChangeView() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.setTheme(R.style.Theme_Entree);
        MenuBarsView v = new MenuBarsView(appContext, null, null);

        View dummy = new View(appContext, null);
        dummy.setId(View.generateViewId());
        v.changeView(dummy);

        assertTrue(dummy.getParent() == v);
    }

    @Test
    public void testChangeToCamera() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.setTheme(R.style.Theme_Entree);
        MenuBarsView v = new MenuBarsView(appContext, null, null);

        v.onNavigationItemSelected(v.getCameraMenuItem());

        assertTrue(v.getSubView() == v.getCameraView());
    }

    @Test
    public void testChangeToIngredients() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.setTheme(R.style.Theme_Entree);
        MenuBarsView v = new MenuBarsView(appContext, null, null);

        v.onNavigationItemSelected(v.getIngredientMenuItem());

        assertTrue(v.getIngredientView().getParent().getParent() == v);
    }

    @Test
    public void testChangeToRecipes() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.setTheme(R.style.Theme_Entree);
        MenuBarsView v = new MenuBarsView(appContext, null, null);

        v.onNavigationItemSelected(v.getRecipeMenuItem());

        assertTrue(v.getSubView() == v.getRecipeView());
    }

    @Test
    public void testTopBarInitialized() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.setTheme(R.style.Theme_Entree);
        MenuBarsView v = new MenuBarsView(appContext, null, null);

        assertTrue(v.getTopBar() != null);
    }

    @Test
    public void testBottomBarInitialized() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.setTheme(R.style.Theme_Entree);
        MenuBarsView v = new MenuBarsView(appContext, null, null);

        assertTrue(v.getBottomView() != null);
    }

}
