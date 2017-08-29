package com.innovaocean.guardianviewer.model;

public class NewsArticle {
    public String id;
    public String webTitle;
    public String webUrl;
    public Fields fields;

    public class Fields {
        public String thumbnail;
    }
}