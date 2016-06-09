package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MixedTreeEnumeratorTest extends TreeEnumeratorTest {
    @Test
    public void testMixedEnumerator() {
//        for(int i = 1; i <= 8; i++) {
//            testMixedEnumerator(i);
//        }
        for(int i = 1; i <= 10; i++) {
            testHartigan(i);
        }
    }
    public void testMixedEnumerator(int treeSize) {
//        CharacterList<Integer> worldSet = new CharacterList<>();
//        worldSet.add(new HashSet<Integer>());
        List<Node<Integer>> treeNodes = new ArrayList<>();
        MixedTreeEnumerator<Integer> treeEnumerator;
        Parser parser = new Parser();

        for (int i = 1; i <= treeSize; i++) {
            treeNodes.add(new Node<Integer>(((Integer) i).toString()));
//            worldSet.get(0).add(i);
        }
        treeEnumerator = new MixedTreeEnumerator<>(new ArrayList<>(treeNodes));
        double denominator = Math.sqrt(2)*Math.exp(treeSize/2)*Math.pow((2-Math.exp(0.5)),treeSize-1.5);
        double expectedSize = Math.pow(treeSize, treeSize-2)/denominator;
        //if (i == 1) expectedScore = 1;
        Set<Node<Integer>> treeList = treeEnumerator.enumerate();

//        for (Node<Integer> tree : treeList) {
//            System.out.println(parser.toString(tree, false));
//        }
        //System.out.println("Size: " + treeEnumerator.completed + " Expected: " + expectedSize);
    }
    public void testHartigan(int dataSize) {
        getData(dataSize);
        Parser parser = new Parser();
        TreeEnumerator<Character> treeEnumerator = new MixedTreeEnumerator<>(species, worldSet);
        Set<Node<Character>> treeList = treeEnumerator.hartiganEnumerate();
        System.out.println("Hartigan enumerate: ");
        for (Node<Character> tree : treeList) {
            System.out.println(parser.toString(tree, false) + " Score: " + Hartigan.bottomUp(tree, worldSet) + " Size: " + tree.size());
        }
    }

}
