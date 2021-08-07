package com.example.entree;

import android.content.Context;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EntreeConstraintViewInstrumentedTest
{
    @Test
    public void useAppContext()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        EntreeConstraintView v = new EntreeConstraintView(appContext, null);

        assertTrue(v.getContext() == appContext);
    }

    @Test
    public void verifyConstraintSet()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        EntreeConstraintView v = new EntreeConstraintView(appContext, null);

        assertTrue(v.set != null);
    }

    @Test
    public void testDpToPx()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.setTheme(R.style.Theme_Entree);
        EntreeConstraintView v = new EntreeConstraintView(appContext, null);

        View dummy = new View(appContext, null);

        assertEquals(35, v.dpToPx(10, appContext));
    }


}
