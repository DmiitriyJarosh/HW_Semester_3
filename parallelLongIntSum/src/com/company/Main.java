package com.company;

import java.util.Scanner;

public class Main {
    public static int NUM_OF_NUMBERS;
    public static int NUM_OF_THREADS = 4;
    public static int STEP;
    public static volatile Carry[] shareCarry;
    static volatile Carry[] carries;
    static volatile int counter;

    public static void main(String[] args)  {
        Scanner sc = new Scanner(System.in);
        String A = sc.nextLine();
        String B = sc.nextLine();
        NUM_OF_NUMBERS = Math.max(A.length(), B.length()) + 1;
        STEP = NUM_OF_NUMBERS / NUM_OF_THREADS;

        int[] bigIntA = new int[NUM_OF_NUMBERS];
        int[] bigIntB = new int[NUM_OF_NUMBERS];
        for (int i = 0; i < NUM_OF_NUMBERS; i++) {
            bigIntA[i] = 0;
            bigIntB[i] = 0;
        }

        for (int i = 0; i < A.length(); i++) {
            bigIntA[NUM_OF_NUMBERS - i - 1] = A.charAt(A.length() - i - 1) - '0';
        }
        bigIntA = reverse(bigIntA);

        for (int i = 0; i < B.length(); i++) {
            bigIntB[NUM_OF_NUMBERS - i - 1] = B.charAt(B.length() - i - 1) - '0';
        }
        bigIntB = reverse(bigIntB);
        try {
            int[] res = multiSum(bigIntA, bigIntB);
            System.out.print("ParalSum res: ");
            printLong(res);
            res = sum (bigIntA, bigIntB);
            System.out.print("SingleSum res: ");
            printLong(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int[] multiSum(int[] bigIntA, int[] bigIntB) throws InterruptedException {

        if (NUM_OF_NUMBERS < NUM_OF_THREADS) {
            return sum(bigIntA, bigIntB);
        }

        Object sync = new Object();
        Thread[] threads = new Thread[NUM_OF_THREADS];
        carries = new Carry[NUM_OF_NUMBERS];
        int[] res = new int[NUM_OF_NUMBERS];
        shareCarry = new Carry[NUM_OF_THREADS];

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            if (i == NUM_OF_THREADS - 1) {
                threads[i] = new Thread(new ParalSum(i * STEP, NUM_OF_NUMBERS, bigIntA, bigIntB, carries, res, sync));
            } else {
                threads[i] = new Thread(new ParalSum(i * STEP, STEP * (i + 1), bigIntA, bigIntB, carries, res, sync));
            }
            threads[i].start();
        }

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            threads[i].join();
        }

        return res;
    }

    public static void setNumOfThreads(int n) {
        NUM_OF_THREADS = n;
    }

    public static void setSTEP(int STEP) {
        Main.STEP = STEP;
    }

    public static void setNumOfNumbers(int numOfNumbers) {
        NUM_OF_NUMBERS = numOfNumbers;
    }

    public static int[] testMulti(int[] A, int[] B) {
        try {
            return multiSum(A, B);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[] testSingle(int[] A, int[] B) {
        return sum(A, B);
    }

    public static int[] sum(int[] A, int[] B) {
        int carry = 0;
        int[] sum = new int[NUM_OF_NUMBERS];
        for (int i = 0; i < NUM_OF_NUMBERS; i++) {
            sum[i] = (A[i] + B[i] + carry) % 10;
            carry = (A[i] + B[i] + carry) / 10;
        }
        return sum;
    }

    private static void printLong(int[] A) {
        boolean flag = false;
        for (int i = A.length - 1; i >= 0; i--) {
            if (flag) {
                System.out.print(A[i]);
            } else if (A[i] != 0 && !flag) {
                flag = true;
                System.out.print(A[i]);
            }
        }
        System.out.println();
    }

    public static int[] reverse(int[] A) {
        int tmp;
        for (int i = 0; i < ((NUM_OF_NUMBERS % 2 == 0) ? NUM_OF_NUMBERS / 2 : NUM_OF_NUMBERS / 2 + 1); i++) {
            tmp = A[i];
            A[i] = A[NUM_OF_NUMBERS - i - 1];
            A[NUM_OF_NUMBERS - i - 1] = tmp;
        }
        return A;
    }
}
