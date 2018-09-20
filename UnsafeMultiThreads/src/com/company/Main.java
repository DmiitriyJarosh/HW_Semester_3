package com.company;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {
        MyPeterson pet = new MyPeterson();
        Incrementer[] incrs = new Incrementer[5];
        Lock lock = new ReentrantLock();
        Counter count = new Counter(0);
        for (int i = 0; i < 2; i++) {
            incrs[i] = new Incrementer(count, lock, pet);
            Thread t = new Thread(incrs[i]);
            t.start();
        }
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        System.out.println(count.getCounter());
    }
}
