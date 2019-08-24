package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {

    private E[] data;
    private int front;
    private int back;

    public CircularArrayFixedSizeQueue(int capacity) {
        this.data = (E[])new Object[capacity];
        this.front = 0;
        this.back = -1;
    }

    @Override
    public boolean isFull() {
        return this.size() == this.capacity();
    }

    @Override
    public int capacity() {
        return this.data.length;
    }

    @Override
    public boolean enqueue(E e) {
        if (!this.isFull()) {
            this.back = (this.back + 1) % this.capacity();
            this.data[this.back] = e;
            return true;
        }
        return false;
    }

    @Override
    public E dequeue() {
        if (this.size() == 1) {
            E front = this.data[this.front];
            this.front  = 0;
            this.back = -1;
            return front;
        }
        else if (this.size() > 1) {
            E front = this.data[this.front];
            this.front  = (this.front + 1) % this.capacity();
            return front;
        }
        return null;
    }

    @Override
    public E peek() {
        if (this.size() == 0) {
            return null;
        }
        return this.data[this.front];
    }

    @Override
    public int size() {
        if (this.back == -1) {
            return 0;
        }
        else if (this.back >= this.front) {
            return this.back - this.front + 1;
        }
        else {
            return this.capacity() - (this.front - this.back) + 1;
        }
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> it = new Iterator<>() {
            private int curr = 0;
            @Override
            public boolean hasNext() {
                return curr < CircularArrayFixedSizeQueue.this.size();
            }
            @Override
            public E next() {
                return CircularArrayFixedSizeQueue.this.data[curr++];
            }
        };
        return it;
    }

    @Override
    public String toString() {
        if (this.size() == 0) {
            return "[]";
        }

        String result = "[";
        for (int i = 0; i < this.size() - 1; i++) {
            result += this.data[i] + ", ";
        }
        return result + this.data[this.size() - 1] + "]";
    }
}
