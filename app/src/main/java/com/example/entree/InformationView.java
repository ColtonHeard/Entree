package com.example.entree;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InformationView extends EntreeConstraintView
{
    private ScrollView scroll;
    private LinearLayout layout;

    /**
     * Initializes the ConstraintView with a given application context and attribute set.
     * Also generates an id for this view so it is compatible for use in other constraint layouts.
     *
     * @param context The application context in which to initialize the view.
     * @param attrs   The attribute set to initialize the view with.
     */
    public InformationView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        this.setId(generateViewId());
        this.setBackgroundColor(getResources().getColor(R.color.white));

        scroll = new ScrollView(context, attrs);
        scroll.setId(ScrollView.generateViewId());

        this.addView(scroll, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        layout = new LinearLayout(context, attrs);
        layout.setOrientation(LinearLayout.VERTICAL);

        scroll.addView(layout);

        set.clone(this);

        to(scroll, TOP, this, TOP);
        to(scroll, BOTTOM, this, BOTTOM);
        to(scroll, LEFT, this, LEFT);
        to(scroll, RIGHT, this, RIGHT);

        this.setConstraintSet(set);
        set.applyTo(this);

        for (int i = 0; i < 10; i++) {
            addHeader();
            addField("Protein", "12g");
        }
    }

    private void addHeader()
    {
        TextView title = new TextView(layout.getContext(), null);
        title.setId(TextView.generateViewId());
        title.setText("Header");
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(24);
        title.setGravity(Gravity.BOTTOM | Gravity.LEFT);

        layout.addView(title);
    }

    private void addField(String fieldName, String amount)
    {
        TextInputLayout text = new TextInputLayout(getContext(), null, R.style.Widget_App_TextInputLayout);
        text.setEnabled(false);
        text.setHintEnabled(true);
        text.setHint(fieldName);
        text.addView(new EditText(getContext(), null));
        text.getEditText().setText(amount);

        layout.addView(text);
    }


}
