package com.jenkov;


public class Incrementer implements Runnable {

    private Counter counter;
    private Lock lock;

    public Incrementer(Counter counter, Lock lock) {
        this.counter = counter;
        this.lock = lock;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            try {
                lock.lock();
                counter.incCounter();
            }
            finally {
                lock.unlock();
            }
        }
    }
}
