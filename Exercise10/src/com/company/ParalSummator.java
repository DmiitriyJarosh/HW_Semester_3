package com.company;

import static com.company.Main.NUM_OF_THREADS;

public class ParalSummator implements Runnable {
    private int start;
    private int[] angles;
    private int[] steps;
    private double[] shareX;
    private boolean[] finishFlags;
    private double[] shareY;
    private int[] shareAng;
    private int[] shareFlags;
    private int num;
    private int finish;

    public ParalSummator(int[] angles, int[] steps, int[] shareAng, double[] shareX, double[] shareY, int[] shareFlags, int start, int finish, int num, boolean[] finishFlags) {
        this.shareAng = shareAng;
        this.finishFlags = finishFlags;
        this.angles = angles;
        this.steps = steps;
        this.shareX = shareX;
        this.shareY = shareY;
        this.shareFlags = shareFlags;
        this.start = start;
        this.num = num;
        this.finish = finish;
    }

    public void preCount() {
        double x = 0, y = 0;
        int angle = 0;
        for (int i = start; i < finish; i++) {
            angle = (angle + angles[i]) % 360;
            x += Math.cos(Math.toRadians(angle)) * steps[i];
            y += Math.sin(Math.toRadians(angle)) * steps[i];
        }
        shareAng[num] = angle;
        shareX[num] = x;
        shareY[num] = y;
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
                        obj.wait(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //System.out.println(num + ": " + shareX[num] + " " + shareY[num] + "  " + (num + i / 2) + ": " + shareX[num + i / 2] + " " + shareY[num + i / 2]);

            shareX[num] += shareX[num + i / 2] * Math.cos(Math.toRadians(shareAng[num])) - shareY[num + i / 2] * Math.sin(Math.toRadians(shareAng[num]));
            shareY[num] += shareX[num + i / 2] * Math.sin(Math.toRadians(shareAng[num])) + shareY[num + i / 2] * Math.cos(Math.toRadians(shareAng[num]));
            shareAng[num] = (shareAng[num] + shareAng[num + i / 2]) % 360;
            shareFlags[num] = i * 2;
            //System.out.println(num + ":::: " + shareX[num] + " " + shareY[num]);
        }


    }

    public void run() {
        preCount();
        convergence();
        finishFlags[num] = true;
        // System.out.println("Thread " + num + " finished!");
    }
}
