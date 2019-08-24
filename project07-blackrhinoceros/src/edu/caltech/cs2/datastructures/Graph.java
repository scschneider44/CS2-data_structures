package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IGraph;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.ISet;

public class Graph<V, E> extends IGraph<V, E> {

    private IDictionary<V, IDictionary<V, E>> graph;

    public Graph() {
        this.graph = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    @Override
    public boolean addVertex(V vertex) {
        if (this.graph.keySet().contains(vertex)) {
            return false;
        }
        this.graph.put(vertex, new ChainingHashDictionary<>(MoveToFrontDictionary::new));
        return true;
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {
        if (!this.graph.keySet().contains(src) || !this.graph.keySet().contains(dest)) {
            throw new IllegalArgumentException("Make sure all vertices are in the graph");
        }
        if (this.graph.get(src).get(dest) != null) {
            this.graph.get(src).put(dest, e);
            return false;
        }
        this.graph.get(src).put(dest, e);
        return true;
    }

    @Override
    public boolean addUndirectedEdge(V src, V dest, E e) {
        boolean add1 = addEdge(src, dest, e);
        boolean add2 = addEdge(dest, src, e);
        return add1 && add2;
    }

    @Override
    public boolean removeEdge(V src, V dest) {
        if (!this.graph.keySet().contains(src) || !this.graph.keySet().contains(dest)) {
            throw new IllegalArgumentException("Make sure all vertices are in the graph");
        }
        return this.graph.get(src).remove(dest) != null;
    }

    @Override
    public ISet<V> vertices() {
        return this.graph.keySet();
    }

    @Override
    public E adjacent(V i, V j) {
        if (!this.graph.keySet().contains(i) || !this.graph.keySet().contains(j)) {
            throw new IllegalArgumentException("Make sure all vertices are in the graph");
        }
        return this.graph.get(i).get(j);
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        if (!this.graph.keySet().contains(vertex)) {
            throw new IllegalArgumentException("Make sure the vertex is in the graph");
        }
        return this.graph.get(vertex).keySet();
    }
}