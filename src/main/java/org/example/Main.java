package org.example;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String searchQuery = "turtle";
        String city = "seattle";
        String category = "ccc";

        // Instantiate the client
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        // Set up the URL with the search term and send the request
        String searchUrl = "https://" + city + ".craigslist.org/search/" + category + "?query=" + URLEncoder.encode(searchQuery, "UTF-8");
        HtmlPage page = client.getPage(searchUrl);
        System.out.println(page.asXml());
        List<HtmlElement> items = page.getByXPath("//li[@class='result-row']") ;

        if (!items.isEmpty()) {
            // Iterate over all elements
            for (HtmlElement item : items) {

                HtmlAnchor itemAnchor = ((HtmlAnchor) item.getFirstByXPath(".//p[@class='cl-search-result cl-search-view-mode-list']/a"));

                // Get the price from <a><span class="result-price"></span></a>
                HtmlElement spanPrice = ((HtmlElement) item.getFirstByXPath(".//p/span[@class='cl-search-result cl-search-view-mode-list']")) ;

                String itemName = itemAnchor.asNormalizedText();
                String itemUrl =  itemAnchor.getHrefAttribute();



                System.out.println( String.format("Name : %s Url : %s ", itemName, itemUrl));

            }
        }
        else {
            System.out.println("No items found !");
        }
    }
}