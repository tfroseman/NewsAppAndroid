package com.newsappandroid.model;

import java.util.ArrayList;

public class Categories {
    public static ArrayList<String> categoriesList;

    public Categories() {
    }

    public static ArrayList<String> getCategoriesList() {
        return categoriesList;
    }

    public static String getCategory(int position){
        return Categories.categoriesList.get(position);
    }

    public static void createCategoriesList(ArrayList<String> categoriesList) {
        Categories.categoriesList = categoriesList;
    }

    public static void insert(String articleTitle){
        Categories.categoriesList.add(articleTitle);
    }
}
