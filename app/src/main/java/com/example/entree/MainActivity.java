package com.example.entree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mlkit.vision.objects.DetectedObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Button ingredientList = (Button) findViewById(R.id.button2); // Ingredient List Button

    ImageView imageView;
    TextView ingredientText;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    FoodObjectRecognizer recognizer;
    View mainView, ingredientView;

    public void cameraButton(View view) {
        /**This is where the camera should open when the camera button is clicked. Proper permissions are already provided
         * in the AndroidManifest.xml
        **/
    }

    public void ingredientClick(View ingredientList) {
        /** This is where code that executes upon clicking the 'Ingredients List' button will be placed. Making changes to this
         *  is not important for milestone 1.
         */
        setContentView(ingredientView);
    }

    public void ingredientReturnOnClick(View view)
    {
        setContentView(mainView);
    }

    public void extraOnClick(View view) {
        /** This is where code that executes upon clicking the 'Extra Info' button will be placed. Making changes to this
         * is not important for milestone 1.
         */
        setContentView(R.layout.information);
    }

    public void settingsOnClick(View view) {
        /** This is where code that executes upon clicking the 'Settings' button will be placed. Making changes to this
         * is not important for milestone 1.
         */
        setContentView(R.layout.settings);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (View) findViewById(R.id.mainView);

        setContentView(R.layout.ingredient_list);

        ingredientView = (View) findViewById(R.id.ingredientView);
        ingredientText = (TextView) findViewById(R.id.ingredientTextView);

        Log.d("IngredientText", "Ingredient text is null = " + (ingredientText == null));

        setContentView(mainView);

        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.buttonLoadPicture);

        recognizer = new FoodObjectRecognizer();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                Log.d("Gallery", "Gallery opened");
            }
        });
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (image == null)
            {
                Log.d("BitmapLoad", "Failed to create bitmap from file uri");
            }
            else
            {
                Log.d("BitmapLoad", "Successfully made bitmap from file uri");
                recognizer.processImage(image);

                Handler handler = new Handler();
                Bitmap finalImage = image;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (String s : recognizer.getObjectsInfo())
                        {
                            Log.d("AIInfo", s);
                        }

                        String s = "";
                        for (String str : recognizer.getObjectsInfo())
                        {
                            s += str + "\n\n";
                        }
                        ingredientText.setText(s);
                        ingredientText.setTextSize(20);

                        paintBoundingBoxes(recognizer.getFoundObjects(), finalImage);
                    }
                }, 2000);
            }

        }
    }

    /*
    Paints the bounding boxes of any food objects that have been recognized by the AI.
    Does so by painting transparent boxes over the imageview's content, then updating the image view.
     */
    private void paintBoundingBoxes(List<DetectedObject> foundObjects, Bitmap imageBitmap)
    {
        ImageView view = (ImageView) findViewById(R.id.imageView);

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
        view.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
}