package com.company;


import static com.company.ParalSum.operator;

public class PrefScan {
    private PrefScan left;
    private PrefScan right;
    private Carry value;
    private int start;
    private int finish;

    public PrefScan(int start, int finish) {
        this.start = start;
        this.finish = finish;
    }

    public Carry getValue() {
        return value;
    }

    public void createTree(Carry[] array, int start, int finish) {
        if (start == finish) {
            this.value = array[start - 1];
        } else {
            int middle = start + (finish - start + 1) / 2 - 1;
            left = new PrefScan(start, middle);
            right = new PrefScan(middle + 1, finish);
            Thread t = new Thread(() -> left.createTree(array, start, middle));
            t.start();
            right.createTree(array, middle + 1, finish);
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Carry up() {
        if (value != null) {
            return value;
        } else {
            Up runLeft = new Up(left);
            Up runRight = new Up(right);
            Thread t = new Thread(runLeft);
            t.start();
            runRight.run();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value = operator(runLeft.getValue(), runRight.getValue());
            return value;
        }
    }

    public void down(Carry carry) {
        value = carry;
        if (start == finish) {
            Main.shareCarry[start - 1] = value;
            return;
        }
        Carry newValue = operator(value, left.getValue());
        Thread t = new Thread(() -> left.down(value));
        t.start();
        right.down(newValue);
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Up implements Runnable {
    private PrefScan prefScan;
    private Carry value;

    public Up(PrefScan prefScan) {
        this.prefScan = prefScan;
    }
    @Override
    public void run() {
        value = prefScan.up();
    }

    public Carry getValue() {
        return value;
    }
}
