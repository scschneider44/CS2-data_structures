package edu.caltech.cs2.project08.datastructures;

import edu.caltech.cs2.project08.interfaces.IDeque;
import edu.caltech.cs2.project08.interfaces.IQueue;
import edu.caltech.cs2.project08.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private static final int defaultCapacity = 10;
    private static final int growthFactor = 2;
    private E[] data;
    private int size;
    private int currCapacity;

    public ArrayDeque() {
        this(defaultCapacity);
    }

    public ArrayDeque(int initialCapacity) {
        this.data = (E[])new Object[initialCapacity];
        this.currCapacity = initialCapacity;
        this.size = 0;
    }

    @Override
    public void addFront(E e) {
        this.size++;
        if (this.size >= this.currCapacity) {
            E[] newData = (E[])new Object[(this.currCapacity*growthFactor)];
            for (int i = this.size; i > 0; i--) {
                newData[i] = this.data[i-1];
            }
            this.data = newData;
            this.currCapacity = this.currCapacity*growthFactor;
        }
        else {
            for (int j = this.size; j > 0; j--) {
                this.data[j] = this.data[j-1];
            }
        }
        this.data[0] = e;
    }

    @Override
    public void addBack(E e) {
        this.size++;
        if (this.size >= this.currCapacity) {
            E[] newData = (E[])new Object[(this.currCapacity*growthFactor)];
            for (int i = 0; i < this.size; i++) {
                newData[i] = this.data[i];
            }
            this.data = newData;
            this.currCapacity = this.currCapacity*growthFactor;
        }
        this.data[this.size-1] = e;
    }

    @Override
    public E removeFront() {
        E front = this.data[0];
        if (this.size == 0) {
            return null;
        }
        for (int i = 0; i < this.size-1; i++) {
            this.data[i] = this.data[i+1];
        }
        this.size--;
        return front;
    }

    @Override
    public E removeBack() {
        if (this.size == 0) {
            return null;
        }
        E back = this.data[this.size-1];
        this.size--;
        return back;
    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        return removeBack();
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        return removeBack();
    }

    @Override
    public E peek() {
        return peekBack();
    }

    @Override
    public E peekFront() {
        if (this.size == 0) {
            return null;
        }
        return this.data[0];
    }

    @Override
    public E peekBack() {
        if (this.size == 0) {
            return null;
        }
        return this.data[this.size-1];
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> newIterator = new Iterator<>() {
            private int idx = 0;
            @Override
            public boolean hasNext() {
                return (idx < ArrayDeque.this.size);
            }

            @Override
            public E next() {
                return ArrayDeque.this.data[idx++];
            }
        };
        return newIterator;
    }

    @Override
    public int size() {
       return this.size;
    }

    @Override
    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        String newString = "";
        for (int i = 0; i < this.size; i++) {
            newString += this.data[i];
            if (i != this.size - 1) {
                newString += ", ";
            }
        }
        return "[" + newString + "]";
    }
}
