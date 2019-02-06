package com.company;

import java.util.Scanner;

public class Main {

    public static int NUM_OF_THREADS = 4;


    public static void setNumOfThreads(int n) {
        NUM_OF_THREADS = n;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        System.out.println(singleAlgo(input));
        System.out.println(multiAlgo(input));
    }

    public static boolean singleAlgo(String input) {
        int tmp = 0;
        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '(':
                    tmp++;
                    break;
                case ')':
                    if (tmp == 0) {
                        return false;
                    } else {
                        tmp--;
                    }
            }
        }
        return (tmp == 0);
    }

    public static boolean multiAlgo(String input) {
        int[] shareR = new int[NUM_OF_THREADS + 1];
        boolean[] finishFlags = new boolean[NUM_OF_THREADS + 1];
        int[] shareL = new int[NUM_OF_THREADS + 1];
        int[] shareFlags = new int[NUM_OF_THREADS + 1];

        Object lock = new Object();
        ParalSummator[] summators = new ParalSummator[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            shareFlags[i] = 0;
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            finishFlags[i] = false;
            summators[i] = new ParalSummator(input, shareL, shareR, shareFlags, i * (input.length() / NUM_OF_THREADS), (i + 1) * (input.length() / NUM_OF_THREADS), i, lock, finishFlags);
            Thread t = new Thread(summators[i]);
            t.start();
        }
        if (input.length() % NUM_OF_THREADS != 0) {
            finishFlags[NUM_OF_THREADS] = false;
            ParalSummator summator = new ParalSummator(input, shareL, shareR, shareFlags, NUM_OF_THREADS * (input.length() / NUM_OF_THREADS), input.length(), NUM_OF_THREADS, lock, finishFlags);
            summator.preCount();
            Object obj = new Object();
            while (shareFlags[0] != (NUM_OF_THREADS * 2)) {
                synchronized (obj) {
                    try {
                        obj.wait(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (shareL[0] > shareR[NUM_OF_THREADS]) {
                shareL[0] -= shareR[NUM_OF_THREADS];
                shareR[NUM_OF_THREADS] = 0;
                shareL[0] += shareL[NUM_OF_THREADS];
            } else {
                shareR[NUM_OF_THREADS] -= shareL[0];
                shareL[0] = shareL[NUM_OF_THREADS];
            }
            shareR[0] += shareR[NUM_OF_THREADS];
            finishFlags[NUM_OF_THREADS] = true;
        } else {
            finishFlags[NUM_OF_THREADS] = true;
        }


        boolean flag;
        do {
            flag = true;
            for (boolean flg : finishFlags) {
                if (!flg) {
                    flag = false;
                }
            }
        } while (!flag);
        return (shareL[0] == 0 && shareR[0] == 0);
    }

}
