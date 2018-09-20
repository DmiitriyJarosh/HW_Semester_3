package com.company;

public class Counter {
    private int counter;

    public Counter(int start) {
        counter = start;
    }

    public void incCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
