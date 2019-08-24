package edu.caltech.cs2.sorts;

import edu.caltech.cs2.datastructures.MinFourHeap;
import edu.caltech.cs2.interfaces.IPriorityQueue;

public class TopKSort {
    /**
     * Sorts the largest K elements in the array in descending order.
     * @param array - the array to be sorted; will be manipulated.
     * @param K - the number of values to sort
     * @param <E> - the type of values in the array
     */
    public static <E> void sort(IPriorityQueue.PQElement<E>[] array, int K) {
        if (K < 0) {
            throw new IllegalArgumentException("K cannot be negative!");
        }
        if (K == 0) {
            for (int i=0; i < array.length; i++){
                array[i] = null;
            }
            return;
        }
        MinFourHeap<E> heap = new MinFourHeap<>();
        if (array.length < K) {
            K = array.length;
        }
        for (int i = 0; i < K; i++) {
            heap.enqueue(array[i]);
        }
        for (int j=K; j < array.length; j++) {
            if (heap.peek() != null) {
                if (array[j].priority > heap.peek().priority) {
                    heap.enqueue(array[j]);
                    heap.dequeue();
                    //heap.enqueue(array[j]);
                }
            }
        }
        for (int i=array.length - 1; i >= 0; i--) {
            if (i < K) {
                array[i] = heap.dequeue();
            }
            else {
                array[i] = null;
            }
        }
    }
}
