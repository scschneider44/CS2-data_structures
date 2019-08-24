package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;
import java.util.function.Supplier;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    // hard code in primes for capacity- should be prime and each should basically double
    //keep lambda around 1
    private int size;
    private IDictionary<K, V>[] buckets;

    IDeque<Integer> primes = new ArrayDeque<>();
    private Supplier<IDictionary<K, V>> chain;

     public  ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        this.chain = chain;

        this.size = 0;

        primes.addBack(2);
        primes.addBack(5);
        primes.addBack(11);
        primes.addBack(23);
        primes.addBack(47);
        primes.addBack(101);
        primes.addBack(211);
        primes.addBack(431);
        primes.addBack(863);
        primes.addBack(1733);
        primes.addBack(3468);
        primes.addBack(6971);
        primes.addBack(13883);
        primes.addBack(27793);
        primes.addBack(55589);
        primes.addBack(111191);
        primes.addBack(222349);
        this.buckets = new IDictionary[this.primes.removeFront()];
        for (int i=0; i < this.buckets.length; i++) {
            this.buckets[i] = chain.get();
        }

    }

    @Override
    public V get(K key) {
        int hash = key.hashCode() % this.buckets.length;
        if (hash < 0) {
            hash += this.buckets.length;
        }
        return this.buckets[hash].get(key);
    }

    @Override
    public V remove(K key) {
         if (!containsKey(key)) {
             return null;
         }
        int hash = key.hashCode() % this.buckets.length;
        if (hash < 0) {
            hash += this.buckets.length;
        }
        this.size--;
        return this.buckets[hash].remove(key);
    }

    @Override
    public V put(K key, V value) {
        int hash = key.hashCode() % this.buckets.length;
        if (hash < 0) {
            hash += this.buckets.length;
        }

        if (this.buckets[hash].containsKey(key)) {
            V oldVal = this.buckets[hash].get(key);
            this.buckets[hash].put(key, value);
            //this.size++;
            return oldVal;
        }

        this.buckets[hash].put(key, value);
        this.size++;

        if (this.size > 1.5 * this.buckets.length) {
            IDictionary<K, V>[] newBuckets = new IDictionary[primes.removeFront()];
            for (int i =0; i < newBuckets.length; i++) {
                newBuckets[i] = chain.get();
            }
            for (IDictionary<K, V> bucket : this.buckets) {
                for (K dictKeys : bucket.keySet()) {
                    int position = dictKeys.hashCode() % newBuckets.length;
                    if (position < 0) {
                        //System.out.println(position + " " + newBuckets.length);
                        position += newBuckets.length;
                    }
                    newBuckets[position].put(dictKeys, bucket.get(dictKeys));
                }
            }
            this.buckets = newBuckets;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
         int bucket = key.hashCode() % buckets.length;
        if (bucket < 0) {
            bucket += this.buckets.length;
        }
        /*for (IDictionary<K, V> bucket : this.buckets) {
            if (bucket != null) {
                if (bucket.containsKey(key)) {
                    return true;
                }
            }
        }*/
        if (buckets[bucket].isEmpty()) {
            return false;
        }
        return buckets[bucket].containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        /*for (IDictionary<K, V> bucket : this.buckets) {
            if (bucket != null) {
                if (bucket.containsValue(value)) {
                    return true;
                }
            }
        }*/
        return values().contains(value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keySet() {
        IDeque<K> keysSet = new LinkedDeque<>();
        for (IDictionary<K, V> bucket : this.buckets) {
            for (K key : bucket.keySet()) {
                keysSet.addBack(key);
            }
        }
        return keysSet;
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> vals = new LinkedDeque<>();
        for (IDictionary<K, V> bucket : buckets) {
            if (!bucket.isEmpty()) {
                for (V value : bucket.values()) {
                    vals.addBack(value);
                }
            }
        }
        return vals;
    }

    @Override
    public Iterator<K> iterator() {
         return keySet().iterator();

    }
}
