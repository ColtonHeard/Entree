package com.example.entree.recipiesTests.AllRecipiesDataSourceTests;

import com.example.entree.recipe.AllRecipesDataSource;
import com.example.entree.recipe.Recipe;

import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

@RunWith(AndroidJUnit4.class)
public class AllRecipesDataSourceTest {

    URL correctURL;
    Integer page = 2;

    ArrayList<String> ingredients;
    AllRecipesDataSource dataSource;
    String correctResponse;
    URL imageURL;

    @Before
    public void setUp() throws MalformedURLException, FileNotFoundException {
        dataSource = new AllRecipesDataSource((recipes) -> {});
        ingredients = new ArrayList<>();

        correctURL = new URL("https://www.allrecipes.com/element-api/content-proxy/faceted-searches-load-more?page=1&IngIncl=strawberry");
        ingredients.add("Blueberry");
        correctResponse = new Scanner("correctResponse.json").useDelimiter("\\A").next();
        imageURL = new URL("https://images.media-allrecipes.com/userphotos/5983455.jpg");
    }

    @Test
    public void buildRequestConnectionTest() {
        HttpsURLConnection connection = null;
        try {
            connection = dataSource.buildRequestConnectionFor( ingredients, page);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(correctURL, connection.getURL());
    }

    @Test
    public void getResponseTest() throws IOException, JSONException { // This test is terrible...
        HttpsURLConnection connection = dataSource.buildRequestConnectionFor(ingredients, page);

        JSONTokener jsonTokener = null;
        try {
            jsonTokener = dataSource.getResponseFrom(connection);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getImageFrom() throws IOException {
        Bitmap testBitmap = dataSource.getImageFrom(imageURL);

    }

    @Test
    public void getRecipesTest() throws IOException, JSONException, AllRecipesDataSource.MalformedHtml {
        HttpsURLConnection connection = dataSource.buildRequestConnectionFor(ingredients, page);

        JSONTokener jsonTokener = null;
        try {
            jsonTokener = dataSource.getResponseFrom(connection);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String html = ((JSONObject)jsonTokener.nextValue()).getString("html");

        dataSource.getRecipesFrom(html);

    }

    @Test
    public void testGetNextElement() throws InterruptedException {

        ArrayList<Recipe> recipies = new ArrayList<>();
        dataSource.setIngredientsArray(new ArrayList<String>(Arrays.asList(new String[]{"strawberry"})));

            dataSource.getRecipes();



    }

}
