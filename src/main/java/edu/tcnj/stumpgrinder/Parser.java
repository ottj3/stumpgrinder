package edu.tcnj.stumpgrinder;

import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
<<<<<<< HEAD
=======
import java.util.Set;
>>>>>>> 7bae4f2... Re-add parser and make a few tweaks to node.

public class Parser {

    private static final String SPECIALS = "():,;";

    /**
     * Converts a single node to a full node string, but does not recursively build a tree.
     *
     * @param node the {@link Node} to stringify
     * @return string of the Node's label and cost
     */
    public static String nodeToString(Node node) {
        return node.label + ":" + node.edgeCost;
    }

    /**
     * Converts a root (or sub-tree root) node into a Newick-format string.
     *
     * @param root top-most node of tree to convert
     * @return Newick-format tree representation
     */
    public static String toString(Node root) {
        if (root == null) return ";";
        String string = nodeToString(root) + ";";
        return toStringRecursive(root) + string;
    }

    private static String toStringRecursive(Node root) {
        String string = "";

        if (root.children.size() > 0) {
            string = ")" + string;
        }

        ListIterator<Node> it = root.children.listIterator(root.children.size());
        while (it.hasPrevious()) {
            Node child = it.previous();
            string = nodeToString(child) + string;
            string = (it.hasPrevious() ? "," : "") + toStringRecursive(child) + string;
        }

        if (root.children.size() > 0) {
            string = "(" + string;
        }

        return string;
    }

    /**
     * Converts a Newick tree string to a {@link Node} object which roots the entire tree.
     *
     * @param s Newick-formatted string
     * @return root {@link Node} of the tree
     */
    public static Node fromString(String s) {
        if (s.isEmpty())
            throw new IllegalArgumentException("Empty string can't be a Newick tree, needs at least a ';'");
        if (s.length() == 1 && s.charAt(0) == ';') return null; // empty tree is technically a valid tree
        if (s.charAt(s.length() - 1) != ';') {
            throw new IllegalArgumentException("Invalid Newick string, missing ';'");
        }
        return fromStringRecursive(s.substring(0, s.length() - 1), null);
    }

    private static Node fromStringRecursive(String s, Node parent) {
        int brackets = 0;
        int topLabelPos = s.lastIndexOf(':');
        if (topLabelPos == -1) return null; // empty tree, but should be handled already
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
        if (nextSpecial == topLabelPos + 1)
            throw new IllegalArgumentException("Found label with no cost: " + (label.isEmpty() ? "[Empty]" : label));
        String costStr = s.substring(topLabelPos + 1, nextSpecial == -1 ? s.length() : nextSpecial);
        double cost;
        try {
            cost = Double.valueOf(costStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Node " + (label.isEmpty() ? "[Empty]" : label) + " has non-number cost: " + costStr);
        }

        Node current = nodeFromLabel(label, cost);
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
        if (childrenEnd == -1) {
            throw new IllegalArgumentException("Missing opening parenthesis for children of " + current.label);
        }
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

    protected static Node nodeFromLabel(String label, double cost) {
        Node node = new Node(label);
        node.edgeCost = cost;
        return node;
    }

    public List<Node> speciesList(List<String> input) {
        List<Node> species = new ArrayList<>();
        for (String line : input) {
            String[] sp = line.split(":");
            String label = sp[0];
            String data = sp[1];
            Node node = new Node(label);
            Node.chars = data.length();
            node.setData(data);
            species.add(node);
        }
        return species;
    }

    /**
     * Recursively sets data on all labelled nodes in a tree.
     * @param root node at root of tree
     * @param labelToSequence map of node labels to the dna sequence of that species
     */
    public static void fillNodes(Node root, Map<String, String> labelToSequence) {
        for (Node child : root.children) {
            fillNodes(child, labelToSequence);
        }
        if (root.labelled && labelToSequence.containsKey(root.label)) {
            root.setData(labelToSequence.get(root.label));
        }
    }

}
