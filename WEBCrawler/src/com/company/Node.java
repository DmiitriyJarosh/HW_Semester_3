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
        lock.lock();
        Node tmp = next;
        lock.unlock();
        return tmp;
    }

    public T getValue() {
        lock.lock();
        T tmp = value;
        lock.unlock();
        return tmp;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node getPrev() {
        lock.lock();
        Node tmp = prev;
        lock.unlock();
        return tmp;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
