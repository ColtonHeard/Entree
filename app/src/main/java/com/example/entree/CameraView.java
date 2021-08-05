package com.example.entree;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.objects.DetectedObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.net.URI;

public class CameraView extends EntreeConstraintView implements View.OnLongClickListener
{

    /*
Constants for the margin at which guideline's should be placed from the edge of the screen.
 */
    private final int BOTTOM_GUIDELINE_MARGIN = 56;
    private final int TOP_GUIDELINE_MARGIN = 56;
    private final int BOTTOMBUTTON_GUIDELINE_MARGIN = 25;
    private final int TOPSeventyFive_GUIDELINE_MARGIN = 100;
    private final int MIDDLE_GUIDELINE_MARGIN = 340;

    /*
    Variables for holding the id's of the guideline helper UI objects.
    Should this NutritionView be emptied of all it's components, these must be recreated and reset.
     */
    private int topGuideline;
    private int bottomGuideline;
    private int topSeventyFive;
    private int bottomButton;
    private int middleGuideline;

    private FoodObjectRecognizer recognizer;
    private CameraManager cameraManager;
    private SurfaceView cameraSurface;

    private MainActivity activity;
    private ImageView pic;

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs, MainActivity mainActivity)
    {
        super(context, attrs);

        this.setOnLongClickListener(this);

        recognizer = new FoodObjectRecognizer(this, mainActivity);
        activity = mainActivity;
        activity.setCameraView(this);

        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        cameraSurface = new SurfaceView(context, attrs);
        cameraSurface.setId(SurfaceView.generateViewId());

//        try {
//            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//            {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                Log.d("InvalidPermissionsForCamera", "Unable to open camera due to lack of permissions.");
//            }
//            cameraManager.openCamera(cameraManager.getCameraIdList()[0], this, null);
//        }
//        catch (CameraAccessException e)
//        {
//            e.printStackTrace();
//        }

        pic = new ImageView(context, attrs);
        pic.setId(ImageView.generateViewId());
        pic.setScaleType(ImageView.ScaleType.FIT_CENTER);

        FloatingActionButton fab = getFab(context);

        TextView text = new TextView(context, attrs);
        text.setText("Camera to be implemented");
        text.setTextSize(24);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        text.setId(TextView.generateViewId());

//        this.addView(text);
        this.addView(cameraSurface, new ConstraintLayout.LayoutParams(0, 0));
        this.addView(pic, new ConstraintLayout.LayoutParams(0, 0));
        this.addView(fab);

        set.clone(this);
        initializeGuidelines();

//        to(text, TOP, topGuideline, TOP);
//        to(text, LEFT, this, LEFT);
//        to(text, BOTTOM, bottomGuideline, TOP);
//        to(text, RIGHT, this, RIGHT);

        to(cameraSurface, TOP, topGuideline, TOP);
        to(cameraSurface, LEFT, this, LEFT);
        to(cameraSurface, BOTTOM, bottomGuideline, TOP);
        to(cameraSurface, RIGHT, this, RIGHT);

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

    public void setImageURI(Uri imageUri)
    {
       pic.setImageURI(imageUri);
        try {
            Bitmap image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
            recognizer.processImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean onLongClick(View v)
    {
        paintBoundingBoxes(recognizer.getFoundObjects(), ((BitmapDrawable) pic.getDrawable()).getBitmap());
        return true;
    }

    public class CameraCallbackHelper extends CameraDevice.StateCallback
    {
        private CameraDevice camera;
        private CameraView cameraView;
        private CaptureCallbackHelper helper;

        public CameraCallbackHelper(CameraView view)
        {
            super();
            cameraView = view;
            helper = new CaptureCallbackHelper();
        }

        public CameraDevice getCamera()
        {
            return camera;
        }

        @Override
        public void onOpened(@NonNull CameraDevice camera)
        {
            this.camera = camera;

            List<OutputConfiguration> outConfigs = new ArrayList<>();
            OutputConfiguration outConfig = new OutputConfiguration(cameraSurface.getHolder().getSurface());
            outConfigs.add(outConfig);

            SessionConfiguration sessionConfig = new SessionConfiguration(SessionConfiguration.SESSION_REGULAR, outConfigs, null, helper);

            try
            {
                this.camera.createCaptureSession(sessionConfig);
            }
            catch (CameraAccessException e)
            {
                e.printStackTrace();
                Log.d("CameraAccessException-OnOpened", "Error creating the capture session.");
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera)
        {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error)
        {

        }
    }

    public class CaptureCallbackHelper extends CameraCaptureSession.StateCallback
    {


        public CaptureCallbackHelper()
        {
            super();
        }

        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {

        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    }

}
