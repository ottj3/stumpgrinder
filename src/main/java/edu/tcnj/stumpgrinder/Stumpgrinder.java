package edu.tcnj.stumpgrinder;

import edu.tcnj.stumpgrinder.algo.Fitch;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;
import edu.tcnj.stumpgrinder.data.Parser;

public class Stumpgrinder {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        testParse();

        testFitch();
    }

    private static void testFitch() {
        Node<Integer> root = makeTestTree();
        int score = Fitch.bottomUp(root);

        System.out.println("Test tree: " + Parser.toString(root, true));
        System.out.println(score);

        Node<Integer> root2 = genTestTree();
        int score2 = Fitch.bottomUp(root2);

        System.out.println("Gen tree: " + Parser.toString(root2, false));
        System.out.println(score2);
    }

    private static Node<Integer> makeTestTree() {
        Node<Integer> root = new Node<Integer>("");
        Node<Integer> left = new Node<Integer>("L");
        Node<Integer> right = new Node<Integer>("R");

        root.children.add(left);
        root.children.add(right);
        left.parent = root;
        right.parent = root;

        Node.chars = 10;
        left.root = Node.sets();
        right.root = Node.sets();
        for (int i = 0; i < Node.chars / 2; i++) {
            left.root.get(i).add(1);
            right.root.get(i).add(2);
        }
        for (int i = Node.chars / 2; i < Node.chars; i++) {
            left.root.get(i).add(2);
            right.root.get(i).add(2);
        }
        return root;
    }

    private static Node<Integer> genTestTree() {
        Node.chars = 5;
        Node<Integer> root = new Node<Integer>("");
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

    private static Node<?> testParse() {
        String treeString = "(((B:0)C:1,D:0):1)A:0";

        Node<?> root = Parser.fromString(treeString);

        String out = Parser.toString(root, false);

        System.out.println(out);

        return root;
    }
}