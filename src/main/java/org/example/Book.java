package org.example;

public class Book {
    String title;
    String starRating;
    String url;

    public Book(String title, String url, String starRating)
    {
        this.starRating = starRating;
        this.title = title;
        this.url = "http://books.toscrape.com/catalogue/" + url.substring(6);
    }

    public String toString()
    {
        return String.format("Title: %s Link: %s starRating: %s", title, url, starRating);
    }

}
