package com.example.entree;

public class FoodData {
    private String description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCalories(String calories) {
        this.calories = calories;
    }
    public void setFat(String fat) {
        this.fat = fat;
    }
    public void setSaturated(String saturated) {
        this.saturated = saturated;
    }
    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }
    public void setSodium(String sodium) {
        this.sodium = sodium;
    }
    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }
    public void setFiber(String fiber) {
        this.fiber = fiber;
    }
    public void setSugar(String sugar) {
        this.sugar = sugar;
    }
    public void setProtein(String protein) {
        this.protein = protein;
    }
    public void setVitaminD(String vitaminD) {
        this.vitaminD = vitaminD;
    }
    public void setCalcium(String calcium) {
        this.calcium = calcium;
    }
    public void setIron(String iron) {
        this.iron = iron;
    }
    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }


    public String toCal() {
        return "" + calories;
    }

    public String toFat() {
        return "" + fat;
    }

    public String toSat() {
        return "" + saturated;
    }

    public String toCho() {
        return "" + cholesterol;
    }

    public String toSod() {
        return "" + sodium;
    }

    public String toCar() {
        return "" + carbs;
    }

    public String toFib() {
        return "" + fiber;
    }

    public String toSug() {
        return "" + sugar;
    }

    public String toPro() {
        return "" + protein;
    }

    public String toVit() {
        return "" + vitaminD;
    }

    public String toCalc() {
        return "" + calcium;
    }

    public String toIro() {
        return "" + iron;
    }

    public String toPot() {
        return "" + potassium;
    }

    public String toClear() {
        return "";
    }

}
