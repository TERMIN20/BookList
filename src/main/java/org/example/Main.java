package org.example;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/*
This code uses the HtmlUnit package under the Apache License, Version 2.0
*/


public class Main {
    public static void main(String[] args) throws IOException {
        String query  = "turtle";
        String category = "_1";

        // Instantiate the client

        // Set up the URL with the search term and send the request
        String searchUrl = "http://books.toscrape.com/catalogue/category/books" + category + "/index.html";
        getPage(searchUrl);

    }

    public static void getPage(String url) throws IOException {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        HtmlPage page = client.getPage(url);
        System.out.println(page.asXml());
        List<HtmlElement> items = page.getByXPath("//li[@class='col-xs-6 col-sm-4 col-md-3 col-lg-3']/article") ;
        System.out.println(items);
        if (!items.isEmpty()) {
            // Iterate over all elements
            for (HtmlElement item : items) {

                HtmlAnchor itemAnchor = ((HtmlAnchor) item.getFirstByXPath(".//p"));

                // Get the price from <a><span class="result-price"></span></a>
                HtmlElement spanPrice = ((HtmlElement) item.getFirstByXPath(".//p/span[@class='cl-search-result cl-search-view-mode-list']")) ;

                String itemName = itemAnchor.asNormalizedText();
                String itemUrl =  itemAnchor.getHrefAttribute();


            }
        }
        else {
            System.out.println("No items found !");
        }
    }
}