package com.company;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static final int NUM_OF_THREADS = 2;
    public static final int THREAD_LENGTH = 10;

    public static void main(String[] args) {

        MyPeterson pet = new MyPeterson(NUM_OF_THREADS);
        Incrementer[] incrs = new Incrementer[5];
        Lock lock = new ReentrantLock();
        Counter count = new Counter(0);
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            incrs[i] = new Incrementer(count, lock, pet);
            Thread t = new Thread(incrs[i]);
            t.start();
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter something");
        sc.nextLine();
        int tmp = NUM_OF_THREADS * THREAD_LENGTH;
        System.out.println("Right answer is " + tmp);
        System.out.println("Our answer is " + count.getCounter());
        if (tmp == count.getCounter()) {
            System.out.println("Our lock is working correctly!!!");
        }
    }
}
