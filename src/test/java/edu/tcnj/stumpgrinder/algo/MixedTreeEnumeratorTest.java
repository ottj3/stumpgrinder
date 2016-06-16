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

@RunWith(Parameterized.class)
public class MixedTreeEnumeratorTest extends TreeEnumeratorTest {
    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList(4, 5, 6, 7, 8, 9/*, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26*/);
    }

    @Parameterized.Parameter
    public int treeSize = 0;

    @Ignore
    @Test
    public void testMixedEnumerator() {
//        CharacterList worldSet = new CharacterList();
//        worldSet.add(new HashSet());
        List<Node> treeNodes = new ArrayList();
        MixedTreeEnumerator treeEnumerator;

        for (int i = 1; i <= treeSize; i++) {
            treeNodes.add(new Node(((Integer) i).toString()));
//            worldSet.get(0).add(i);
        }
        treeEnumerator = new MixedTreeEnumerator(new ArrayList(treeNodes));
        double denominator = Math.sqrt(2) * Math.exp(treeSize / 2) * Math.pow((2 - Math.exp(0.5)), treeSize - 1.5);
        double expectedSize = Math.pow(treeSize, treeSize - 2) / denominator;
        //if (i == 1) expectedScore = 1;
        int treeListSize = treeEnumerator.enumerate();

//        Parser parser = new Parser();
//        for (Node tree : treeList) {
//            System.out.println(parser.toString(tree, false));
//        }
        System.out.println("Size: " + treeListSize + " Expected: " + expectedSize);
    }

    @Test
    public void testSankoff() {
        long start = System.currentTimeMillis();
        getData(treeSize);
        Parser parser = new Parser();
        MixedTreeEnumerator treeEnumerator = new MixedTreeEnumerator(species, weights);
        Set<Node> treeList = treeEnumerator.sankoffEnumerate();
//        System.out.println("Hartigan enumerate: ");
        for (Node tree : treeList) {
            System.out.println(parser.toString(tree) + " Score: " + Sankoff.bottomUp(tree, weights) + " Size: " + tree.size());
        }
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms for trees of size " + treeSize + ".");
    }

}
