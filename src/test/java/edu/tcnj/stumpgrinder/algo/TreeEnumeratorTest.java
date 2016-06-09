package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TreeEnumeratorTest {
    public ArrayList<String> labels = new ArrayList<>();
    public ArrayList<String> data = new ArrayList<>();
    public CharacterList<Character> worldSet = new CharacterList<>();
    public List<Node<Character>> species = new ArrayList<>();
    @Test
    public void testEnumerator() {
        for (int i = 1; i <= 10; i++) {
            testEnumerator(i);
        }
        for (int i = 1; i <= 10; i++) {
            testFitch(i);
            //testHartigan(i);
        }
    }

    public void testEnumerator(int treeSize) {
        List<Node<Integer>> treeNodes = new ArrayList<>();
        TreeEnumerator<Integer> treeEnumerator;
        Parser parser = new Parser();
        for (int i = 1; i <= treeSize; i++) {
            treeNodes.add(new Node<Integer>(((Integer) i).toString()));
        }
        treeEnumerator = new TreeEnumerator<>(new ArrayList<>(treeNodes));
        int expectedScore = 1;
        for (int j = 2 * treeSize - 5; j > 0; j -= 2) {
            expectedScore *= j;
        }
        //if (i == 1) expectedScore = 1;
        Set<Node<Integer>> treeList = treeEnumerator.enumerate();
//        for (Node<Integer> tree : treeList) {
//            System.out.println(parser.toString(tree, false));
//        }
        assertEquals("Size: " + treeNodes.size(), expectedScore, treeList.size());
    }

    public void testFitch(int dataSize) {
        getData(dataSize);
        Parser parser = new Parser();
        TreeEnumerator<Character> treeEnumerator = new TreeEnumerator<>(species);
        Set<Node<Character>> treeList = treeEnumerator.fitchEnumerate();
//        System.out.println("Fitch enumerate: ");
        for (Node<Character> tree : treeList) {
            System.out.println(parser.toString(tree, false) + " Score: " + Fitch.bottomUp(tree));
        }
    }

    public void testHartigan(int dataSize) {
        getData(dataSize);
        Parser parser = new Parser();
        TreeEnumerator<Character> treeEnumerator = new TreeEnumerator<>(species, worldSet);
        Set<Node<Character>> treeList = treeEnumerator.hartiganEnumerate();
//        System.out.println("Hartigan enumerate: ");
//        for (Node<Character> tree : treeList) {
//            System.out.println(parser.toString(tree, false) + " Score: " + Hartigan.bottomUp(tree, worldSet));
//        }
    }

    public void getData(int dataSize) {
        List<String> lines = new ArrayList<>();
        lines.add("A:GAGGACCCCAGATATTACGCGGGTCGAACA");
        lines.add("B:GAAGATCCCAGATACTTTGCCGGAGAACAA");
        lines.add("C:GAGGATCCGCGTTACTTTAGCGGTATTCAA");
        lines.add("D:GAGGACCCCCGTTACTTTGCCGGCGAGGCC");
        lines.add("E:GAGGATCCCAGATATTTTGCGGGTGAGGCT");
        lines.add("F:GAAGACCCGCGCTACTTTGCCGGCACCGGC");
        lines.add("G:GAAGATCCACGTTTCTTCGCAGGAGAAGCA");
//      lines.add("G:GAAGATCC{AG}CGTTTCTTCGCAGGAGAA");
        lines.add("H:GAAGATCCACGCTACTATGCAGGACCTCAA");
        lines.add("I:GAAGACCCTCGCTATTACGCCGGTCCGCAA");
        lines.add("J:GAGGACCCACGATATTACGCGGGAGAAGGA");
        lines.add("K:GAGGATCCGCGCTACTTTGCCGGCCCGCAG");
        labels = new ArrayList<>();
        data = new ArrayList<>();
        worldSet = new CharacterList<>();
        species = new ArrayList<>();

        /* Processes the data. */
        for (int i = 0; i < dataSize; i++) {
            String line = lines.get(i);
            if (line != null && line.length() > 0) {
                labels.add(line.split(":", 2)[0]);
                data.add(line.split(":", 2)[1]);
            }
        }
        makeNodes();
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
                while (index_ >= worldSet.size()) {
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
}
