package edu.tcnj.stumpgrinder.algo;


import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HartiganTest {

    public Parser parser = new Parser();

    public ArrayList<String> labels = new ArrayList<>();
    public ArrayList<String> data = new ArrayList<>();
    public List<Set<Character>> worldSet = new ArrayList<>();
    public List<Node<Character>> species = new ArrayList<>();
    public Node<Character> root = new Node<>("");
    @Test
    public void testHartigan() throws IOException {
        int score = Hartigan.bottomUp(root, worldSet);
        Hartigan.topDown(root);
        System.out.println("Test tree: " + parser.toString(root, false));
        assertEquals("Hartigan score", 25, score);
    }

    @Before
    public void getData() {
        List<String> lines = new ArrayList<>();
        lines.add("A:GAGGACCCCAGATATTACGCGGGTCGAACA");
        lines.add("B:GAAGATCCCAGATACTTTGCCGGAGAACAA");
        lines.add("C:GAGGATCCGCGTTACTTTAGCGGTATTCAA");
        lines.add("D:GAGGACCCCCGTTACTTTGCCGGCGAGGCC");
		/* Processes the data. */
        for(String line : lines) {
            if (line != null && line.length() > 0) {
                labels.add(line.split(":", 2)[0]);
                data.add(line.split(":", 2)[1]);
            }
        }
        makeNodes();
        makeTree();
    }


    public void makeNodes() {
        Node.chars = data.get(0).length();
        for (int index = 0; index < data.get(0).length(); index++) {
            worldSet.add(new HashSet<Character>());
        }

        for (int index = 0; index < labels.size(); index++) {
            Node<Character> node = new Node<>(labels.get(index));
            node.labelled = true;
            CharacterList<Character> characters = new CharacterList<>();
            //System.out.print(labels.get(index) + ": ");
            for (int index_ = 0; index_ < data.get(index).length(); index_++) {
                //System.out.println(data.get(index).length());
                characters.add(new HashSet<Character>());
                characters.get(index_).add(data.get(index).charAt(index_));
                while(index_ >= worldSet.size()) {
                    worldSet.add(new HashSet<Character>());
                }
                worldSet.get(index_).add(data.get(index).charAt(index_));
                //System.out.print(data.get(index).charAt(index_));
            }
            node.root = characters;
            // System.out.print(" root.size(): " + node.root.size());

            // Node<List<SetList<Character>>> node = new Node<List<SetList<Character>>>(
            // 		labels.get(index), sets);
            species.add(node);
            //System.out.println();
        }
    }


    public void makeTree() {

        Node<Character> unlabelled1 = new Node<>("");
        Node<Character> unlabelled2 = new Node<>("");
        Node<Character> A = species.get(0);
        Node<Character> B = species.get(1);
        Node<Character> C = species.get(2);
        Node<Character> D = species.get(3);

        species.add(unlabelled1);
        species.add(unlabelled2);
        //(B,C),D))A : score = 25
        A.children.add(unlabelled1);
        unlabelled1.parent = A;

        unlabelled1.children.add(unlabelled2);
        unlabelled2.parent = unlabelled1;

        unlabelled1.children.add(D);
        D.parent = unlabelled1;

        unlabelled2.children.add(B);
        B.parent = unlabelled2;

        unlabelled2.children.add(C);
        C.parent = unlabelled2;

        root = A;

        //(((B)C,D))A : score = 31
        // A.children.add(unlabelled1);
        // unlabelled1.parent = A;

        // unlabelled1.children.add(D);
        // D.parent = unlabelled1;

        // unlabelled1.children.add(C);
        // C.parent = unlabelled2;

        // C.children.add(B);
        // B.parent = C;
    }
}