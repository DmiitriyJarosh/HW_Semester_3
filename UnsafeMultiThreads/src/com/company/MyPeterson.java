package com.company;

import java.util.ArrayDeque;
import java.util.Queue;

public class MyPeterson {
    private boolean[] wantFlags;
    private int waiting;

    public MyPeterson () {
        waiting = -1;
        wantFlags = new boolean[2];
        for (int i = 0; i < 2; i++) {
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
