package com.example.entree;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;

import java.util.ArrayList;
import java.util.List;

/**
 Class for interacting with and processing images through the AI.
 */
public class FoodObjectRecognizer
{
    /** Arraylist containing objects detected from the networks input image. This is set on an independent thread and may not always have a value. */
    private ArrayList<DetectedObject> foundObjects;

    /** ObjectDetector instance responsible for handling the AI model and processing image requests. */
    private ObjectDetector detector;

    /** Instance to the CameraView, used for passing information between classes. SHOULD BE REPLACED WITH A LISTENER AND REMOVED LATER. */
    private CameraView view;

    /** Instance to the MainActivity so that tasks can be run on the UI thread */
    private MainActivity mainActivity;

    /**
     Constructor that takes the associated CameraView and a reference to the app's MainActivity. Initializes the ObjectDetector.

     @param v The CameraView this FoodObjectRecognizer belongs to.
     @param main A reference to the application's MainActivity.
     */
    public FoodObjectRecognizer(CameraView v, MainActivity main)
    {
        view = v;
        mainActivity = main;

        ObjectDetectorOptions options = new ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build();

        detector = ObjectDetection.getClient(options);
    }

    /**
     Processes a given image through the AI model. On success, returns a list of the detected objects. On failure nothing happens and an error message is sent to the log.

     @param image A Bitmap of the input image's pixel information.
     */
    public void processImage(Bitmap image)
    {
        InputImage input = InputImage.fromBitmap(image, 0);
        detector.process(input)
                .addOnSuccessListener(
                        new OnSuccessListener<List<DetectedObject>>() {
                            @Override
                            public void onSuccess(List<DetectedObject> detectedObjects)
                            {
                                //Task completed successfully
                                //System.out.println("Object detector process() finished successfully")

                                ArrayList<DetectedObject> foodObjects = new ArrayList<DetectedObject>();

                                for (DetectedObject detectedObject : detectedObjects)
                                {
                                    Rect boundingBox = detectedObject.getBoundingBox();
                                    Integer trackingId = detectedObject.getTrackingId();

                                    for (DetectedObject.Label label : detectedObject.getLabels())
                                    {
                                        String text = label.getText();
                                        if (PredefinedCategory.FOOD.equals(text))
                                        {
                                            Log.d("Food", "Bounding box = " + boundingBox.toString());
                                            foodObjects.add(detectedObject);
                                        }
                                    }
                                }

                                foundObjects = foodObjects;
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.performLongClick();
                                    }
                                });
                            }
                        })

                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Task failed
                                Log.d("AIFailed", "Object detector process() method has failed!");
                            }
                        });
    }

    /**
     Returns the list of detected objects.

     @return An ArrayList containing all of the DetectedObjects from the AI.
     */
    public List<DetectedObject> getFoundObjects()
    {
        return foundObjects;
    }

    /**
     Returns a list of strings containing the details of every currently detected object or "No detected food objects!" if none are currently available.

     @return An ArrayList containing strings with information about each DetectedObject, including the confidence and location of it's bounding box.
     */
    public ArrayList<String> getObjectsInfo()
    {
        ArrayList<String> info = new ArrayList<>();

        if (foundObjects == null) { return info; }

        for (int y = 0; y < foundObjects.size(); y++)
        {
            DetectedObject obj = foundObjects.get(y);
            List<DetectedObject.Label> labels = obj.getLabels();

            float confidence = 1337;
            for (int x = 0; x < labels.size(); x++)
            {
                if (PredefinedCategory.FOOD.equals(labels.get(x).getText()))
                {
                    confidence = obj.getLabels().get(x).getConfidence();
                }
            }
            String infoString = "Category: FOOD Confidence: " + confidence + " Location: " + obj.getBoundingBox().toShortString();
            info.add(infoString);
        }

        if (info.size() == 0)
        {
            info.add("No detected food objects!");
        }

        return info;
    }
}
