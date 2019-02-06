package com.company;

public class QueueNode<T> {

    private T value;
    private QueueNode<T> next;
    private QueueNode<T> previous;

    public QueueNode(T value) {
        this.value = value;
        next = null;
        previous = null;
    }

    public T getValue() {
        return value;
    }

    public void setNext(QueueNode<T> next) {
        this.next = next;
    }

    public void setPrevious(QueueNode<T> previous) {
        this.previous = previous;
    }

    public QueueNode<T> getNext() {
        return next;
    }

    public QueueNode<T> getPrevious() {
        return previous;
    }
}
