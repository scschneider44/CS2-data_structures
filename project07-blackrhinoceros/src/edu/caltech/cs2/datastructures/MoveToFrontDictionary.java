package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private DictNode head;
    private int size;

    private class DictNode {
        public K key;
        public V data;
        public DictNode next;

        public DictNode (K key, V data) {
            this(key, data, null);
        }

        public DictNode(K key, V data, DictNode next) {
            this.key = key;
            this.data = data;
            this.next = next;
        }
    }

    public MoveToFrontDictionary() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public V get(K key) {
        if (containsKey(key)) {
            return this.head.data;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        else {
            V removedValue = get(key);
            this.head.key = null;
            this.head = this.head.next;
            this.size--;
            return removedValue;
        }
    }

    @Override
    public V put(K key, V value) {
        if (containsKey(key)) {
            V oldValue = this.head.data;
            this.head.data = value;
            return oldValue;
        }
        if (this.head == null) {
            this.head = new DictNode(key, value, null);
        }
        else {
            DictNode newHead = new DictNode(key, value, null);
            newHead.next = this.head;
            this.head = newHead;
        }
        this.size++;
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        DictNode curr = this.head;
        DictNode prev = null;

        if (this.head == null) {
            return false;
        }

        if (this.head.key.equals(key)) {
            return true;
        }
        else {
            while (curr != null) {
                if (curr.key.equals(key) && prev != null) {
                    prev.next = curr.next;
                    curr.next = this.head;
                    this.head = curr;
                    return true;
                }
                prev = curr;
                curr = curr.next;
            }
            return false;
        }
    }

    @Override
    public boolean containsValue(V value) {
        DictNode curr = this.head;
        DictNode prev = null;
        if (!values().contains(value)) {
            return false;
        }
        else {
            while (curr != null) {
                if (curr.data.equals(value) && prev != null) {
                    prev.next = curr.next;
                    curr.next = this.head;
                    this.head = curr;
                    break;
                }
                prev = curr;
                curr = curr.next;
            }
            return true;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        IDeque<K> keysSet = new LinkedDeque<>();
        DictNode curr = this.head;
        while (curr != null) {
            keysSet.addBack(curr.key);
            curr = curr.next;
        }
        return keysSet;
    }

    @Override
    public ICollection<V> values() {
        DictNode curr = this.head;
        IDeque<V> valueDeque = new LinkedDeque<>();
        while (curr != null) {
            valueDeque.addBack(curr.data);
            curr = curr.next;
        }
        return valueDeque;
    }

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }
}
