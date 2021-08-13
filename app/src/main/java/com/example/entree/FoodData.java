package com.example.entree;

public class FoodData {
    private String name;
    private String calories;
    private String fat;
    private String saturated;
    private String cholesterol;
    private String sodium;
    private String carbs;
    private String fiber;
    private String sugar;
    private String protein;
    private String vitaminD;
    private String calcium;
    private String iron;
    private String potassium;

    public FoodData()
    {
        name = "Example Food";
        calories = "0";
        fat = "0g";
        saturated = "0g";
        cholesterol = "0mg";
        sodium = "0mg";
        carbs = "0g";
        fiber = "0g";
        sugar = "0g";
        protein = "0g";
        vitaminD = "0mcg";
        calcium = "0mg";
        iron = "0mg";
        potassium = "0mg";
    }

    public void setArray(String[] data) {
        name = data[0];
        calories = data[1];
        fat = data[2] + "g";
        saturated = data[3] + "g";
        cholesterol = data[4] + "mg";
        sodium = data[5] + "mg";
        carbs = data[6] + "g";
        fiber = data[7] + "g";
        sugar = data[8] + "g";
        protein = data[9] + "g";
        vitaminD = data[10] + "mcg";
        calcium = data[11] + "mg";
        iron = data[12] + "mg";
        potassium = data[13] + "mg";
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    public String getFat() {
        return fat;
    }

    public String getSaturated() {
        return saturated;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public String getSodium() {
        return sodium;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFiber() {
        return fiber;
    }

    public String getSugar() {
        return sugar;
    }

    public String getProtein() {
        return protein;
    }

    public String getVitaminD() {
        return vitaminD;
    }

    public String getCalcium() {
        return calcium;
    }

    public String getIron() {
        return iron;
    }

    public String getPotassium() {
        return potassium;
    }
}
