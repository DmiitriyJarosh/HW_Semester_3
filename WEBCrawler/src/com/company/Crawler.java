package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static com.company.Main.NUM_OF_DEPTH;

public class Crawler implements Runnable {

    private MyConcurencyListSet<String> pagesVisited;
    private MyThreadPool ex;
    private MyThreadPool sd;
    private String url;
    private Counter cntEx;
    private Counter cntSd;
    private int depth;
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    public Crawler(String url, int depth, MyConcurencyListSet<String> pagesVisited, MyThreadPool ex, MyThreadPool sd, Counter cntEx, Counter cntSd) {
        this.depth = depth;
        this.ex = ex;
        this.pagesVisited = pagesVisited;
        this.sd = sd;
        this.url = url;
        this.cntEx = cntEx;
        this.cntSd = cntSd;
    }

    public void crawl() {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            //System.out.println("Received web page at " + url);

            Elements linksOnPage = htmlDocument.select("a[href]");
            //System.out.println("Found (" + linksOnPage.size() + ") links");
            for (Element link : linksOnPage)
            {
                cntEx.inc();
                ex.execute(new Crawler(link.absUrl("href"), depth + 1, pagesVisited, ex, sd, cntEx, cntSd));
            }
            cntSd.inc();
            sd.execute(new DiskSaver(htmlDocument, url, cntSd));
        }
        catch(IOException e)
        {
            //System.out.println("Error web page at " + url);
        }
    }

    public void run() {

        if (url == null || depth > NUM_OF_DEPTH || pagesVisited.contains(url)) {
            cntEx.dec();
            return;
        }
        if (pagesVisited.add(url)) {
            crawl();
        }
        cntEx.dec();
    }
}
