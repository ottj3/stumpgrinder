package edu.tcnj.stumpgrinder.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node<S> {

    public static int chars;
    public static <S> CharacterList<S> sets() {
        List<Set<S>> sets = new ArrayList<>(chars);
        for (int i = chars; i-- > 0;) {
            sets.add(new HashSet<S>());
        }
        return new CharacterList<>(sets);
    }
    public static <S> void linkNodes(Node<S> parent, Node<S> child) {
        parent.children.add(child);
        child.parent = parent;
    }
    public static <S> void unlinkNodes(Node<S> parent, Node<S> child) {
        parent.children.remove(child);
        if (parent == child.parent) {
            child.parent = null;
        }
    }
    public Node(String label) {
        this.label = label;
        this.labelled = !label.isEmpty();
    }

    public Node(String label, int cost) {
        this(label);
        this.cost = cost;
    }

    public Node<S> clone() {
        Node<S> newNode = new Node<>(this.label, this.cost);
        for(Node<S> child : children) {
            Node<S> newChild = child.clone();
            newChild.parent = newNode;
            newNode.children.add(newChild);
        }
        newNode.root = this.root;
        newNode.upper = this.upper;
        newNode.lower = this.lower;
        return newNode;
    }
    public String label;

    public int cost;

    public boolean labelled;

    public CharacterList<S> root = new CharacterList<>();
    public CharacterList<S> upper = new CharacterList<>();
    public CharacterList<S> lower = new CharacterList<>();

    public Node<S> parent;
    public List<Node<S>> children = new ArrayList<>();

}
