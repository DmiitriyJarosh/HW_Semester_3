package com.company;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Incrementer implements Runnable {

    private Counter counter;
    private Lock lock;
    private MyPeterson pet;

    public Incrementer(Counter counter, Lock lock, MyPeterson pet) {
        this.counter = counter;
        this.pet = pet;
        this.lock = lock;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            /*try {
                if(lock.tryLock(10, TimeUnit.SECONDS)){
                    counter.incCounter();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            */
            pet.lock((int)Thread.currentThread().getId() % 2);
            counter.incCounter();
            pet.unlock((int)Thread.currentThread().getId() % 2);
        }
        System.out.println("end of thread");
    }
}
