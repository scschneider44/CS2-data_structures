package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROW_FACTOR = 2;

    private int capacity;
    private E[] data;
    private int size;

    public ArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayDeque(int initialCapacity) {
        this.data = (E[])new Object[initialCapacity];
        this.capacity = initialCapacity;
        this.size = 0;
    }

    @Override
    public void addFront(E e) {

        this.size++;
        if (this.size >= this.capacity) {
            E[] newData = (E[])new Object[(this.capacity*GROW_FACTOR)];
            for (int i = 0; i < this.size; i++) {
                newData[i+1] = this.data[i];
            }
            this.data = newData;
            this.capacity = this.capacity * GROW_FACTOR;
        }
        else {
            if (this.size > 0) {
                for (int i=this.size; i > 0; i--) {
                    this.data[i] = this.data[i-1];
                }
            }
        }
        this.data[0] = e;
    }

    @Override
    public void addBack(E e) {
        this.size++;
        if (this.size >= this.capacity) {
            E[] newData = (E[])new Object[(this.capacity*GROW_FACTOR)];
            for (int i = 0; i < this.size; i++) {
                newData[i] = this.data[i];
            }
            this.data = newData;
            this.capacity = this.capacity * GROW_FACTOR;
        }
        this.data[this.size - 1] = e;
    }

    @Override
    public E removeFront() {
        if (this.size > 0) {

            E front = this.data[0];

            for (int i = 0; i < this.size - 1; i++) {
                this.data[i] = this.data[i+1];
            }
            this.size--;

            return front;
        }
        return null;
    }

    @Override
    public E removeBack() {
        if (this.size > 0) {
            E back = this.data[this.size-1];
            this.size--;
            return back;
        }
        return null;
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
        return this.peekBack();
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
        return this.data[this.size - 1];
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> it = new Iterator<>() {

            private int curr = 0;

            @Override
            public boolean hasNext() {
                return curr < ArrayDeque.this.size;
            }

            @Override
            public E next() {
                return ArrayDeque.this.data[curr++];
            }
        };
        return it;
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

        String result = "[";
        for (int i = 0; i < this.size - 1; i++) {
            result += this.data[i] + ", ";
        }
        return result + this.data[this.size - 1] + "]";
    }
}
