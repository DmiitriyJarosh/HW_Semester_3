package com.company;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyConcurrentQueue<T> {

    private QueueNode<T> head;
    private QueueNode<T> tail;
    private Lock lock;

    public MyConcurrentQueue() {
        head = null;
        lock = new ReentrantLock();
        tail = null;
    }

    public void offer(T value) {
        QueueNode<T> newNode = new QueueNode<>(value);
        try {
            lock.lock();
            if (tail != null) {
                tail.setNext(newNode);
                newNode.setPrevious(tail);
                tail = newNode;
            } else {
                head = newNode;
                tail = newNode;
            }
        }
        finally {
            lock.unlock();
        }
    }

    public T poll() {
        QueueNode<T> res = null;
        try {
            lock.lock();
            if (head != null) {
                res = head;
                head = head.getNext();
                if (head != null) {
                    head.setPrevious(null);
                } else {
                    tail = null;
                }
            }
        }
        finally {
            lock.unlock();
        }
        if (res == null) {
            return null;
        } else {
            return res.getValue();
        }
    }
}
