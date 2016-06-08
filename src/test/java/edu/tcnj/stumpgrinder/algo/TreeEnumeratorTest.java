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
    public List<Set<Character>> worldSet = new ArrayList<>();
    public List<Node<Character>> species = new ArrayList<>();

    @Test
    public void testEnumerator() {
        for(int i = 1; i <= 5; i++) {
            testEnumerator(i);
        }
        testFitch();
    }
    public void testEnumerator(int treeSize) {
        List<Node<Integer>> treeNodes = new ArrayList<>();
        TreeEnumerator<Integer> treeEnumerator = new TreeEnumerator<>();
        Parser parser = new Parser();
        for (int i = 1; i <= treeSize; i++) {
            treeNodes.add(new Node<Integer>(((Integer) i).toString()));
        }
        treeEnumerator = new TreeEnumerator<>(new ArrayList<>(treeNodes));
        int expectedScore = 1;
        for (int j = 2*treeSize-5; j > 0; j-=2) {
            expectedScore *= j;
        }
        //if (i == 1) expectedScore = 1;
        Set<Node<Integer>> treeList = treeEnumerator.enumerate();
        for(Node<Integer> tree : treeList) {
            System.out.println(parser.toString(tree, false));
        }
        assertEquals("Size: " + treeNodes.size(), expectedScore, treeList.size());
    }

    public void testFitch() {
        getData();
        Parser parser = new Parser();
        TreeEnumerator<Character> treeEnumerator = new TreeEnumerator<>(species);
        Set<Node<Character>> treeList = treeEnumerator.fitchEnumerate();
        for(Node<Character> tree : treeList) {
            System.out.println(parser.toString(tree, false) + " Score: " + (new Fitch()).bottomUp(tree));
        }
    }
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
}
