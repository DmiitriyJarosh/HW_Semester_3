package com.company;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node <T> {
    private T value;
    private Lock lock;
    private Node next;
    private Node prev;

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public Node (T value) {
        this.value = value;
        this.lock = new ReentrantLock();
    }

    public Node getNext() {
        try {
            lock.lock();
            Node tmp = next;
            lock.unlock();
        }
        finally {
            lock.unlock();
        }
        return tmp;
    }

    public T getValue() {
        try {
            lock.lock();
            T tmp = value;
            lock.unlock();
        }
        finally {
            lock.unlock();
        }
        return tmp;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node getPrev() {
        try {
            lock.lock();
            Node tmp = prev;
            lock.unlock();
        }
        finally {
            lock.unlock();
        }
        return tmp;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
