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
        try {
            node.lock();
            lock.lock();
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
        }
        finally {
            lock.unlock();
            node.unlock();
        }
    }

    public boolean contains(T value) {
        try {
            lock.lock();
            if (head == null) {
                lock.unlock();
                return false;
            }
            Node tmp = head;
            lock.unlock();
        }
        finally {
            lock.unlock();
        }
        while (tmp.getNext() != null) {
            if (tmp.getValue() == value) {
                System.out.println(Thread.currentThread().getId() + " contains finished");
                return true;
            }
            tmp = tmp.getNext();
        }
        return false;
    }
}
