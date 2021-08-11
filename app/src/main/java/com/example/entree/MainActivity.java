package com.example.entree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{

    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private FoodData Data;
    private CameraView camera;
    private HashMap<String, FoodData> foodMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Data = new FoodData();

        MenuBarsView menuBars = new MenuBarsView(this, null, this);
//        ((ViewGroup)mainView.getParent()).removeView(mainView);
        setContentView(menuBars);

    }

    public void setCameraView(CameraView v)
    {
        camera = v;
    }

    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            camera.setImageURI(imageUri);
        }
    }

    private void readFoodData()
    {
        // Read the raw csv file
        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(getResources().openRawResource(R.raw.food), Charset.forName("UTF-8"))
        );

        String line = "";

        try
        {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null)
            {
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");

                // Read the data
                FoodData data = new FoodData();
                data.setArray(tokens);

                // Add the read food to the FoodData HashMap
                foodMap.put(data.getName().toLowerCase(), data);
            }

            reader.close();
        }
        catch (IOException e)
        {
            // Logs error with priority level
            Log.wtf("MyActivityError", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }

}