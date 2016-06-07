package edu.tcnj.stumpgrinder.data;

import java.util.Iterator;
import java.util.Set;

public final class Parser {

    private Parser() {
    }

    private static <S> String toStringRecursive(Node<S> root, boolean data) {
        String string = "";

        if (root.children.size() > 0) {
            string = ")" + string;
        }

        Iterator<Node<S>> it = root.children.iterator();
        while (it.hasNext()) {
            Node<S> child = it.next();
            string = (it.hasNext() ? "," : "") + child.label + ":" + (data ? stateString(child.root) : child.cost) + string;
            string = toStringRecursive(child, data) + string;
        }

        if (root.children.size() > 0) {
            string = "(" + string;
        }

        return string;
    }

    public static <S> String toString(Node<S> root, boolean data) {
        String string = root.label + ":" + (data ? stateString(root.root) : root.cost);
        return toStringRecursive(root, data) + string;
    }

    public static <S> Node<S> fromString(String s) {
        int last = s.lastIndexOf(':');

        String label = s.substring(last - 1, last);

        return fromStringRecursive(s, new Node<S>(label), 0, last);
    }

    private static <S> Node<S> fromStringRecursive(String s, Node<S> parent, int start, int end) {
        //Check if the substring contains a single label.
        // if so, return the node. Else, continue parsing the string
        if (s.charAt(start) != '(') {
            return parent;
        }

        int brackets = 0; // counts parenthesis
        int colon = 0; // marks the position of the colon
        int marker = start; // marks the position of string
        String label = ""; //stores the label of the node

        for (int i = start; i < end; i++) {
            char c = s.charAt(i);

            if (c == '(') {
                brackets++;
            } else if (c == ')') {
                brackets--;
            } else if (c == ':') {
                colon = i;
            }

            if (brackets == 0 && c == ')' || brackets == 1 && c == ',') {
                if (s.charAt(colon - 1) == ')') {
                    label = "";
                } else {
                    label = s.substring(colon - 1, colon);
                }
                int cost = Integer.valueOf(String.valueOf(s.charAt(colon + 1)));

                Node<S> newNode = new Node<>(label, cost);
                newNode.parent = parent;
                parent.children.add(fromStringRecursive(s, newNode, marker + 1, colon));
                marker = i;
            }
        }
        return parent;
    }


    private static <S> String stateString(CharacterList<S> root) {
        Iterator<Set<S>> it = root.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Iterator<S> intIt = it.next().iterator();
            while (intIt.hasNext()) {
                sb.append(intIt.next());
            }
        }
        return sb.toString();
    }
}
