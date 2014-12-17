package com.newsappandroid.model;

import java.util.ArrayList;

public class ArticleList {
    String id;
    String title;
    String description;
    String url;

    public ArticleList(String articleID, String articleTitle, String articleDescription, String articleURL){

        id = articleID;
        title = articleTitle;
        description = articleDescription;
        url = articleURL;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl(){
        return url;
    }
}
