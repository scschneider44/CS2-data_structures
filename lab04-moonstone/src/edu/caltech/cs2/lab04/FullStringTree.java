package edu.caltech.cs2.lab04;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullStringTree {
    protected static class StringNode {
        public final String data;
        public StringNode left;
        public StringNode right;

        public StringNode(String data) {
            this(data, null, null);
        }

        public StringNode(String data, StringNode left, StringNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
            // Ensures that the StringNode is either a leaf or has two child nodes.
            if ((this.left == null || this.right == null) && !this.isLeaf()) {
                throw new IllegalArgumentException("StringNodes must represent nodes in a full binary tree");
            }
        }

        // Returns true if the StringNode has no child nodes.
        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    protected StringNode root;

    protected FullStringTree() {}

    public FullStringTree(Scanner in) {
        this.root = deserialize(in);
    }

    private StringNode deserialize(Scanner in) {
        StringNode newNode;
        if (in.hasNext()) {
            String node = in.nextLine();
            String[] scanData = node.split(":");
            if (scanData[0].equals("L")) {
                newNode = new StringNode(scanData[1].strip());
            }
            else {
                newNode = new StringNode(scanData[1].strip(), deserialize(in), deserialize(in));
            }
            return newNode;
        }
        return null;
    }

    public List<String> explore() {
        return explore(this.root);
    }
    private List<String> explore(StringNode current) {
        List<String> tree = new ArrayList<>();
        if (current != null) {
            if (current.left == null) {
                tree.add("L: " + current.data);
            }
            else {
                tree.add("I: " + current.data);
            }
        }
        if (current.left != null) {
            tree.addAll(explore(current.left));
        }
        if (current.right != null) {
            tree.addAll(explore(current.right));
        }
        return tree;
    }

    public void serialize(PrintStream output) {
        List<String> tree = explore(this.root);
        for (int i=0; i < tree.size(); i++) {
            output.println(tree.get(i));
        }
    }
}