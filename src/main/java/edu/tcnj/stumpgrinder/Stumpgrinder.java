package edu.tcnj.stumpgrinder;

import edu.tcnj.stumpgrinder.algo.Fitch;
import edu.tcnj.stumpgrinder.data.Node;

public class Stumpgrinder {

    private static Parser parser = new Parser();

    public static void main(String[] args) {
        System.out.println("Hello World!");

        testFitch();
    }

    private static void testFitch() {
        Node<Integer> root = makeTestTree();
        int score = Fitch.bottomUp(root);

        System.out.println("Test tree: " + parser.toString(root, true));
        System.out.println(score);
    }

    private static Node<Integer> makeTestTree() {
        Node<Integer> root = new Node<>("");
        Node<Integer> left = new Node<>("L");
        Node<Integer> right = new Node<>("R");

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

}