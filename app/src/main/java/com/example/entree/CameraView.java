package com.example.entree;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.objects.DetectedObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * View for interacting with the Camera and feeding images to the AI.
 */
public class CameraView extends EntreeConstraintView implements View.OnLongClickListener, TextureView.SurfaceTextureListener
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
    private int topButtonGuideline;
    private int resumeButtonGuideline;

    private FoodObjectRecognizer recognizer;
    private CameraManager cameraManager;
    private TextureView cameraSurface;
    private SurfaceTexture texture;

    private MainActivity activity;
    private ImageView pic;
    private FloatingActionButton resumeButton;

    private boolean imageReadyForBounding;
    private boolean readyForCapture;

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs, MainActivity mainActivity)
    {
        super(context, attrs);

        this.setOnLongClickListener(this);

        recognizer = new FoodObjectRecognizer(this, mainActivity);

        activity = mainActivity;
        if (activity != null)
        {
            activity.setCameraView(this);

            cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            cameraSurface = new TextureView(context, attrs);
            cameraSurface.setId(SurfaceView.generateViewId());
            cameraSurface.setSurfaceTextureListener(this);
            cameraSurface.setOpaque(false);
        }

        pic = new ImageView(context, attrs);
        pic.setId(ImageView.generateViewId());
        pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        pic.setVisibility(ImageView.INVISIBLE);

        FloatingActionButton fab = getFab(context);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openGallery();
                Log.d("Gallery", "Gallery opened");
            }
        });

        FloatingActionButton cameraButton = getFab(context);
        cameraButton.setImageResource(R.drawable.camera_icon);
        cameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        resumeButton = getFab(context);
        resumeButton.setImageResource(R.drawable.close_icon);
        resumeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeCamera();
            }
        });
        resumeButton.setVisibility(INVISIBLE);

        TextView text = new TextView(context, attrs);
        text.setText("Camera to be implemented");
        text.setTextSize(24);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        text.setId(TextView.generateViewId());

//        this.addView(text);
        if (activity != null)
        {
            this.addView(pic, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.addView(cameraSurface, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.addView(fab, new ConstraintLayout.LayoutParams(dpToPx(56, getContext()), dpToPx(56, getContext())));
            this.addView(cameraButton, new ConstraintLayout.LayoutParams(dpToPx(56, getContext()), dpToPx(56, getContext())));
            this.addView(resumeButton, new ConstraintLayout.LayoutParams(dpToPx(56, getContext()), dpToPx(56, getContext())));
//            cameraSurface.setAlpha(0.0f);
            ViewCompat.setTranslationZ(cameraSurface, 0f);
            ViewCompat.setTranslationZ(fab, -20f);
            fab.bringToFront();
        }

        set.clone(this);
        initializeGuidelines();

        if (activity != null) {
            to(cameraSurface, TOP, this, TOP);
            to(cameraSurface, LEFT, this, LEFT);
            to(cameraSurface, BOTTOM, this, BOTTOM);
            to(cameraSurface, RIGHT, this, RIGHT);
        }

        to(cameraButton, TOP, bottomGuideline, TOP);
        to(cameraButton, LEFT, middleGuideline, LEFT);
        to(cameraButton, BOTTOM, bottomButton, TOP);
        to(cameraButton, RIGHT, this, RIGHT);

        to(fab, TOP, topButtonGuideline, BOTTOM);
        to(fab, LEFT, middleGuideline, LEFT);
        to(fab, BOTTOM, bottomGuideline, TOP);
        to(fab, RIGHT, this, RIGHT);

        to(resumeButton, TOP, resumeButtonGuideline, BOTTOM);
        to(resumeButton, LEFT, middleGuideline, LEFT);
        to(resumeButton, BOTTOM, topButtonGuideline, TOP);
        to(resumeButton, RIGHT, this, RIGHT);

//        to(pic, TOP, topGuideline, TOP);
//        to(pic, LEFT, this, LEFT);
//        to(pic, BOTTOM, bottomGuideline, TOP);
//        to(pic, RIGHT, this, RIGHT);

        this.setConstraintSet(set);
        set.applyTo(this);

        requestLayout();
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
            pic.setVisibility(VISIBLE);
            resumeButton.setVisibility(VISIBLE);
            cameraSurface.setVisibility(SurfaceView.INVISIBLE);
            recognizer.processImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImageReadyForBounding(boolean val)
    {
        imageReadyForBounding = true;
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

        //Draw the image bitmap into the canvas
        tempCanvas.drawBitmap(imageBitmap, 0, 0, null);

        for (DetectedObject obj: foundObjects)
        {
            paint.setColor(getResources().getColor(R.color.purple_500));
            paint.setAlpha(60);
            Rect box = obj.getBoundingBox();
            tempCanvas.drawRect(box, paint);

            if (obj.getLabels().size() > 0) {
                paint.setColor(getResources().getColor(R.color.white));
                paint.setAlpha(100);

                paint.setTextSize(48);

                tempCanvas.drawText(obj.getLabels().get(0).getText(), box.centerX() - (box.width() / 4), box.centerY(), paint);
            }
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
        middleGuideline = Guideline.generateViewId();
        topButtonGuideline = Guideline.generateViewId();
        resumeButtonGuideline = Guideline.generateViewId();

        set.create(topGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(middleGuideline, ConstraintSet.VERTICAL_GUIDELINE);
        set.create(topSeventyFive, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(bottomButton, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(topButtonGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);
        set.create(resumeButtonGuideline, ConstraintSet.HORIZONTAL_GUIDELINE);

        set.setGuidelinePercent(topGuideline, 0.01f);
        set.setGuidelinePercent(bottomGuideline, 0.90f);
        set.setGuidelinePercent(topButtonGuideline, 0.76f);
        set.setGuidelinePercent(resumeButtonGuideline, 0.695f);
        set.setGuidelineBegin(topSeventyFive, dpToPx(TOPSeventyFive_GUIDELINE_MARGIN, getContext()));
        set.setGuidelineEnd(bottomButton, dpToPx(BOTTOMBUTTON_GUIDELINE_MARGIN, getContext()));


        set.setGuidelineBegin(middleGuideline, dpToPx(MIDDLE_GUIDELINE_MARGIN, getContext()));
    }

    private void takePhoto()
    {
        pic.setImageDrawable(new BitmapDrawable(cameraSurface.getBitmap()));

        pic.setVisibility(VISIBLE);
        resumeButton.setVisibility(VISIBLE);
        cameraSurface.setVisibility(SurfaceView.INVISIBLE);

        recognizer.processImage(((BitmapDrawable) pic.getDrawable()).getBitmap());
    }

    private void resumeCamera()
    {
        cameraSurface.setVisibility(SurfaceView.VISIBLE);
        resumeButton.setVisibility(INVISIBLE);
        pic.setVisibility(INVISIBLE);
    }


    @Override
    public boolean onLongClick(View v)
    {
        if (imageReadyForBounding) {
            imageReadyForBounding = false;
            paintBoundingBoxes(recognizer.getFoundObjects(), ((BitmapDrawable) pic.getDrawable()).getBitmap());
        }

        return true;
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height)
    {
        texture = surface;

        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.CAMERA }, 1);

                Log.d("InvalidPermissionsForCamera", "Unable to open camera due to lack of permissions.");
            }
            cameraManager.openCamera(cameraManager.getCameraIdList()[0], new CameraCallbackHelper(), null);
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    /**
     * Inner helper class for handling Callback events from the CameraDevice.
     */
    public class CameraCallbackHelper extends CameraDevice.StateCallback
    {
        private CameraDevice camera;
        private CaptureSessionStateCallbackHelper helper;

        public CameraCallbackHelper()
        {
            super();
            helper = new CaptureSessionStateCallbackHelper(this);
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
            OutputConfiguration outConfig = new OutputConfiguration(new Surface(texture));
            outConfigs.add(outConfig);

//            Executor sessionExecutor = new Executor() {
//
//                @Override
//                public void execute(Runnable command)
//                {
//                    helper.
//                }
//            };
            SessionConfiguration sessionConfig = new SessionConfiguration(SessionConfiguration.SESSION_REGULAR, outConfigs, activity.getMainExecutor(), helper);

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

    /**
     * Inner helper class for handling Callback events from the CameraCaptureSession.
     * Initializes the Capture session and handles the successful return of the session as well as any failed returns.
     */
    public class CaptureSessionStateCallbackHelper extends CameraCaptureSession.StateCallback
    {
        CaptureRequest request;
        CameraCallbackHelper cameraHelper;
        CameraCaptureSession session;

        public CaptureSessionStateCallbackHelper(CameraCallbackHelper cameraHelper)
        {
            super();

            this.cameraHelper = cameraHelper;
        }

        @Override
        public void onConfigured(@NonNull CameraCaptureSession session)
        {
            this.session = session;

            try
            {
                CaptureRequest.Builder requestBuilder = cameraHelper.getCamera().createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                requestBuilder.addTarget(new Surface(texture));

                request = requestBuilder.build();

                session.setRepeatingRequest(request, null, null);
            }
            catch (CameraAccessException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    }

    /**
     * Inner helper class for handling Callback events from the CameraCaptureSession.
     * Initializes the Capture session and handles the successful return of the session as well as any failed returns.
     */
    public class CaptureSessionCaptureCallbackHelper extends CameraCaptureSession.CaptureCallback
    {
        public CaptureSessionCaptureCallbackHelper()
        {
            super();
        }

        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber)
        {
            super.onCaptureStarted(session, request, timestamp, frameNumber);


        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result)
        {
            super.onCaptureCompleted(session, request, result);


        }

        @Override
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure)
        {
            super.onCaptureFailed(session, request, failure);


        }
    }

}
