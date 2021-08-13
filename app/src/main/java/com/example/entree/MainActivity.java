package com.example.entree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
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

}