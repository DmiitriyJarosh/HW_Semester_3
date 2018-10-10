package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static final int NUM_OF_THREADS = 4;
    public static final  int N = 4;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int[] a = new int[N + 1];
        int[] b = new int[N + 1];
        a[0] = 0;
        for (int i = 0; i < N + 1; i++) {
            if (i != 0) {
                a[i] = sc.nextInt();
            }
            b[i] = sc.nextInt();
        }

        int singleRes = singleAlgo(a, b);
        System.out.println(singleRes);
        int multiRes = multiAlgo(a, b);
        System.out.println(multiRes);
    }

    private static int singleAlgo(int[] a, int[] b) {
        int x = b[0];
        for (int i = 1; i < N; i++) {
            x = a[i] * x + b[i];
        }
        return x;
    }

    private static int multiAlgo(int[] a, int[] b) {
        int[] shareA = new int[NUM_OF_THREADS];
        int[] shareB = new int[NUM_OF_THREADS];
        int[] resA = new int[N + 1];
        Object lock = new Object();
        int[] resB = new int[N + 1];
        resB[0] = b[0];
        resA[0] = 0;
        int[] tmpB = new int[NUM_OF_THREADS];
        ParalSummator[] summators = new ParalSummator[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            shareA[i] = -1;
            shareB[i] = -1;
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            summators[i] = new ParalSummator(a, b, resA, resB, shareA, shareB, tmpB, i * (N / NUM_OF_THREADS), (i + 1) * (N / NUM_OF_THREADS), i, lock);
            Thread t = new Thread(summators[i]);
            t.start();
        }
        if (N % NUM_OF_THREADS != 0) {
            ParalSummator summator = new ParalSummator(a, b, resA, resB, shareA, shareB, tmpB, NUM_OF_THREADS * (N / NUM_OF_THREADS), N, NUM_OF_THREADS, lock);
            summator.run();
        }
        try {
            System.out.println("Enter smth when all threads finish!");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int tmp = N % (N / NUM_OF_THREADS) == 0 ? N / (N / NUM_OF_THREADS) - 1 : N / (N / NUM_OF_THREADS);
        //System.out.println(tmp);
        return resA[N - 1] * tmpB[tmp] + resB[N - 1];
    }

}
