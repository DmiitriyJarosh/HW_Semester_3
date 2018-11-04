package com.company;

public class ParalSummator implements Runnable {
    private int[] a;
    private int[] b;
    private int[] resA;
    private int[] resB;
    private int start;
    private int[] tmpB;
    private int[] shareA;
    private Object lock;
    private int[] shareB;
    private int num;
    private boolean[] finishFlags;
    private int finish;

    public ParalSummator(int[] a, int[] b, int[] resA, int[] resB, int[] shareA, int[] shareB, int[] tmpB, int start, int finish, int num, Object lock, boolean[] finishFlags) {
        this.a = a;
        this.b = b;
        this.lock = lock;
        this.finishFlags = finishFlags;
        this.tmpB = tmpB;
        this.resA = resA;
        this.resB = resB;
        this.shareA = shareA;
        this.shareB = shareB;
        this.start = start;
        this.num = num;
        this.finish = finish;
    }

    private void preCount() {
        resA[start] = a[start];
        resB[start] = b[start];
        for (int i = start + 1; i <= finish; i++) {
            resA[i] = a[i] * resA[i - 1];
            resB[i] = a[i] * resB[i - 1] + b[i];
            //System.out.println(i + ":! " + resA[i] + " " + resB[i]);
        }
        shareA[num] = resA[finish - 1];
        shareB[num] = resB[finish - 1];
    }

    private void variableCount() {
        int tmpB = b[0];
        Object waiter = new Object();
        boolean flag = true;
        for (int i = 0; i < num; i++) {
            while (true) {
                try {
                    synchronized (lock) {
                        if (shareA[i] != -1 && shareB[i] != -1) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        synchronized (waiter) {
                            waiter.wait(10);
                        }
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tmpB = shareA[i] * tmpB + shareB[i];
        }
        this.tmpB[num] = tmpB;
    }

    public void run() {
        preCount();
        /*for (int i = start + 1; i <= finish; i++) {
            System.out.println(i + ": " + resA[i] + " " + resB[i]);
        }*/
        variableCount();
        //System.out.println("Thread " + num + " finished!");
        finishFlags[num] = true;
        //System.out.println(num + ": " + shareA[num] + " " + shareB[num] + " " + tmpB[num]);
    }
}
