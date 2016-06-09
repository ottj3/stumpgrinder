package edu.tcnj.stumpgrinder;


import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private Parser parser;
    private Node<Integer> genTree;

    @Before
    public void setup() {
        parser = new Parser();
        genTree = genTestTree();
    }

    @Test
    public void parseSimpleTree() {
        String[] treeStrings = {
            ";",
            "A:0;",
            "(((B:1)C:1,D:1):1)A:0;",
            "(((D:1)B:1)C:1)A:0;",
            "((C:1,(B:1,D:1):0):1)A:0;",
            "((C:0,(E:0,F:0)D:0)B:0)A:0;"
        };

        for (String treeString : treeStrings) {
            Node<?> root = parser.fromString(treeString);
            String out = parser.toString(root, false);
            assertEquals("Simple tree re-parsing", treeString, out);
        }

    }

    @Test
    public void parseDataTree() {
        String[] treeStrings = {
            "A[[1][2][]]:0;",
            "(B[[1,2][][]]:1,C[[1,3][][]]:1)A[[][][]]:0;",
            "((C[[A][A,B][]]:0,D[[A][B][]]:1)B[[A][A,B][]]:1)A[[A:B][][]]:0;"
        };

        for (String treeString : treeStrings) {
            Node<?> root = parser.fromString(treeString);
            String out = parser.toString(root, true);
            assertEquals("Data-labelled tree re-parsing", treeString, out);
        }
    }

    @Test
    public void parseDataNode() {
        String[] nodeStrings = {
            "A[[1|2|a|0|50|x][][]]",
            "A[[1,2,3|3,4,5][][]]",
            "A[[y,x|y,x|x][z,x|z,x|y,x][0,1,2|0,1|0,1]]", // technically sets are unordered, so input string
            // order does matter when comparing to output string. in practice, this won't matter since
            // labelled input nodes should only have one (known) state for each character
        };

        for (String nodeString : nodeStrings) {
            Node<?> node = parser.nodeFromLabel(nodeString, 0);
            String out = parser.nodeToString(node, true);
            assertEquals("Data-labelled node re-parsing", nodeString + ":0", out);
        }
    }

    @Test
    public void parseGenTree() {
        String genOut = parser.toString(genTree, false);

        Node<?> root = parser.fromString(genOut);
        String genOutAgain = parser.toString(root, false);

        assertEquals("Generated tree re-parsing (leaves: " + leaves + ")", genOut, genOutAgain);
    }

    private static Node<Integer> genTestTree() {
        Node.chars = 5;
        Node<Integer> root = new Node<>("");
        genRecursive(root);
        return root;
    }

    private static int leaves = 0;
    private static void genRecursive(Node<Integer> current) {
        if (Math.random() < 0.5) {
            Node<Integer> left = new Node<>("");
            current.children.add(left);
            left.parent = current;
            genRecursive(left);
        }
        if (Math.random() < 0.5) {
            Node<Integer> right = new Node<>("");
            current.children.add(right);
            right.parent = current;
            genRecursive(right);
        }
        if (current.children.isEmpty()) {
            current.label = String.valueOf(leaves++);
            current.root = Node.sets();
            for (int i = 0; i < Node.chars; i++) {
                current.root.get(i).add((int) (Math.random() * 3));
            }
        }
    }
}
