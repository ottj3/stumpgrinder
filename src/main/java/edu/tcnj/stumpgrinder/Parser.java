package edu.tcnj.stumpgrinder;

import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

public class Parser {

    public Parser() {
    }

    public <S> String toString(Node<S> root, boolean data) {
        String string = root.label + ":" + (data ? stateString(root.root) : root.cost) + ";";
        return toStringRecursive(root, data) + string;
    }

    private <S> String toStringRecursive(Node<S> root, boolean data) {
        String string = "";

        if (root.children.size() > 0) {
            string = ")" + string;
        }

        ListIterator<Node<S>> it = root.children.listIterator(root.children.size());
        while (it.hasPrevious()) {
            Node<S> child = it.previous();
            string = child.label + ":" + (data ? stateString(child.root) : child.cost) + string;
            string = (it.hasPrevious() ? "," : "") + toStringRecursive(child, data) + string;
        }

        if (root.children.size() > 0) {
            string = "(" + string;
        }

        return string;
    }

    public <S> Node<S> fromString(String s) {
        int last = s.lastIndexOf(':');

        String label = s.substring(last - 1, last);

        return fromStringRecursive(s, new Node<S>(label), 0, last);
    }

    private <S> Node<S> fromStringRecursive(String s, Node<S> parent, int start, int end) {
        //Check if the substring contains a single label.
        // if so, return the node. Else, continue parsing the string
        if (s.charAt(start) != '(') {
            return parent;
        }

        int parens = 0; // counts parenthesis
        int brackets = 0; // for comments, label data, etc
        int colon = 0; // marks the position of the colon
        int marker = start; // marks the position of string
        String label; //stores the label of the node

        for (int i = start; i < end; i++) {
            char c = s.charAt(i);

            if (c == '(') {
                parens++;
            } else if (c == ')') {
                parens--;
            } else if (c == ':') {
                colon = i;
            } else if (c == '[') {
                brackets++;
            } else if (c == ']') {
                brackets--;
            }

            if (parens == 0 && c == ')' || parens == 1 && c == ',') {
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


    private <S> String stateString(CharacterList<S> root) {
        Iterator<Set<S>> it = root.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) { // for each character
            Iterator<S> intIt = it.next().iterator();
            boolean first = true;
            while (intIt.hasNext()) { // for each state in potential set of states
                if (!first) {
                    sb.append("-");
                }
                sb.append(intIt.next());
                first = false;
            }
        }
        return sb.toString();
    }
}
