package edu.tcnj.stumpgrinder.algo;


import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HartiganTest {

    public Parser parser = new Parser();

    public CharacterList<Character> worldSet = new CharacterList<>();
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
    public void setup() {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            lines.add(TreeEnumeratorTest.testData.get(i));
        }

        Parser parser = new Parser();
        List<Set<Character>> worldSet0 = new ArrayList<>();
        parser.speciesList(lines, species, worldSet0);
        worldSet = new CharacterList<>(worldSet0);
        makeTree();
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
    }
}