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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Button ingredientList = (Button) findViewById(R.id.button2); // Ingredient List Button

    ImageView imageView;
    TextView ingredientText, settingsText, calorieAmount, fatAmount, saturatedFatAmount, transFatAmount, cholesterolAmount, sodiumAmount, carbAmount, fiberAmount, totalSugarAmount, proteinAmount, vitaminDAmount, calciumAmount, ironAmount, potassiumAmount;
    EditText IngredientSearch;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    FoodObjectRecognizer recognizer;
    FoodData Data;
    View mainView, ingredientView, settingsView, nutritionView;

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

    private List<FoodData> Name = new ArrayList<>();
    private List<FoodData> Calories = new ArrayList<>();
    private List<FoodData> Fat = new ArrayList<>();
    private List<FoodData> Saturated = new ArrayList<>();
    private List<FoodData> Cholesterol = new ArrayList<>();
    private List<FoodData> Sodium = new ArrayList<>();
    private List<FoodData> Carbs = new ArrayList<>();
    private List<FoodData> Fibers = new ArrayList<>();
    private List<FoodData> Sugar = new ArrayList<>();
    private List<FoodData> Protein = new ArrayList<>();
    private List<FoodData> VitaminD = new ArrayList<>();
    private List<FoodData> Calcium = new ArrayList<>();
    private List<FoodData> Iron = new ArrayList<>();
    private List<FoodData> Potassium = new ArrayList<>();

    private void readFoodData() {
        // Read the raw csv file

        String na = "Banana";
        String ka = "Kale";
        String ca = "Carrot";
        InputStream FINAL = null;

        if(IngredientSearch.getText().toString().equals(na)) {
            InputStream Ban = getResources().openRawResource(R.raw.banana);
            FINAL = Ban;
        }
        if(IngredientSearch.getText().toString().equals(ka)) {
            InputStream Kal = getResources().openRawResource(R.raw.carrot);
            FINAL = Kal;
        }
        if(IngredientSearch.getText().toString().equals(ca)) {
            InputStream Car = getResources().openRawResource(R.raw.kale);
            FINAL = Car;
        }


        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(FINAL, Charset.forName("UTF-8"))
        );

        // Initialization
        String line = "";

        // Initialization
        try {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity","Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                FoodData name = new FoodData();
                FoodData calories = new FoodData();
                FoodData fat = new FoodData();
                FoodData saturated = new FoodData();
                FoodData cholesterol = new FoodData();
                FoodData sodium = new FoodData();
                FoodData carbs = new FoodData();
                FoodData fiber = new FoodData();
                FoodData sugar = new FoodData();
                FoodData protein = new FoodData();
                FoodData vitaminD = new FoodData();
                FoodData calcium = new FoodData();
                FoodData iron = new FoodData();
                FoodData potassium = new FoodData();

                Log.d("Data Setter", "Just created: " + name);
                // Setters
                name.setName(tokens[0]);
                calories.setCalories(tokens[1]);
                fat.setFat(tokens[2]);
                saturated.setSaturated(tokens[3]);
                cholesterol.setCholesterol(tokens[4]);
                sodium.setSodium(tokens[5]);
                carbs.setCarbs(tokens[6]);
                fiber.setFiber(tokens[7]);
                sugar.setSugar(tokens[8]);
                protein.setProtein(tokens[9]);
                vitaminD.setVitaminD(tokens[10]);
                calcium.setCalcium(tokens[11]);
                iron.setIron(tokens[12]);
                potassium.setPotassium(tokens[13]);

                Log.d("Food Add", "Just created: " + name);
                // Adding object to a class
                Name.add(name);
                Calories.add(calories);
                Fat.add(fat);
                Saturated.add(saturated);
                Cholesterol.add(cholesterol);
                Sodium.add(sodium);
                Carbs.add(carbs);
                Fibers.add(fiber);
                Sugar.add(sugar);
                Protein.add(protein);
                VitaminD.add(vitaminD);
                Calcium.add(calcium);
                Iron.add(iron);
                Potassium.add(potassium);


                // Log the object
                Log.d("My Activity", "Just created: " + name);
            }
            /*print();*/
        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivityError", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }

    public void searchClick(View view){
        readFoodData();
        String name = "";
        String a = "";
        String b = "";
        String c = "";
        String d = "";
        String e = "";
        String f = "";
        String g = "";
        String h = "";
        String i = "";
        String j = "";
        String k = "";
        String l = "";
        String m = "";

        String na = "Banana";
        String ka = "Kale";
        String ca = "Carrot";

            if (IngredientSearch.getText().toString().equals(na)) {
                name = na;
            }
            if (IngredientSearch.getText().toString().equals(ka)) {
                name = ka;
            }
            if (IngredientSearch.getText().toString().equals(ca)) {
                name = ca;
            }

            if (IngredientSearch.getText().toString().equals(name)) {
                for (FoodData cal : Calories) {
                    a += cal.toCal();
                    calorieAmount.setText(a);
                }

                for (FoodData fa : Fat) {
                    b += fa.toFat();
                    fatAmount.setText(b);
                }


                for (FoodData sat : Saturated) {
                    c += sat.toSat();
                    saturatedFatAmount.setText(c);
                }


                for (FoodData chol : Cholesterol) {
                    d += chol.toCho();
                    cholesterolAmount.setText(d);
                }

                for (FoodData sod : Sodium) {
                    e += sod.toSod();
                }
                sodiumAmount.setText(e);

                for (FoodData car : Carbs) {
                    f += car.toCar();
                }
                carbAmount.setText(f);

                for (FoodData fib : Fibers) {
                    g += fib.toFib();
                }
                fiberAmount.setText(g);

                for (FoodData sug : Sugar) {
                    h += sug.toSug();
                }
                totalSugarAmount.setText(h);

                for (FoodData pro : Protein) {
                    i += pro.toPro();
                }
                proteinAmount.setText(i);

                for (FoodData vit : VitaminD) {
                    j += vit.toVit();
                }
                vitaminDAmount.setText(j);

                for (FoodData calc : Calcium) {
                    k += calc.toCalc();
                }
                calciumAmount.setText(k);

                for (FoodData iro : Iron) {
                    l += iro.toIro();
                }
                ironAmount.setText(l);

                for (FoodData pot : Potassium) {
                    m += pot.toPot();
                }
                potassiumAmount.setText(m);

                Calories.clear();
                Fat.clear();
                Saturated.clear();
                Cholesterol.clear();
                Sodium.clear();
                Carbs.clear();
                Fibers.clear();
                Sugar.clear();
                Protein.clear();
                VitaminD.clear();
                Calcium.clear();
                Iron.clear();
                Potassium.clear();
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