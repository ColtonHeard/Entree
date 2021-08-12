package com.example.entree.recipe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Recipe scraper class that makes use of Google's search API and Recipe Schema to find relevant recipes from a list of ingredients.
 */
public class AllRecipesDataSource extends RecipesDataSource {

    public class MalformedHtml extends Exception {}

    private Integer page = 1;
    private Boolean hasNextPage = true;

    public AllRecipesDataSource(RecipeDataSourceListener recipeListener) {
        super(recipeListener);
    }

    public void setIngredients(ArrayList<String> ingredientsArray) {
        if (searchThread != null) {
            searchThread.interrupt(); // No guarantee its null by...
        }
        this.ingredientsArray = ingredientsArray;
        recipesCache = new ArrayList<Recipe>();
        page = 1;
        getRecipes(); // This call
    }


    public void getRecipes() {
        if (searchThread != null) {return;}

        searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!hasNextPage) {
                    listener.onRecipesReady(null) ;
                     return;
                };

                HttpURLConnection connection;
                try {connection = buildRequestConnectionFor(ingredientsArray, page);}
                catch (IOException e) {e.printStackTrace(); return;}

                if (searchThread.isInterrupted()) {return;}

                JSONObject responseJson;
                try {responseJson = (JSONObject) getResponseFrom(connection).nextValue();}
                catch (IOException | JSONException e) {e.printStackTrace(); return;}

                if (searchThread.isInterrupted()) {return;}

                String html;
                try {
                    html = responseJson.getString("html");;
                    hasNextPage = responseJson.getBoolean("hasNext");
                }
                catch (JSONException e) {e.printStackTrace(); return;}

                if (searchThread.isInterrupted()) {return;}

                ArrayList<Recipe> recipesList;
                try {recipesList = getRecipesFrom(html);}
                catch (IOException e) {e.printStackTrace(); return;}

                recipesCache = recipesList;
                notifyListener();
                page++;
                return;
            }
        });

        searchThread.setPriority(Thread.NORM_PRIORITY);
        searchThread.start();
    }

    public void notifyListener() {
        listener.onRecipesReady(recipesCache);
        getRecipes();
    }

    public HttpsURLConnection buildRequestConnectionFor(ArrayList<String> ingredients, Integer page) throws IOException {
        final Integer maxRetry = 5;

        Uri.Builder urlBuilder = new Uri.Builder();

        urlBuilder.scheme("https")
                .authority("www.allrecipes.com")
                .appendPath("element-api")
                .appendPath("content-proxy")
                .appendPath("faceted-searches-load-more")
                .appendQueryParameter("page", page.toString());

        for (String ingredient : ingredients) {
            urlBuilder.appendQueryParameter("IngIncl", ingredient);
        }

        URL requestURL = null;
        try {
            requestURL = new URL(urlBuilder.build().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpsURLConnection requestConnection;

        Integer retryCount = 0;
        while (true)
            try {
                requestConnection = (HttpsURLConnection)requestURL.openConnection();
                break;
            } catch (IOException e) { // openConnection Failed. Retry or propagate.
                if (retryCount < maxRetry) {retryCount++;}
                else {throw e;}
            }

        return requestConnection;
    }

    public JSONTokener getResponseFrom(HttpURLConnection connection) throws IOException, JSONException {
        Integer maxRetry = 5;

        InputStream jsonStream;
        Integer retryCount = 0;

        while (true) {
            try {
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    jsonStream = connection.getInputStream();
                    break;
                } else {
                    throw new IOException("Unknown Connection Error");
                }
            }
            catch (IOException e) { // Connection Failed, try again or propagate.
                if (retryCount == maxRetry) {throw e;}
                else {retryCount++;}
            }
        }

        Scanner jsonScanner = new Scanner(jsonStream, StandardCharsets.UTF_8.name());
        String jsonString = jsonScanner.useDelimiter("\\A").next();

        JSONTokener json = new JSONTokener(jsonString);
        if (json == null) {throw new JSONException("JSONTokener Failed. Response Invalid?");}

        return json;
    }

    public Bitmap getImageFrom(URL imageURL) throws IOException {

        HttpsURLConnection connection = (HttpsURLConnection) imageURL.openConnection();

        Integer maxRetry = 5;

        InputStream imageStream;
        Integer retryCount = 0;

        while (true) {
            try {
                switch (connection.getResponseCode()) {
                    case HttpURLConnection.HTTP_OK:
                        imageStream = connection.getInputStream();
                        break;
                    default:
                        throw new IOException("Unknown Connection Error");
                }
                break;
            } catch (IOException e) { // Connection Failed, try again or propagate.
                if (retryCount == maxRetry) {
                    throw e;
                } else {
                    retryCount++;
                }
            }
        }
        return BitmapFactory.decodeStream(imageStream);
    }

    public ArrayList<Recipe> getRecipesFrom(String html) throws IOException {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        Document document = Jsoup.parseBodyFragment(html);

        Elements recipeElements = document.getElementsByClass("component card card__recipe card__facetedSearchResult");
        for (Element recipeElement : recipeElements) {

            RecipeBuilder builder = new RecipeBuilder();

            try {
                Element imageContainerElement = recipeElement.getElementsByClass("card__imageContainer").first();
                Element imageLinkContainer = imageContainerElement.getElementsByClass("card__titleLink manual-link-behavior").first();
                Element imageElement = imageLinkContainer.getElementsByClass("component lazy-image lazy-image-udf aspect_1x1").first();
                URL imageURL = new URL(imageElement.attributes().get("data-src"));
                builder.setImage(getImageFrom(imageURL));



                Element detailsContainer = recipeElement.getElementsByClass("card__detailsContainer").first();
                Element descriptionElement = detailsContainer.getElementsByClass("card__detailsContainer-left").first();
                Attributes titleAndLinkElementAttributes = descriptionElement.getElementsByClass("card__titleLink manual-link-behavior").first().attributes();
                builder.setTitle(titleAndLinkElementAttributes.get("title"));
                builder.setUrl(new URL(titleAndLinkElementAttributes.get("href")));

                builder.setDescription(descriptionElement.getElementsByClass("card__summary").first().text());
            } catch (NullPointerException e) {continue;}

            recipes.add(builder.buildRecipe());
        }

        return recipes;
    }

}
