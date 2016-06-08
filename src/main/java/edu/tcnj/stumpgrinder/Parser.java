package edu.tcnj.stumpgrinder;

import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.*;

public class Parser {

    public Parser() {
    }

    public <S> String toString(Node<S> root, boolean data) {
        if (root == null) return ";";
        String string = root.label + ":" + root.cost + ";";
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
            // TODO add data to label
            string = child.label + ":" + child.cost + string;
            string = (it.hasPrevious() ? "," : "") + toStringRecursive(child, data) + string;
        }

        if (root.children.size() > 0) {
            string = "(" + string;
        }

        return string;
    }

    private static final String SPECIALS = "():,;";

    public <S> Node<S> fromString(String s) {
        if (s.length() == 1 && s.charAt(0) == ';') return null;
        if (s.charAt(s.length() - 1) != ';') {
            throw new IllegalArgumentException("Invalid Newick string, missing ';'");
        }
        return fromStringRecursive(s.substring(0, s.length() - 1), null);
    }

    private <S> Node<S> fromStringRecursive(String s, Node<S> parent) {
        int brackets = 0;
        int topLabelPos = s.lastIndexOf(':');
        if (topLabelPos == -1) return null; // empty tree
        if (topLabelPos == s.length()) throw new IllegalArgumentException("Found node with no cost");

        // label is everything between the colon and the closest special character on the left
        int prevSpecial = -1;
        for (int i = topLabelPos - 1; i >= 0; i--) {
            // scan left until we find one of our special characters
            int ch = s.charAt(i);
            if (ch == ']') brackets++;
            else if (ch == '[') brackets--;

            // ignore special characters between brackets
            if (brackets == 0 && SPECIALS.indexOf(ch) != -1) {
                prevSpecial = i;
                break;
            }
        }
        String label = s.substring(prevSpecial == -1 ? 0 : prevSpecial + 1, topLabelPos);

        brackets = 0;
        // cost is everything between the colon and the closest special character on the right
        int nextSpecial = -1;
        for (int i = topLabelPos + 1; i < s.length(); i++) {
            // scan right until we find one of our special characters
            int ch = s.charAt(i);
            if (ch == ']') brackets--;
            else if (ch == '[') brackets++;

            // ignore special characters between brackets
            if (brackets == 0 && SPECIALS.indexOf(ch) != -1) {
                nextSpecial = i;
                break;
            }
        }
        // if the next character after the colon was a special character, it means the cost substring is empty
        if (nextSpecial == topLabelPos + 1) throw new IllegalArgumentException("Found label with no cost: " + (label.isEmpty() ? "[Empty]" : label));
        String costStr = s.substring(topLabelPos + 1, nextSpecial == -1 ? s.length() : nextSpecial);
        int cost;
        try {
            cost = Integer.valueOf(costStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Node " + (label.isEmpty() ? "[Empty]" : label) + " has non-number cost: " + costStr);
        }

        // TODO parse data from label if present
        Node<S> current = new Node<>(label, cost);
        if (parent != null) {
            current.parent = parent;
            parent.children.add(current);
        }

        // current node has no children
        if (prevSpecial == -1 || s.charAt(prevSpecial) != ')') {
            return current;
        }
        // otherwise
        // find the matching ( for all of current's children
        int childrenEnd = -1;
        int parens = 0;
        brackets = 0;
        for (int i = prevSpecial; i >= 0; i--) { // first will always be a close paren
            char ch = s.charAt(i);
            if (ch == ']') brackets++;
            else if (ch == '[') brackets--;
            else if (brackets == 0 && ch == ')') parens++; // don't count parens inside brackets
            else if (brackets == 0 && ch == '(') parens--; // as above

            // when parens hits 0, it means the initial (prevSpecial) ')' has been closed
            if (parens == 0) {
                childrenEnd = i;
                break;
            }
        }
        if (childrenEnd == -1) throw new IllegalArgumentException("Missing opening parenthesis for children of " + current.label);
        String sC = s.substring(childrenEnd + 1, prevSpecial); // string containing all current children
        List<Integer> childSeps = new ArrayList<>();
        brackets = parens = 0;
        for (int i = 0; i < sC.length(); i++) {
            char ch = sC.charAt(i);
            if (ch == '[') brackets++;
            else if (ch == ']') brackets--;
            else if (brackets == 0 && ch == '(') parens++;
            else if (brackets == 0 && ch == ')') parens--;

            if (brackets == 0 && parens == 0 && ch == ',') {
                childSeps.add(i);
            }
        }
        if (childSeps.isEmpty()) {
            fromStringRecursive(sC, current); // single child
        } else {
            int start = 0;
            int next = 0;
            for (int i : childSeps) {
                next = i;
                fromStringRecursive(sC.substring(start, next + 1), current); // children up to the last ','
                start = next;
            }
            fromStringRecursive(sC.substring(next, sC.length()), current); // child from last ',' to the ')'
        }

        return current;

    }


    private <S> String stateString(CharacterList<S> root) {
        if (root == null) return "";
        Iterator<Set<S>> it = root.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) { // for each character
            Iterator<S> intIt = it.next().iterator();
            boolean first = true;
            while (intIt.hasNext()) { // for each state in potential set of states
                if (!first) {
                    sb.append(",");
                }
                sb.append(intIt.next());
                first = false;
            }
        }
        return sb.toString();
    }
}
