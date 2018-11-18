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
        MyConcurencyListSet<String> pagesVisited = new MyConcurencyListSet<>();
        String url = "http://www.shaderx.com/";
        //final ExecutorService ex = Executors.newFixedThreadPool(NUM_OF_THREADS);
        //final ExecutorService sd = Executors.newFixedThreadPool(NUM_OF_THREADS);
        MyThreadPool ex = new MyThreadPool(NUM_OF_THREADS);
        MyThreadPool sd = new MyThreadPool(NUM_OF_THREADS);
        Counter cntEx = new Counter();
        Counter cntSd = new Counter();
        cntEx.inc();
        ex.execute(new Crawler(url, 0, pagesVisited, ex, sd, cntEx, cntSd));
        while (cntEx.getCount() != 0 || cntSd.getCount() != 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ex.shutdown();
        sd.shutdown();
    }


    public static void CrawlTest(String url, int NUM_OF_THREADS, MyConcurencyListSet<String> pagesVisited) {
        MyThreadPool ex = new MyThreadPool(NUM_OF_THREADS);
        MyThreadPool sd = new MyThreadPool(NUM_OF_THREADS);
        Counter cntEx = new Counter();
        Counter cntSd = new Counter();
        cntEx.inc();
        ex.execute(new Crawler(url, 0, pagesVisited, ex, sd, cntEx, cntSd));
        while (cntEx.getCount() != 0 || cntSd.getCount() != 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ex.shutdown();
        sd.shutdown();
    }
}
