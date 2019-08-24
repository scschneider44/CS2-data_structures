package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.Iterator;
import java.util.function.Function;

public class TrieMap<A, K extends Iterable<A>, V> extends ITrieMap<A, K, V> {

    private IDeque<K> keysSet = new LinkedDeque<>();
    
    public TrieMap(Function<IDeque<A>, K> collector) {
        super(collector);
    }

    @Override
    public boolean isPrefix(K key) {
        return isPrefixHelper(key);
    }

    private boolean isPrefixHelper(K key) {
        TrieNode<A, V> curr = this.root;
        for (A letter : key) {
            if (!curr.pointers.containsKey(letter)) {
                return false;
            }
            else {
                curr = curr.pointers.get(letter);
            }
        }
        return true;
    }

    @Override
    public IDeque<V> getCompletions(K prefix) {
        IDeque<V> completions = new LinkedDeque<>();
        if (!isPrefix(prefix)) {
            return completions;
        }
        TrieNode<A, V> node = nodeHelper(prefix);
        getCompletionsHelper(node, completions);
        return completions;
    }

    private void getCompletionsHelper(TrieNode<A, V> curr, IDeque<V> completions) {
        if (curr.value != null) {
            completions.addBack(curr.value);
        }
        for (A mapKey : curr.pointers.keySet()) {
            getCompletionsHelper(curr.pointers.get(mapKey), completions);
        }
    }

    private TrieNode<A, V> nodeHelper(K key) {
        TrieNode<A, V> curr = this.root;
        for (A letter : key) {
            if (!curr.pointers.containsKey(letter)) {
                return null;
            }
            else {
                curr = curr.pointers.get(letter);
            }
        }
        return curr;
    }

    @Override
    public V get(K key) {
       return loopHelper(key);
    }

    private V loopHelper(K key) {
        TrieNode<A, V> curr = this.root;
        for (A letter : key) {
            if (!curr.pointers.containsKey(letter)) {
                return null;
            }
            else {
                curr = curr.pointers.get(letter);
            }
        }
        return curr.value;
    }

    @Override
    public V remove(K key) {
        //removes value at key
        //different cases for one at leaf node and if intermediate
        remove(this.root);
        this.size--;
        return null;
    }
    private void remove(TrieNode<A, V> current) {

    }

    @Override
    public V put(K key, V value) {
        keysSet.addBack(key);
        return putHelper(key.iterator(), value, this.root);
    }

    private V putHelper(Iterator<A> key, V value, TrieNode<A, V> node) {
        if (!key.hasNext()) {
            if (node.value == null){
                this.size++;
            }
            V oldVal = node.value;
            node.value = value;
            return oldVal;
        }
        else {
            A letter = key.next();
            if (!node.pointers.containsKey(letter)) {
                node.pointers.put(letter, new TrieNode<>());
            }
            return putHelper(key, value, node.pointers.get(letter));
        }
    }

    @Override
    public boolean containsKey(K key) {
        return keysSet.contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        if (this.values().contains(value)) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        return keysSet;
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> emptyDeque = new LinkedDeque<>();
        return getCompletions((K)emptyDeque);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}