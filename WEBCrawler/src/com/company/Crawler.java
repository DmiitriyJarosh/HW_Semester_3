package com.company;

import javafx.util.Pair;

import java.util.Queue;
import java.util.Set;

import static com.company.Main.NUM_OF_DEPTH;

public class Crawler implements Runnable {

    private Set<String> pagesVisited;
    private boolean[] flags;
    private int num;
    private Queue<Pair<String, Integer>> pagesToVisit;
    private boolean flagActive;

    public Crawler(Set<String> pagesVisited, Queue<Pair<String, Integer>> pagesToVisit, boolean[] flags, int num) {
        this.pagesToVisit = pagesToVisit;
        this.pagesVisited = pagesVisited;
        this.flagActive = true;
        this.flags = flags;
        this.num = num;
    }

    private boolean checkFlags() {
        for (boolean flag : flags) {
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    public void CrawlerOff() {
        flagActive = false;
    }

    public Pair<String, Integer> getNextURL() {
        Pair<String, Integer> url = pagesToVisit.poll();
        if (url == null) {
            return null;
        }
        while (pagesVisited.contains(url.getKey())) {
            url = pagesToVisit.poll();
            if (url == null) {
                return null;
            }
        }
        return url;
    }

    public void search() {
        while (flagActive || !checkFlags()) {
            CrawlerTech crawlerTech = new CrawlerTech();
            Pair<String, Integer> url = getNextURL();
            if (url == null || url.getValue() > NUM_OF_DEPTH) {
                flags[num] = true;
                continue;
            }
            flags[num] = false;
            crawlerTech.crawl(url);
            pagesToVisit.addAll(crawlerTech.getLinks());
            crawlerTech.saveToDisk(url.getKey());
        }
    }

    public void run() {
        search();
        System.out.println("Thread finished!");
    }
}
