package com.example.entree;

import android.content.res.Resources;
import android.content.res.loader.ResourcesLoader;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FoodObjectRecognizer
{

    private ArrayList<DetectedObject> foundObjects;
    private ObjectDetector detector;
    private CameraView view;
    private MainActivity mainActivity;

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

    public void processImage(Bitmap image)
    {
        //Pass images to the ObjectDetector's process() method
        //Pass as a Bitmap, NV21 ByteBuffer, or YUV_420_888 medio.Image
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

    public List<DetectedObject> getFoundObjects()
    {
        return foundObjects;
    }

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
