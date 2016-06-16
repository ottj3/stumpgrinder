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
public class CubicTreeEnumeratorTest extends TreeEnumeratorTest {

    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList(4, 5, 6, 7, 8, 9/*, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26*/);
    }

    @Parameterized.Parameter
    public int treeSize = 0;

    @Test
    public void testEnumerator() {
        List<Node> treeNodes = new ArrayList();
        CubicTreeEnumerator treeEnumerator;
        for (int i = 1; i <= treeSize; i++) {
            treeNodes.add(new Node(((Integer) i).toString()));
        }
        treeEnumerator = new CubicTreeEnumerator(new ArrayList(treeNodes));
        int expectedScore = 1;
        for (int j = 2 * treeSize - 5; j > 0; j -= 2) {
            expectedScore *= j;
        }
//        Parser parser = new Parser();
//        if (i == 1) expectedScore = 1;
//        for (Node tree : treeList) {
//            System.out.println(parser.toString(tree, false));
//        }
        assertEquals("Size: " + treeNodes.size(), expectedScore, treeEnumerator.enumerate());
    }

    @Test
    public void testSankoff() {
        long start = System.currentTimeMillis();
        getData(treeSize);
        Parser parser = new Parser();
        CubicTreeEnumerator treeEnumerator = new CubicTreeEnumerator(species, weights);
        Set<Node> treeList = treeEnumerator.sankoffEnumerate();
//        System.out.println("Fitch enumerate: ");
        for (Node tree : treeList) {
            System.out.println(parser.toString(tree) + " Score: " + Sankoff.bottomUp(tree, weights));
            EdgeContractor edgeContractor = new EdgeContractor(weights);
            Node compacted = edgeContractor.edgeContraction(tree);
            System.out.println(parser.toString(compacted) + " Compacted Score: " + Sankoff.bottomUp(compacted, weights));
        }
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms for trees of size " + treeSize + ".");
    }


}
