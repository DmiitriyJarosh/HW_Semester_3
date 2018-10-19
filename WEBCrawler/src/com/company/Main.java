package com.company;

import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static final int NUM_OF_THREADS = 2;
    public static final int NUM_OF_DEPTH = 1;

    public static void main(String[] args) {
        Set <String> pagesVisited = new ConcurrentSkipListSet <>();
        Queue <Pair<String, Integer>> pagesToVisit = new ConcurrentLinkedQueue <>();
        boolean[] flags = new boolean[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            flags[i] = false;
        }
        pagesToVisit.add(new Pair<>("http://www.shaderx.com/", 0));
        Crawler[] crawlers = new Crawler[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            crawlers[i] = new Crawler(pagesVisited, pagesToVisit, flags, i);
        }
        System.out.println("Enter something when all " + NUM_OF_THREADS + " threads finished!");
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            Thread t = new Thread(crawlers[i]);
            t.start();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            crawlers[i].CrawlerOff();
        }
    }
}
