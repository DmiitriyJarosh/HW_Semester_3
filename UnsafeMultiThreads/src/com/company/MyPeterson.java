package com.company;

import java.util.ArrayDeque;
import java.util.Queue;

public class MyPeterson {
    private boolean[] wantFlags;
    private int waiting;
    private int numOfThreads;

    public MyPeterson (int numOfThreads) {
        waiting = -1;
        this.numOfThreads = numOfThreads;
        wantFlags = new boolean[numOfThreads];
        for (int i = 0; i < numOfThreads; i++) {
            wantFlags[i] = false;
        }
    }

    void lock(int threadID) {
        int other = 1 - threadID;
        wantFlags[threadID] = true;
        waiting = threadID;
        while (wantFlags[other] && waiting == threadID) {
            //waiting
        }
    }

    void unlock(int threadID) {
        wantFlags[threadID] = false;
    }
}
