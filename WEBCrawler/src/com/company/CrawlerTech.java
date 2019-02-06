package com.company;

import javafx.util.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;



public class CrawlerTech {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<Pair<String, Integer>> links = new LinkedList<>();
    private Document htmlDocument;

    public void crawl(Pair<String, Integer> url) {
        try
        {
            Connection connection = Jsoup.connect(url.getKey()).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;

            System.out.println("Received web page at " + url.getKey());

            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for (Element link : linksOnPage)
            {
                links.add(new Pair<>(link.absUrl("href"), url.getValue() + 1));
            }
        }
        catch(IOException e)
        {
            System.out.println("Error web page at " + url.getKey());
        }
    }

    public void saveToDisk(String url) {
        if (htmlDocument == null) {
            return;
        }
        String tmp = htmlDocument.outerHtml();

        try (FileWriter writer = new FileWriter("/home/adminlinux/Desktop/" + url.replaceAll(":", "_").replaceAll("/", "_").replaceAll("\\.", "_") + ".html", false))
        {
            writer.write(tmp);
            writer.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pair<String, Integer>> getLinks() {
        return links;
    }
}
