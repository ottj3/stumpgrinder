package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class MixedTreeEnumeratorTest extends EnumeratorTest {
    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList(4, 5, 6, 7, 8, 9, 10);
    }

    @Parameterized.Parameter
    public int treeSize = 0;
    @Ignore
    @Test
    public void testMixedEnumerator() {
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
        double denominator = Math.sqrt(2) * Math.exp(treeSize / 2) * Math.pow((2 - Math.exp(0.5)), treeSize - 1.5);
        double expectedSize = Math.pow(treeSize, treeSize - 2) / denominator;
        //if (i == 1) expectedScore = 1;
        Set<Node<Integer>> treeList = treeEnumerator.enumerate();

//        for (Node<Integer> tree : treeList) {
//            System.out.println(parser.toString(tree, false));
//        }
        //System.out.println("Size: " + treeEnumerator.completed + " Expected: " + expectedSize);
    }

    @Test
    public void testHartigan() {
        getData(treeSize);
        Parser parser = new Parser();
        TreeEnumerator<Character> treeEnumerator = new MixedTreeEnumerator<>(species, worldSet);
        Set<Node<Character>> treeList = treeEnumerator.hartiganEnumerate();
        System.out.println("Hartigan enumerate: ");
        for (Node<Character> tree : treeList) {
            System.out.println(parser.toString(tree, false) + " Score: " + Hartigan.bottomUp(tree, worldSet) + " Size: " + tree.size());
        }
    }

}
