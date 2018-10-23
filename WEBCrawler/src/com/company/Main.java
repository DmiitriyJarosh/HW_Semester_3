package com.company;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static final int NUM_OF_THREADS = 2;
    public static final int NUM_OF_DEPTH = 1;

    public static void main(String[] args) {
        //Set<String> pagesVisited = new ConcurrentSkipListSet<>();
        MyConcurencyListSet<String> pagesVisited = new MyConcurencyListSet<>();
        String url = "http://www.shaderx.com/";
        final ExecutorService ex = Executors.newFixedThreadPool(NUM_OF_THREADS);
        final ExecutorService sd = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ex.execute(new Crawler(url, 0, pagesVisited, ex, sd));
        try {
            System.in.read();
            ex.shutdown();
            sd.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
