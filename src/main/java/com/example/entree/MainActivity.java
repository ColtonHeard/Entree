package com.example.entree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Button ingredientList = (Button) findViewById(R.id.button2); // Ingredient List Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cameraButton(View view) {
        /**This is where the camera should open when the camera button is clicked. Proper permissions are already provided
         * in the AndroidManifest.xml
        **/
    }

    public void ingredientClick(View ingredientList) {
        /** This is where code that executes upon clicking the 'Ingredients List' button will be placed. Making changes to this
         *  is not important for milestone 1.
         */
        setContentView(R.layout.ingredient_list);
    }

    public void extraOnClick(View view) {
        /** This is where code that executes upon clicking the 'Extra Info' button will be placed. Making changes to this
         * is not important for milestone 1.
         */
    }

    public void settingsOnClick(View view) {
        /** This is where code that executes upon clicking the 'Settings' button will be placed. Making changes to this
         * is not important for milestone 1.
         */
    }
}