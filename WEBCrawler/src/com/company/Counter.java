package com.company;

public class Counter {
    private int count;

    public Counter() {
        count = 0;
    }

    public synchronized void inc() {
        count++;
    }

    public synchronized void dec() {
        count--;
    }

    public synchronized int getCount() {
        return count;
    }
}
