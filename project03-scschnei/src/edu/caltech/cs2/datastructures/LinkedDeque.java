package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private int numElements;
    private LinkNode head;
    private LinkNode tail;

    private class LinkNode {
        private E data;
        private LinkNode next;
        private LinkNode prev;

        public LinkNode(E data, LinkNode next, LinkNode prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }


    public LinkedDeque() {
        this.head = null;
        this.tail = null;
        this.numElements = 0;

    }

    @Override
    public void addFront(E e) {
        if (numElements > 0) {
            numElements++;
            LinkNode oldHead = this.head;
            this.head = new LinkNode(e, oldHead, null);
            oldHead.prev = this.head;
        }
        else {
            LinkNode both = new LinkNode(e, null, null);
            this.head = both;
            this.tail = both;
            numElements++;
        }
    }

    @Override
    public void addBack(E e) {
        if (numElements > 0) {
            numElements++;
            LinkNode oldTail = this.tail;
            this.tail = new LinkNode(e, null, oldTail);
            oldTail.next = this.tail;
        }
        else {
            LinkNode both = new LinkNode(e, null, null);
            this.head = both;
            this.tail = both;
            numElements++;
        }
    }

    @Override
    public E removeFront() {
        E removed = null;
        if (numElements > 1) {
            numElements--;
            removed = this.head.data;
            this.head = this.head.next;
            this.head.prev = null;
        }
        else if (numElements == 1){
            numElements--;
            removed = this.head.data;
            this.head = null;
            this.tail = null;
        }
        return removed;
    }

    @Override
    public E removeBack() {
        E removed = null;
        if (numElements > 1) {
            numElements--;
            removed = this.tail.data;
            this.tail = this.tail.prev;
            this.tail.next = null;
        }
        else if (numElements == 1){
            numElements--;
            removed = this.tail.data;
            this.head = null;
            this.tail = null;
        }
        return removed;
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
        if (this.size() == 0) {
            return null;
        }
        return this.head.data;
    }

    @Override
    public E peekBack() {
        if (this.size() == 0) {
            return null;
        }
        return this.tail.data;
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> it = new Iterator<>() {

            private LinkNode curr = LinkedDeque.this.head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public E next() {
                E data = curr.data;

                curr = curr.next;
                return data;
            }
        };
        return it;
    }

    @Override
    public int size() {
        return this.numElements;
    }

    @Override
    public String toString() {

        if (this.size() == 0) {
            return "[]";
        }

        String result = "[";
        LinkNode fakeHead = this.head;

        while (fakeHead.next != null) {
            result += fakeHead.data + ", ";
            fakeHead = fakeHead.next;
        }
        return result + fakeHead.data + "]";
    }
}