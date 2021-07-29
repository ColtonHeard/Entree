package com.example.entree;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CameraView extends EntreeConstraintView
{

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        TextView text = new TextView(context, attrs);
        text.setText("Camera");
        this.addView(text);
    }

}
