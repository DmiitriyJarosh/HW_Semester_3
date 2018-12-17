package com.company;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyConcurencyListSet <T> {

    private Node<T> head;
    private Node<T> tail;
    private Lock lock;

    public MyConcurencyListSet() {
        this.head = null;
        this.tail = null;
        this.lock = new ReentrantLock();
    }

    public void add(T value) {
        Node<T> node = new Node<>(value);
        node.lock();
        lock.lock();
        if (Contains(value)) {
            lock.unlock();
            return false;
        }
        if (head == null) {
            head = node;
            tail = node;
            lock.unlock();
        } else {
            Node tmp = tail;
            tmp.setNext(node);
            tail = node;
            lock.unlock();
            node.setPrev(tmp);
        }
        node.unlock();
        return true;
    }

    public boolean contains(T value) {
        lock.lock();
        if (head == null) {
            lock.unlock();
            return false;
        }
        Node tmp = head;
        lock.unlock();
        while (tmp.getNext() != null) {
            if (tmp.getValue() == value) {
                return true;
            }
            tmp = tmp.getNext();
        }
        return false;
    }
    
    private boolean Contains(T value) {
        if (head == null) {
            return false;
        }
        Node tmp = head;
        while (tmp.getNext() != null) {
            if (tmp.getValue() == value) {
                return true;
            }
            tmp = tmp.getNext();
        }
        return false;
    }
}
