package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static int NUM_OF_THREADS = 4;
    public static final int NUM_OF_STEPS = 1000000;

    public static void setNumOfThreads(int numOfThreads) {
        NUM_OF_THREADS = numOfThreads;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] angles = new int[NUM_OF_STEPS];
        int[] steps = new int[NUM_OF_STEPS];
        for (int i = 0; i < NUM_OF_STEPS; i++) {
            angles[i] = sc.nextInt();
            steps[i] = sc.nextInt();
        }
        double[] single = singleAlgo(angles, steps);
        double[] multi = multiAlgo(angles, steps);
        System.out.println(single[0] + " " + single[1]);
        System.out.println(multi[0] + " " + multi[1]);
    }

    public static double[] singleAlgo(int[] angles, int[] steps) {
        double x = 0, y = 0;
        int angle = 0;
        for (int i = 0; i < NUM_OF_STEPS; i++) {
            angle = (angle + angles[i]) % 360;
            x += Math.cos(Math.toRadians(angle)) * steps[i];
            y += Math.sin(Math.toRadians(angle)) * steps[i];
        }
        return new double[] {x, y};
    }

    public static double[] multiAlgo(int[] angles, int[] steps) {
        int[] shareAng = new int[NUM_OF_THREADS + 1];
        double[] shareX = new double[NUM_OF_THREADS + 1];
        boolean[] finishFlags = new boolean[NUM_OF_THREADS + 1];
        double[] shareY = new double[NUM_OF_THREADS + 1];
        int[] shareFlags = new int[NUM_OF_THREADS + 1];
        ParalSummator[] summators = new ParalSummator[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            shareFlags[i] = 0;
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            finishFlags[i] = false;
            summators[i] = new ParalSummator(angles, steps, shareAng, shareX, shareY, shareFlags, i * (NUM_OF_STEPS / NUM_OF_THREADS), (i + 1) * (NUM_OF_STEPS / NUM_OF_THREADS), i, finishFlags);
            Thread t = new Thread(summators[i]);
            t.start();
        }
        if (NUM_OF_STEPS % NUM_OF_THREADS != 0) {
            finishFlags[NUM_OF_THREADS] = false;
            ParalSummator summator = new ParalSummator(angles, steps, shareAng, shareX, shareY, shareFlags, NUM_OF_THREADS * (NUM_OF_STEPS / NUM_OF_THREADS), NUM_OF_STEPS, NUM_OF_THREADS, finishFlags);
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
            shareX[0] += shareX[NUM_OF_THREADS] * Math.cos(Math.toRadians(shareAng[0])) - shareY[NUM_OF_THREADS] * Math.sin(Math.toRadians(shareAng[0]));
            shareY[0] += shareX[NUM_OF_THREADS] * Math.sin(Math.toRadians(shareAng[0])) + shareY[NUM_OF_THREADS] * Math.cos(Math.toRadians(shareAng[0]));
            shareAng[0] = (shareAng[0] + shareAng[NUM_OF_THREADS]) % 360;
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

        return new double[] {shareX[0], shareY[0]};
    }

}
