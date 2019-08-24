package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E> {
    private LinkedNode head;
    private LinkedNode tail;
    private int size;

    private class LinkedNode {
        public final E data;
        public LinkedNode next;
        public LinkedNode prev;

        public LinkedNode(E data) {
            this(data, null, null);
        }

        public LinkedNode(E data, LinkedNode next, LinkedNode prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    public LinkedDeque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void addFront(E e) {
        if (this.head == null) {
            this.head = new LinkedNode(e);
            this.tail = this.head;
        } else {
            LinkedNode newNode = new LinkedNode(e);
            this.head.prev = newNode;
            newNode.next = this.head;
            this.head = newNode;
            this.head.prev = null;
        }
        this.size++;
    }

    @Override
    public void addBack(E e) {
        if (this.tail == null) {
            this.head = new LinkedNode(e);
            this.tail = this.head;
            this.size++;
            return;
        }
        LinkedNode lastNode = new LinkedNode(e);
        lastNode.next = null;
        lastNode.prev = this.tail;

        this.tail.next = lastNode;
        this.tail = lastNode;

        this.size++;
    }

    @Override
    public E removeFront() {
        if (this.head == null) {
            return null;
        }
        if (this.head.next == null) {
            E headData = this.head.data;
            this.head = null;
            this.tail = null;
            this.size--;
            return headData;
        }

        E front = this.head.data;
        this.head = this.head.next;
        this.head.prev = null;

        this.size--;
        return front;
    }

    @Override
    public E removeBack() {
        if (this.tail == null) {
            return null;
        }
        if (this.tail.prev == null) {
            E tailData = this.tail.data;
            this.tail = null;
            this.head = null;
            this.size--;
            return tailData;
        }

        E back = this.tail.data;
        this.tail = this.tail.prev;
        this.tail.next = null;
        this.size--;
        return back;
    }

    @Override
    public E peek() {
        return peekFront();
    }

    @Override
    public E peekFront() {
        if (this.head == null) {
            return null;
        }
        return this.head.data;
    }

    @Override
    public E peekBack() {
        if (this.tail == null) {
            return null;
        }
        return this.tail.data;
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> newIterator = new Iterator<>() {
            LinkedNode curr = LinkedDeque.this.head;

            @Override
            public boolean hasNext() {
                if (curr == null) {
                    return false;
                }
                return true;
            }

            @Override
            public E next() {
                E nextData = curr.data;
                curr = curr.next;
                return nextData;
            }
        };
        return newIterator;
    }

    @Override
    public int size() {
        return this.size;
    }
}
