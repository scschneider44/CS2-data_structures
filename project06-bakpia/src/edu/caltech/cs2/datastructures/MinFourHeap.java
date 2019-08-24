package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.Iterator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 5;
    private static final int GROW_FACTOR = 2;

    private int size;
    private int capacity;
    private PQElement<E>[] data;
    private IDictionary<E, Integer> keyToIndexMap;

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PQElement[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.keyToIndexMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);

    }

    @Override
    public void increaseKey(PQElement<E> key) {
        if (!checkChildren(key)) {
            throw new IllegalArgumentException("Element not in heap");
        }
        int idx = keyToIndexMap.get(key.data);
        //keyToIndexMap.remove(this.data[idx].data);
        this.data[idx] = key;
        //keyToIndexMap.put(key.data, idx);
        percolateDown(idx);

    }

    @Override
    public void decreaseKey(PQElement<E> key) {
        if (!checkChildren(key)) {
            throw new IllegalArgumentException("Element not in heap");
        }

        int idx = keyToIndexMap.get(key.data);
        //keyToIndexMap.remove(this.data[idx].data);
        this.data[idx] = key;
        //keyToIndexMap.put(key.data, idx);
        percolateUp(idx);
    }

    private boolean checkChildren(PQElement<E> child) {
        return this.keyToIndexMap.containsKey(child.data);
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        if (checkChildren(epqElement)) {
            throw new IllegalArgumentException("Already in heap");
        }
        if (this.size == this.capacity) {
            PQElement<E>[] newData = new PQElement[this.capacity*GROW_FACTOR];
            for (int i = 0; i < this.size; i++) {
                newData[i] = this.data[i];
            }
            this.data = newData;
            this.capacity = this.capacity * GROW_FACTOR;
        }
        this.data[this.size] = epqElement;
        keyToIndexMap.put(epqElement.data, this.size);
        percolateUp(size);
        this.size++;
        return true;
    }

    @Override
    public PQElement<E> dequeue() {
        if (this.data.length == 0) {
            return null;
        }
        PQElement<E> front = this.data[0];
        this.data[0] = this.data[this.size -1];
        this.keyToIndexMap.remove(front.data);
        this.keyToIndexMap.put(this.data[this.size-1].data, 0);
        this.data[this.size - 1] = null;
        this.size--;
        percolateDown(0);
        return front;
    }

    private void percolateUp(int idx) {
        while (idx > 0 && this.data[(idx-1)/4] != null && this.data[idx].priority < this.data[(idx-1)/4].priority) {
            PQElement<E> temp = this.data[idx];
            this.data[idx] = this.data[(idx-1)/4];
            keyToIndexMap.put(this.data[idx].data, idx);
            this.data[(idx-1)/4] = temp;
            keyToIndexMap.put(temp.data, (idx-1)/4);
            idx = (idx-1)/4;
        }
    }

    private void percolateDown(int idx) {
        int target = getSmallestChild(idx);
        while (target != 0 && this.data[target] != null && this.data[target].priority < this.data[idx].priority) {
            PQElement<E> temp = this.data[idx];
            this.data[idx] = this.data[target];
            keyToIndexMap.put(this.data[idx].data, idx);
            this.data[target] = temp;
            keyToIndexMap.put(temp.data, target);
            idx = target;
            target = getSmallestChild(idx);
        }

    }

    private int getSmallestChild(int idx) {
        if (4*idx +1 > this.size) {
            return 0;
        }
        int minIdx = 4*idx +1;
        PQElement<E> minChild = this.data[4*idx + 1];
        for (int i = 4*idx + 2; i < 4*idx + 5; i++) {
            if (minChild != null && this.data[i] != null && this.data[i].priority < minChild.priority) {
                minChild = this.data[i];
                minIdx = i;
            }
        }
        return minIdx;
    }

    @Override
    public PQElement<E> peek() {
        return this.data[0];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return null;
    }
}