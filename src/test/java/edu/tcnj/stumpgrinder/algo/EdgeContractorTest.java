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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(Parameterized.class)
public class EdgeContractorTest {

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


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
        System.out.print(treeSize + "\t");
//        System.out.println("===========MIXED TREES===========");
        runMixed();
//        System.out.println("\n===========CUBIC TREES===========");
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
//        for (Node<Character> tree : mostCompact) {
//            System.out.println(parser.toString(tree, false)+ " size: " + mostCompactSize);
//        }
//        int parsimonyScore = Hartigan.bottomUp(mostCompact.iterator().next(), worldSet);
//        System.out.println("Found " + mostCompact.size() + " most compact tree(s) from " + mostParsimonious.size()
//                + " mixed MP tree(s)");
//        System.out.println("Parsimony score: " + parsimonyScore);
        long time = System.currentTimeMillis() - before;
        System.out.print(time + "\t" + mostCompact.size() + "\t");
//        System.out.println("Took " + time + "ms for mixed trees with " + treeSize + " input species.");
    }

    public void runCubic() {
        long before = System.currentTimeMillis();
        CubicTreeEnumerator<Character> treeEnumerator = new CubicTreeEnumerator<>(species);
        Set<Node<Character>> mostParsimonious = treeEnumerator.fitchEnumerate();
        List<Node<Character>> mostCompact = new ArrayList<>();
        int numContractions = 0;
        int mostCompactSize = Integer.MAX_VALUE;
        int initialSize = mostParsimonious.iterator().next().size();
        List<Callable<Node<Character>>> callables = new ArrayList<>();
        List<Future<Node<Character>>> futures = new ArrayList<>();
        int i = 0;
        for (final Node<Character> tree : mostParsimonious) {
            final EdgeContractor<Character> edgeContractor = new EdgeContractor<>(worldSet);
            final int num = i++;
            callables.add(new Callable<Node<Character>>() {
                @Override
                public Node<Character> call() throws Exception {
                    System.out.println("Running contractor on tree #" + num);
                    return edgeContractor.edgeContraction(tree);
                }
            });
        }
        try {
            futures = executor.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Future<Node<Character>> nodeFuture : futures) {
            Node<Character> compactTree;
            try {
                compactTree = nodeFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return;
            }
            int thisSize = compactTree.size();
            if (thisSize <= mostCompactSize) {
                if (compactTree.size() < mostCompactSize) {
                    mostCompact.clear();
                }
                mostCompact.add(compactTree);
                numContractions = initialSize - compactTree.size();
                mostCompactSize = thisSize;
            }
        }
//        for (int i = 0; i < mostCompact.size(); i++) {
//            System.out.println(parser.toString(mostCompact.get(i), false)+ " size: " + mostCompactSize +
//                    " after " + numContractions + " contractions.");
//        }
//        int parsimonyScore = Hartigan.bottomUp(mostCompact.iterator().next(), worldSet);
//        System.out.println("Found " + mostCompact.size() + " most compact tree(s) from " + mostParsimonious.size()
//                + " cubic MP tree(s)");
//        System.out.println("Parsimony score: " + parsimonyScore);
        long time = System.currentTimeMillis() - before;
//        System.out.println("Took " + time + "ms for cubic trees with " + treeSize + " input species.");
        System.out.print(time + "\t" + mostParsimonious.size() + "\t" + mostCompact.size()  + "\t" + numContractions);
    }
}
