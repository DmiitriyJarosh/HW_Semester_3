package com.jenkov;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Incrementer[] incrs = new Incrementer[4];
        Counter counter = new Counter(0);
        com.jenkov.Lock lock = new TAS();
        for (int i = 0; i < 4; i++) {
            incrs[i] = new Incrementer(counter, lock);
            Thread t = new Thread(incrs[i]);
            t.start();
        }
        try {
            System.in.read();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
