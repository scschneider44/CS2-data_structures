package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.ICollection;

import java.util.Iterator;

public class BSTDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {

    private static int LEFT = 0;
    private static int RIGHT = 1;

    protected BSTNode<K, V> root;
    protected int size;

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ICollection<K> keySet() {
        return null;
    }

    @Override
    public ICollection<V> values() {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }
    
    @Override
    public String toString() {
        if (this.root == null) {
            return "{}";
        }

        StringBuilder contents = new StringBuilder();

        IQueue<BSTNode<K, V>> nodes = new ArrayDeque<>();
        BSTNode<K, V> current = this.root;
        while (current != null) {
            contents.append(current.key + ": " + current.value + ", ");

            if (current.children[0] != null) {
                nodes.enqueue(current.children[0]);
            }
            if (current.children[1] != null) {
                nodes.enqueue(current.children[1]);
            }

            current = nodes.dequeue();
        }

        return "{" + contents.toString().substring(0, contents.length() - 2) + "}";
    }

    protected static class BSTNode<K, V> {
        public final K key;
        public final V value;
        public BSTNode<K, V>[] children;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.children = (BSTNode<K, V>[]) new BSTNode[2];
        }

        public BSTNode(BSTNode<K, V> o) {
            this.key = o.key;
            this.value = o.value;
            this.children = (BSTNode<K, V>[]) new BSTNode[2];
            this.children[LEFT] = o.children[LEFT];
            this.children[RIGHT] = o.children[RIGHT];
        }

        public boolean isLeaf() {
            return this.children[LEFT] == null && this.children[RIGHT] == null;
        }

        public boolean hasBothChildren() {
            return this.children[LEFT] != null && this.children[RIGHT] != null;
        }
    }
}
