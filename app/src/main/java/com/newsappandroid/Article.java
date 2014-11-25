package com.newsappandroid;

import java.util.Date;

/**
 * Created by Thomas on 11/24/2014.
 */
public class Article {
    private int id;
    private String title;
    private String description;
    private String link;
    private String image_link;
    private Date created_time;
    private Date scrape_time;
    private boolean is_active;
    private int current_relevance;
    private int rss_url_id;

    public Article() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public Date getScrape_time() {
        return scrape_time;
    }

    public void setScrape_time(Date scrape_time) {
        this.scrape_time = scrape_time;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getCurrent_relevance() {
        return current_relevance;
    }

    public void setCurrent_relevance(int current_relevance) {
        this.current_relevance = current_relevance;
    }

    public int getRss_url_id() {
        return rss_url_id;
    }

    public void setRss_url_id(int rss_url_id) {
        this.rss_url_id = rss_url_id;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", image_link='" + image_link + '\'' +
                ", created_time=" + created_time +
                ", scrape_time=" + scrape_time +
                ", is_active=" + is_active +
                ", current_relevance=" + current_relevance +
                ", rss_url_id=" + rss_url_id +
                '}';
    }
}
