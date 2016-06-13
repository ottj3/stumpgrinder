package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(Parameterized.class)
public class EdgeContractorTest {
    //Enumerate all mixed trees
    //All cubic trees
    //(returned: most parsimonious of both)
    //Contract all cubic trees given
    //Compare size to most compact of mixed
    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList(4, 5, 6, 7, 8, 9, 10/*, 11, 12, 13, 14, 15*/);
    }

    @Parameterized.Parameter
    public int treeSize = 0;

    public CharacterList<Character> worldSet;
    public List<Node<Character>> species = new ArrayList<>();
    public Parser parser = new Parser();

    @Test
    public void testContraction() {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < treeSize; i++) {
            lines.add(TreeEnumeratorTest.testData.get(i));
        }

        List<Set<Character>> worldSet0 = new ArrayList<>();
        parser.speciesList(lines, species, worldSet0);
        worldSet = new CharacterList<>(worldSet0);

        System.out.println("===========MIXED TREES===========");
        runMixed();
        System.out.println("\n===========CUBIC TREES===========");
        runCubic();
        System.out.println();
    }

    public void runMixed() {
        long before = System.currentTimeMillis();
        MixedTreeEnumerator<Character> treeEnumerator = new MixedTreeEnumerator<>(species, worldSet);
        Set<Node<Character>> mostParsimonious = treeEnumerator.hartiganEnumerate();
        Set<Node<Character>> mostCompact = new HashSet<>();
        int mostCompactSize = -1;
        for (Node<Character> tree : mostParsimonious) {
            int thisSize = tree.size();
            if (thisSize <= mostCompactSize || mostCompactSize == -1) {
                if (tree.size() < mostCompactSize) {
                    mostCompact.clear();
                }
                mostCompact.add(tree);
                mostCompactSize = thisSize;
            }
        }
        for (Node<Character> tree : mostCompact) {
            System.out.println(parser.toString(tree, false)+ " size: " + mostCompactSize);
        }
        int parsimonyScore = Hartigan.bottomUp(mostCompact.iterator().next(), worldSet);
        System.out.println("Found " + mostCompact.size() + " most compact tree(s) from " + mostParsimonious.size()
                + " mixed MP tree(s)");
        System.out.println("Parsimony score: " + parsimonyScore);
        long time = System.currentTimeMillis() - before;
        System.out.println("Took " + time + "ms for mixed trees with " + treeSize + " input species.");
    }

    public void runCubic() {
        long before = System.currentTimeMillis();
        CubicTreeEnumerator<Character> treeEnumerator = new CubicTreeEnumerator<>(species);
        Set<Node<Character>> mostParsimonious = treeEnumerator.fitchEnumerate();
        List<Node<Character>> mostCompact = new ArrayList<>();
        List<Integer> numContractions = new ArrayList<>();
        int mostCompactSize = -1;
        for (Node<Character> tree : mostParsimonious) {
            EdgeContractor<Character> edgeContractor = new EdgeContractor<>(worldSet);
            Node<Character> compactTree = edgeContractor.edgeContraction(tree);
            int thisSize = compactTree.size();
            if (thisSize <= mostCompactSize || mostCompactSize == -1) {
                if (tree.size() < mostCompactSize) {
                    mostCompact.clear();
                    numContractions.clear();
                }
                mostCompact.add(compactTree);
                numContractions.add(tree.size() - compactTree.size());
                mostCompactSize = thisSize;
            }
        }
        for (int i = 0; i < mostCompact.size(); i++) {
            System.out.println(parser.toString(mostCompact.get(i), false)+ " size: " + mostCompactSize +
                    " after " + numContractions.get(i) + " contractions.");
        }
        int parsimonyScore = Hartigan.bottomUp(mostCompact.iterator().next(), worldSet);
        System.out.println("Found " + mostCompact.size() + " most compact tree(s) from " + mostParsimonious.size()
                + " cubic MP tree(s)");
        System.out.println("Parsimony score: " + parsimonyScore);
        long time = System.currentTimeMillis() - before;
        System.out.println("Took " + time + "ms for cubic trees with " + treeSize + " input species.");
    }
}
