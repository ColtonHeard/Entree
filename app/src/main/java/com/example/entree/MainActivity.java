package com.example.entree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mlkit.vision.objects.DetectedObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Button ingredientList = (Button) findViewById(R.id.button2); // Ingredient List Button

    ImageView imageView;
    TextView ingredientText, settingsText, calorieAmount, fatAmount, saturatedFatAmount, transFatAmount, cholesterolAmount, sodiumAmount, carbAmount, fiberAmount, totalSugarAmount, proteinAmount, vitaminDAmount, calciumAmount, ironAmount, potassiumAmount, nutritionFactsLabel;
    EditText IngredientSearch;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    FoodObjectRecognizer recognizer;
    FoodData Data;
    View mainView, ingredientView, settingsView, nutritionView;
    Boolean dataHasNotBeenRead = true;

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
        setContentView(nutritionView);
    }

    public void settingsOnClick(View view) {
        /** This is where code that executes upon clicking the 'Settings' button will be placed. Making changes to this
         * is not important for milestone 1.
         */
        setContentView(settingsView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (View) findViewById(R.id.mainView);

        setContentView(R.layout.ingredient_list);

        ingredientView = (View) findViewById(R.id.ingredientView);
        ingredientText = (TextView) findViewById(R.id.ingredientTextView);

        setContentView(R.layout.settings);

        settingsView = (View) findViewById(R.id.settingsView);
        settingsText = (TextView) findViewById(R.id.settingsTextView);

        setContentView(R.layout.information);
        nutritionView = (View) findViewById(R.id.nutritionView);
        nutritionFactsLabel = (TextView) findViewById(R.id.nutritionFactsLabel);
        IngredientSearch = (EditText) findViewById(R.id.IngredientSearch);
        calorieAmount = (TextView) findViewById(R.id.calorieAmount);
        fatAmount = (TextView) findViewById(R.id.fatAmount);
        saturatedFatAmount = (TextView) findViewById(R.id.saturatedFatAmount);
        transFatAmount = (TextView) findViewById(R.id.transFatAmount);
        cholesterolAmount = (TextView) findViewById(R.id.cholesterolAmount);
        sodiumAmount = (TextView) findViewById(R.id.sodiumAmount);
        carbAmount = (TextView) findViewById(R.id.carbAmount);
        fiberAmount = (TextView) findViewById(R.id.fiberAmount);
        totalSugarAmount = (TextView) findViewById(R.id.totalSugarAmount);
        proteinAmount = (TextView) findViewById(R.id.proteinAmount);
        vitaminDAmount = (TextView) findViewById(R.id.vitaminDAmount);
        calciumAmount = (TextView) findViewById(R.id.calciumAmount);
        ironAmount = (TextView) findViewById(R.id.ironAmount);
        potassiumAmount = (TextView) findViewById(R.id.potassiumAmount);

        Log.d("IngredientText", "Ingredient text is null = " + (ingredientText == null));

        setContentView(R.layout.information);
        informationView = (View) findViewById(R.id.informationView);

        setContentView(R.layout.settings);
        settingView = (View) findViewById(R.id.settingView);

        setContentView(mainView);

        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.buttonLoadPicture);

        recognizer = new FoodObjectRecognizer();
        Data = new FoodData();

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

    private HashMap<String, FoodData> foodMap = new HashMap<>();

    private void readFoodData() {
        // Read the raw csv file
        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(getResources().openRawResource(R.raw.food), Charset.forName("UTF-8"))
        );

        // Initialization
        String line = "";

        // Initialization
        try {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                FoodData data = new FoodData();
                // Setters
                data.setArray(tokens);
                // Adding object to a class
                foodMap.put(data.getName().toLowerCase(), data);
            }
            reader.close();
            /*print();*/
        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivityError", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }

    public void searchClick(View view){
        if (dataHasNotBeenRead) {
            readFoodData();
            dataHasNotBeenRead = false;
        }
        FoodData food = null;

        if ((food = foodMap.get(IngredientSearch.getText().toString().toLowerCase())) != null) {
            setupLabel(food);
        } else {
            setupLabel(new FoodData());
        }
    }

    private void setupLabel(FoodData food){
        calorieAmount.setText(food.getCalories());
        fatAmount.setText(food.getFat());
        saturatedFatAmount.setText(food.getSaturated());
        cholesterolAmount.setText(food.getCholesterol());
        sodiumAmount.setText(food.getSodium());
        carbAmount.setText(food.getCarbs());
        fiberAmount.setText(food.getFiber());
        totalSugarAmount.setText(food.getSugar());
        proteinAmount.setText(food.getProtein());
        vitaminDAmount.setText(food.getVitaminD());
        calciumAmount.setText(food.getCalcium());
        ironAmount.setText(food.getIron());
        potassiumAmount.setText(food.getPotassium());

        if(food.getName().equals("None")){
            nutritionFactsLabel.setText("Nutrition Facts");
        } else {
            nutritionFactsLabel.setText("Nutrition Facts: " + food.getName());
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