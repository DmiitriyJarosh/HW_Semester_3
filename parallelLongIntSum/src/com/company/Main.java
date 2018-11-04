package com.company;

import java.util.Scanner;

public class Main {

    public static final int NUM_OF_THREADS = 4;
    public static final  int NUM_OF_NUMBERS = 10000;

    public static void main(String[] args) {
	    int[] bigIntA = new int[NUM_OF_NUMBERS];
	    int[] bigIntB = new int[NUM_OF_NUMBERS];
	    for (int i = 0; i < NUM_OF_NUMBERS; i++) {
	        bigIntA[i] = 0;
	        bigIntB[i] = 0;
        }
        Scanner sc = new Scanner(System.in);
	    String A = sc.nextLine();
	    for (int i = 0; i < A.length(); i++) {
	        bigIntA[NUM_OF_NUMBERS - i - 1] = A.charAt(A.length() - i - 1) - '0';
        }
        bigIntA = reverse(bigIntA);
	    String B = sc.nextLine();
        for (int i = 0; i < B.length(); i++) {
            bigIntB[NUM_OF_NUMBERS - i - 1] = B.charAt(B.length() - i - 1) - '0';
        }
        bigIntB = reverse(bigIntB);
        int[] bigIntSingleRes = sum(bigIntA, bigIntB);
        int[] bigIntParallelRes = sumParallel(bigIntA, bigIntB, NUM_OF_THREADS);
        System.out.println(LongIntToStr(bigIntParallelRes));
        System.out.println(LongIntToStr(bigIntSingleRes));
    }

    public static int[] sum(int[] A, int[] B) {
        int carry = 0;
        int[] sum = new int[NUM_OF_NUMBERS + 1];
        for (int i = 0; i < NUM_OF_NUMBERS; i++) {
            sum[i] = (A[i] + B[i] + carry) % 10;
            carry = (A[i] + B[i] + carry) / 10;
        }
        return sum;
    }

    private static String LongIntToStr(int[] A) {
        boolean flag = false;
        A = reverse(A);
        String res = "";
        for (int i = 0; i < NUM_OF_NUMBERS; i++) {
            if (A[i] != 0) {
                flag = true;
            }
            if (flag) {
                res += A[i];
            }
        }
        return res;
    }

    public static int[] reverse(int[] A) {
        for (int i = 0; i < 50; i++) {
            int tmp = A[i];
            A[i] = A[NUM_OF_NUMBERS - i - 1];
            A[NUM_OF_NUMBERS - i - 1] = tmp;
        }
        return A;
    }

    public static int[] sumParallel(int[] A, int[] B, int NUM_OF_THREADS) {
        int[] res = new int[NUM_OF_NUMBERS + 1];
        int[] share = new int[NUM_OF_NUMBERS];
        boolean[] finishFlags = new boolean[NUM_OF_THREADS + 1];
        int[] precount = new int[NUM_OF_NUMBERS + 1];
        ParalSummator[] summators = new ParalSummator[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_NUMBERS; i++) {
            share[i] = -1;
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            finishFlags[i] = false;
            summators[i] = new ParalSummator(A, B, precount, res, share, i * (NUM_OF_NUMBERS / NUM_OF_THREADS), (i + 1) * (NUM_OF_NUMBERS / NUM_OF_THREADS), i, finishFlags);
            Thread t = new Thread(summators[i]);
            t.start();
        }
        if (NUM_OF_NUMBERS % NUM_OF_THREADS != 0) {
            finishFlags[NUM_OF_THREADS] = false;
            ParalSummator summator = new ParalSummator(A, B, precount, res, share, NUM_OF_THREADS * (NUM_OF_NUMBERS / NUM_OF_THREADS), NUM_OF_NUMBERS, NUM_OF_THREADS, finishFlags);
            summator.run();
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

        return res;
    }

}
