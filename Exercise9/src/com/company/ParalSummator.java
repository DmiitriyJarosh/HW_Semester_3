package com.company;

import static com.company.Main.NUM_OF_THREADS;

public class ParalSummator implements Runnable {
    private char[] input;
    private int start;
    private int[] shareR;
    private int[] shareL;
    private Object lock;
    private int[] shareFlags;
    private int num;
    private int finish;

    public ParalSummator(String input, int[] shareL, int[] shareR, int[] shareFlags, int start, int finish, int num, Object lock) {
        this.input = input.toCharArray();
        this.lock = lock;
        this.shareR = shareR;
        this.shareL = shareL;
        this.shareFlags = shareFlags;
        this.start = start;
        this.num = num;
        this.finish = finish;
    }

    public void preCount() {
        int l = 0;
        int r = 0;
        for (int i = start; i < finish; i++) {
            if (input[i] == '(') {
                l++;
            } else if (input[i] == ')') {
                if (l == 0) {
                    r++;
                } else {
                    l--;
                }
            }
        }
        shareR[num] = r;
        shareL[num] = l;
        shareFlags[num] = 2;
    }

    private void convergence() {
        for (int i = 2; i <= NUM_OF_THREADS; i *= 2) {
            if (num % i != 0) {
                return;
            }
            Object obj = new Object();
            while (shareFlags[num + i / 2] != i) {
                synchronized (obj){
                    try {
                        obj.wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
           // System.out.println(num + ": " + shareL[num] + " " + shareR[num] + "  " + (num + i / 2) + ": " + shareL[num + i / 2] + " " + shareR[num + i / 2]);

            if (shareL[num] >= shareR[num + i / 2]) {
                shareL[num] -= shareR[num + i / 2];
                shareR[num + i / 2] = 0;
                shareL[num] += shareL[num + i / 2];
            } else {
                shareR[num + i / 2] -= shareL[num];
                shareL[num] = shareL[num + i / 2];
            }
            shareR[num] += shareR[num + i / 2];
            shareFlags[num] = i * 2;
            //System.out.println(num + ":::: " + shareL[num] + " " + shareR[num]);
        }


    }

    public void run() {
        preCount();
        /*for (int i = start; i < finish; i++) {
            System.out.println(i + ": " + shareL[i] + " " + shareR[i]);
        }
        */
        convergence();
        System.out.println("Thread " + num + " finished!");
        //System.out.println(num + ": " + shareA[num] + " " + shareB[num] + " " + tmpB[num]);
    }
}
