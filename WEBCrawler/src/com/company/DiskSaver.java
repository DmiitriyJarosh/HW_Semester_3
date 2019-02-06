package com.company;

import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;

public class DiskSaver implements Runnable {

    private Document webpage;
    private String url;
    private Counter cntSd;

    public DiskSaver(Document webpage, String url, Counter cntSd) {
        this.webpage = webpage;
        this.url = url;
        this.cntSd = cntSd;
    }

    public void run() {
        String tmp = webpage.outerHtml();
        try (FileWriter writer = new FileWriter("webpages/" + url.replaceAll(":", "_").replaceAll("/", "_").replaceAll("\\.", "_") + ".html", false))
        {
            writer.write(tmp);
            writer.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            cntSd.dec();
        }
    }
}
