package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CubicTreeEnumeratorTest extends EnumeratorTest {
    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList(4, 5, 6, 7, 8, 9, 10);
    }
    @Parameterized.Parameter
    public int treeSize = 0;

    @Test
    public void testEnumerator() {
        List<Node<Integer>> treeNodes = new ArrayList<>();
        CubicTreeEnumerator<Integer> treeEnumerator;
        Parser parser = new Parser();
        for (int i = 1; i <= treeSize; i++) {
            treeNodes.add(new Node<Integer>(((Integer) i).toString()));
        }
        treeEnumerator = new CubicTreeEnumerator<>(new ArrayList<>(treeNodes));
        int expectedScore = 1;
        for (int j = 2 * treeSize - 5; j > 0; j -= 2) {
            expectedScore *= j;
        }
        //if (i == 1) expectedScore = 1;
//        for (Node<Integer> tree : treeList) {
//            System.out.println(parser.toString(tree, false));
//        }
        assertEquals("Size: " + treeNodes.size(), expectedScore, treeEnumerator.enumerate());
    }

    @Test
    public void testFitch() {
        getData(treeSize);
        Parser parser = new Parser();
        CubicTreeEnumerator<Character> treeEnumerator = new CubicTreeEnumerator<>(species);
        Set<Node<Character>> treeList = treeEnumerator.fitchEnumerate();
//        System.out.println("Fitch enumerate: ");
        for (Node<Character> tree : treeList) {
            System.out.println(parser.toString(tree, false) + " Score: " + Fitch.bottomUp(tree));
        }
    }

    @Test
    public void testHartigan() {
        getData(treeSize);
        Parser parser = new Parser();
        CubicTreeEnumerator<Character> treeEnumerator = new CubicTreeEnumerator<>(species, worldSet);
        Set<Node<Character>> treeList = treeEnumerator.hartiganEnumerate();
//        System.out.println("Hartigan enumerate: ");
//        for (Node<Character> tree : treeList) {
//            System.out.println(parser.toString(tree, false) + " Score: " + Hartigan.bottomUp(tree, worldSet));
//        }
    }


}
