package com.example.entree;

import org.junit.Test;

import static org.junit.Assert.*;

public class FoodDataTest
{
    private String[] inputData = {"Banana", "100", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    @Test
    public void testName()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("Banana", data.getName());
    }

    @Test
    public void testCalories()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("100", data.getCalories());
    }

    @Test
    public void testFat()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("1g", data.getFat());
    }

    @Test
    public void testSaturatedFat()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("2g", data.getSaturated());
    }

    @Test
    public void testCholesterol()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("3mg", data.getCholesterol());
    }

    @Test
    public void testSodium()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("4mg", data.getSodium());
    }

    @Test
    public void testCarbs()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("5g", data.getCarbs());
    }

    @Test
    public void testFiber()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("6g", data.getFiber());
    }

    @Test
    public void testSugar()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("7g", data.getSugar());
    }

    @Test
    public void testProtein()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("8g", data.getProtein());
    }

    @Test
    public void testVitaminD()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("9mcg", data.getVitaminD());
    }

    @Test
    public void testCalcium()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("10mg", data.getCalcium());
    }

    @Test
    public void testIron()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("11mg", data.getIron());
    }

    @Test
    public void testPotassium()
    {
        FoodData data = new FoodData();
        data.setArray(inputData);

        assertEquals("12mg", data.getPotassium());
    }
}
