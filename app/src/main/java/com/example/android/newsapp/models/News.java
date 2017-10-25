package com.example.android.newsapp.models;

import java.util.Date;

public class News {

    private String title;
    private String sectionName;
    private Date publicationDate;
    private String webUrl;

    public News(String title, String sectionName, Date publicationDate, String webUrl) {
        this.title = title;
        this.sectionName = sectionName;
        this.publicationDate = publicationDate;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
