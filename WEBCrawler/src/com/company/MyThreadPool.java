package com.company;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class MyThreadPool implements Executor, AutoCloseable {
    private final Queue<Runnable> workQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean isRunning = true;
    private Thread[] threads;
    //private static final int NUM_OF_THREADS = 4;

    public MyThreadPool(int nThreads) {
        threads = new Thread[nThreads];
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(new TaskWorker());
            threads[i].start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (isRunning) {
            workQueue.offer(command);
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    private final class TaskWorker implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                Runnable nextTask = workQueue.poll();
                if (nextTask != null) {
                    nextTask.run();
                }
            }
        }
    }

    @Override
    public void close() {
        shutdown();
    }
}
