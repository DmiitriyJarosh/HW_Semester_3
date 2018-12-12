package com.company;


public class ParalSum implements Runnable {
    private int start;
    private int finish;
    private int[] bigIntA;
    private int[] bigIntB;
    private Carry[] carries;
    private Carry carryShare;
    private Object sync;
    private int[] res;

    public ParalSum(int start, int finish, int[] bigIntA, int[] bigIntB, Carry[] carries, int[] res, Object sync) {
        this.start = start;
        this.finish = finish;
        this.res = res;
        carryShare = Carry.M;
        this.sync = sync;
        this.bigIntA = bigIntA;
        this.bigIntB = bigIntB;
        this.carries = carries;
    }

    public static Carry operator(Carry a, Carry b) {
        if (b != Carry.M) {
            return b;
        } else {
            return a;
        }
    }

    private void waitIterationFinish() {
        synchronized (sync) {
            Main.counter++;
            if (Main.counter == Main.NUM_OF_THREADS) {
                Main.counter = 0;
                sync.notifyAll();
            } else {
                try {
                    sync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        int sum = 0;
        Carry finalCarryToThread;
        for (int i = start; i < finish; i++) {
            sum = bigIntA[i] + bigIntB[i];
            if (sum < 9) {
                carries[i] = Carry.N;
            } else if (sum == 9) {
                carries[i] = Carry.M;
            } else {
                carries[i] = Carry.C;
            }
            carryShare = operator(carryShare, carries[i]);
        }
        
        
        Main.shareCarry[start/Main.STEP] = carryShare;

        waitIterationFinish();

        if (start == 0) {
            PrefScan head = new PrefScan(1, Main.NUM_OF_THREADS);
            head.createTree(Main.shareCarry, 1, Main.NUM_OF_THREADS);
            head.up();
            head.down(Carry.M);
        }

        waitIterationFinish();

        if (start != 0) {
            finalCarryToThread = Main.shareCarry[start / Main.STEP];
        } else {
            finalCarryToThread = Carry.N;
        }

        carries[start] = operator(finalCarryToThread, carries[start]);
        for (int i = start + 1; i < finish; i++) {
            carries[i] = operator(carries[i - 1], carries[i]);
        }

        waitIterationFinish();

        for (int i = start; i < finish; i++) {
            if (i != 0) {
                res[i] = (bigIntA[i] + bigIntB[i] + (Main.carries[i - 1] == Carry.C ? 1 : 0)) % 10;
            } else {
                res[i] = (bigIntA[i] + bigIntB[i]) % 10;
            }
        }
    }
}
