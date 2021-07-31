package com.example.entree;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.objects.DetectedObject;

import java.io.IOException;
import java.util.List;

public class CameraView extends EntreeConstraintView
{
    MainActivity activity;
    ImageView pic;
    /*
Constants for the margin at which guideline's should be placed from the edge of the screen.
 */
    private final int BOTTOM_GUIDELINE_MARGIN = 56;
    private final int TOP_GUIDELINE_MARGIN = 56;
    private final int BOTTOMBUTTON_GUIDELINE_MARGIN = 25;
    private final int TOPSeventyFive_GUIDELINE_MARGIN = 100;
    private final int MIDDLE_GUIDELINE_MARGIN = 350;

    /*
    Variables for holding the id's of the guideline helper UI objects.
    Should this NutritionView be emptied of all it's components, these must be recreated and reset.
     */
    private int topGuideline;
    private int bottomGuideline;
    private int topSeventyFive;
    private int bottomButton;
    private int middleGuideline;

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs, MainActivity mainActivity)
    {
        super(context, attrs);

        activity = mainActivity;
        activity.setCameraView(this);
        pic = new ImageView(context, attrs);

        pic.setId(ImageView.generateViewId());

        FloatingActionButton fab = getFab(context);

        TextView text = new TextView(context, attrs);
        text.setText("Camera to be implemented");
        text.setTextSize(24);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        text.setId(TextView.generateViewId());

        this.addView(text);
        this.addView(fab);
        this.addView(pic);

        set.clone(this);
        initializeGuidelines();

        to(text, TOP, topGuideline, TOP);
        to(text, LEFT, this, LEFT);
        to(text, BOTTOM, bottomGuideline, TOP);
        to(text, RIGHT, this, RIGHT);

        to(fab, TOP, bottomGuideline, TOP);
        to(fab, LEFT, middleGuideline, LEFT);
        to(fab, BOTTOM, bottomButton, TOP);
        to(fab, RIGHT, this, RIGHT);

        to(pic, TOP, topGuideline, TOP);
        to(pic, LEFT, this, LEFT);
        to(pic, BOTTOM, bottomGuideline, TOP);
        to(pic, RIGHT, this, RIGHT);

        this.setConstraintSet(set);
        set.applyTo(this);

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                activity.openGallery();
                Log.d("Gallery", "Gallery opened");
            }
        });
    }

    public FloatingActionButton getFab(Context context){

        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setId(View.generateViewId());
        fab.setImageResource(R.drawable.add_image);
        return fab;
    }

    public void setImageURI(Uri imageUri) {
       pic.setImageURI(imageUri);
    }

    /*
 Paints the bounding boxes of any food objects that have been recognized by the AI.
 Does so by painting transparent boxes over the imageview's content, then updating the image view.
  */
    private void paintBoundingBoxes(List<DetectedObject> foundObjects, Bitmap imageBitmap)
    {
        //Create a new image bitmap and attach a brand new canvas to it
        Bitmap tempBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.purple_500));
        paint.setAlpha(60);

        //Draw the image bitmap into the canvas
        tempCanvas.drawBitmap(imageBitmap, 0, 0, null);

        for (DetectedObject obj: foundObjects)
        {
            tempCanvas.drawRect(obj.getBoundingBox(), paint);
        }

        //Attach the canvas to the ImageView
        pic.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

    private void initializeGuidelines()
    {
        topGuideline = Guideline.generateViewId();
        bottomGuideline = Guideline.generateViewId();
        topSeventyFive = Guideline.generateViewId();
        bottomButton = Guideline.generateViewId();

        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelineBegin(topGuideline, dpToPx(TOP_GUIDELINE_MARGIN, getContext()));
        set.setGuidelineEnd(bottomGuideline, dpToPx(BOTTOM_GUIDELINE_MARGIN, getContext()));

        set.create(topSeventyFive, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomButton, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelineBegin(topSeventyFive, dpToPx(TOPSeventyFive_GUIDELINE_MARGIN, getContext()));
        set.setGuidelineEnd(bottomButton, dpToPx(BOTTOMBUTTON_GUIDELINE_MARGIN, getContext()));

        set.create(middleGuideline, ConstraintSet.VERTICAL_GUIDELINE);

        set.setGuidelineBegin(middleGuideline, dpToPx(MIDDLE_GUIDELINE_MARGIN, getContext()));
    }
}
