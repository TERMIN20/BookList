package org.example;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/*
This code uses the HtmlUnit package under the Apache License, Version 2.0

Initial idea inspired by ScrapingBee's CraigsList Scraper by Kevin Sahin

Uses toscrape.com's fictional online bookstore web scraping sandbox and abides by all of their policies of use.
*/


public class Main {
    public static void main(String[] args) throws IOException {
        //define url variables and rating threshold
        String category = "_1";
        String ratingThreshold = "five";
        int page = 1;

        //create stack to store Book objects
        Stack<Book> bookStack = new Stack<Book>();
        //start website crawling and update stack
        startSearch(page, category, ratingThreshold, bookStack);
        //reverses stack
        bookStack = reverseStack(bookStack);
        //output stack into BookList.txt file
        stackToFile(bookStack);

    }

    private static Stack<Book> reverseStack(Stack<Book> books) {
        Queue<Book> q = new LinkedList<>();
        System.out.println(books.toString());
        while(!books.isEmpty()){
            q.add(books.pop());
        }

        while(!q.isEmpty())
        {
            books.add(q.remove());
        }

        return books;
    }

    private static void stackToFile(Stack<Book> books) throws IOException {


        //Create file or get it empty.
        Path file = Path.of("C:\\Users\\abhin\\IdeaProjects\\Craigslist Project\\src\\main\\java\\org\\example\\BookList.txt");
        File bookList = new File("BookList.txt");
        try {
            if (bookList.createNewFile()) {
                System.out.println("File created: " + bookList.getName());
            } else {
                System.out.println("File already exists.");
            }
        }catch(IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }

        FileWriter myWriter = new FileWriter("BookList.txt");
        myWriter.flush();

        while(!books.isEmpty()){
            writeToFile(books.pop().toString());
        }
    }

    public static void writeToFile(String entry)
    {
        try {
            FileWriter writer = new FileWriter("BookList.txt", true);
            writer.write(entry + "\n");

            writer.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    //recursively iterate through the website's pages until all pages are considered.
    public static void startSearch(int page, String category, String ratingThreshold, Stack<Book> bookStack) throws IOException {
        String url = "http://books.toscrape.com/catalogue/category/books" + category + "/page-" + page + ".html";
        System.out.println(noNextPage(url));

        //base case: No next page
        if(noNextPage(url)|| page == 5){
            getPage(url, ratingThreshold, bookStack);
            //add output method
            System.out.println(bookStack.toString());
        }
        else{
            getPage(url, ratingThreshold, bookStack);
            startSearch(page+1, category, ratingThreshold, bookStack);
        }

    }

    public static void getPage(String url, String ratingThreshold, Stack<Book> bookStack) throws IOException {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        HtmlPage page = client.getPage(url);
        List<HtmlElement> items = page.getByXPath("//li[@class='col-xs-6 col-sm-4 col-md-3 col-lg-3']/article") ;


        if (!items.isEmpty()) {
            // Iterate over all elements
            for (HtmlElement item : items) {

                HtmlAnchor itemAnchor = ((HtmlAnchor) item.getFirstByXPath(".//h3/a"));


                HtmlElement starRatingElement = item.getFirstByXPath(".//p");
                String starRating = starRatingElement.getAttribute("class");
                String itemName = itemAnchor.getAttribute("title");
                String itemUrl =  itemAnchor.getHrefAttribute();


                if(meetsThreshold(starRating, ratingThreshold)) {
                    Book bookObject = new Book(itemName, itemUrl, starRating);
                    bookStack.add(bookObject);

                }
            }
        }
        else {
            System.out.println("No items found !");
        }

        System.out.println("Page Finished");
    }


    //returns true if there is no next page button
    public static boolean noNextPage(String url) throws IOException {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        HtmlPage page = client.getPage(url);
        if(page.getFirstByXPath("//li[@class='next']")!= null){
            return false;
        }
        return true;
    }

    //checks if a book is rated highly enough for the user's list, returns boolean value
    public static boolean meetsThreshold(String starRating, String ratingThreshold)
    {
        if(ratingThreshold == "one"){
            if(starRating.equals("star-rating One") || starRating.equals("star-rating Two") || starRating.equals("star-rating Three") ||starRating.equals("star-rating Four") || starRating.equals("star-rating Five")){
                return true;
            }
            return false;
        }
        if(ratingThreshold == "two"){
            if(starRating.equals("star-rating Two") || starRating.equals("star-rating Three") ||starRating.equals("star-rating Four") || starRating.equals("star-rating Five")){
                return true;
            }
            return false;
        }
        if(ratingThreshold == "three"){
            if(starRating.equals("star-rating Three") ||starRating.equals("star-rating Four") || starRating.equals("star-rating Five")){
                return true;
            }
            return false;
        }
        if(ratingThreshold == "four"){
            if(starRating.equals("star-rating Four") || starRating.equals("star-rating Five")){
                return true;
            }
            return false;
        }
        if(ratingThreshold == "five"){
            if(starRating.equals("star-rating Five")){
                return true;
            }
            return false;
        }
        return false;
    }
}