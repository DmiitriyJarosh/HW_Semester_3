package com.company;

import static com.company.Main.NUM_OF_THREADS;

public class ParalSummator implements Runnable {
    private int[] A;
    private int[] B;
    private int[] res;
    private int[] precount;
    private int start;
    private int[] share;
    private int[] shareRes;
    private int num;
    private int finish;
    private boolean[] finishedFlags;

    public ParalSummator(int[] A, int[] B, int[] precount, int[] res, int[] share, int[] shareRes, int start, int finish, int num, boolean[] finishFlags) {
        this.A = A;
        this.B = B;
        this.shareRes = shareRes;
        this.finishedFlags = finishFlags;
        this.precount = precount;
        this.res = res;
        this.share = share;
        this.start = start;
        this.num = num;
        this.finish = finish;
    }

    private void preCount() {
        for (int i = start; i < finish; i++) {
            if ((A[i] + B[i]) > 9) {
                precount[i + 1] = 2;
            } else if ((A[i] + B[i]) == 9) {
                precount[i + 1] = 1;
            } else {
                precount[i + 1] = 0;
            }
        }
        if (num != NUM_OF_THREADS) {
            share[num] = precount[finish];
        }
    }

    private int operator(int a, int b) {
        int res = a + b;
        if (res > 2) {
            res = 2;
        }
        if ((a == 0 && b == 1) || (a == 1 && b ==0) || (a == 2 && b == 0)) {
            res = 0;
        }
        if ((a == 1 && b == 1)) {
            res = 1;
        }
        return res;
    }

    private void carryCount(int carry) {
        if (carry == 1) {
            while (shareRes[num - 1] == -1) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            carry = shareRes[num - 1];
        }
        for (int i = start; i < finish; i++) {
            precount[i + 1] = operator(carry, precount[i + 1]);
            carry = precount[i + 1];
        }
    }

    private void count() {
        for (int i = start; i < finish; i++) {
            res[i] = (A[i] + B[i] + (precount[i] == 2 ? 1 : 0)) % 10;
        }
        shareRes[num] = A[finish - 1] + B[finish - 1] + (precount[finish - 1] == 2 ? 1 : 0) >= 10 ? 2 : 0;
    }


    public void run() {
        preCount();
        int tmp;
        if (num != 0) {
            while (share[num - 1] == -1) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tmp = share[num - 1];
        } else {
            tmp = 0;
        }
        carryCount(tmp);
        count();
        finishedFlags[num] = true;
    }
}
