package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class EdgeContractorTest {

    //Enumerate all mixed trees
    //All cubic trees
    //(returned: most parsimonious of both)
    //Contract all cubic trees given
    //Compare size to most compact of mixed
    public Parser parser = new Parser();

    @Test
    public void getTimingData() {
        int NUM_TRIALS = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<long[]>> callables = new ArrayList<>();
        List<Future<long[]>> futures;
        int[] treeSizes = {4, 5, 6, 7/*, 8, 9/*, 10, 11, 12, 13, 14/*, 15*/};
        for (final int treeSize : treeSizes) {
            for (int i = 0; i < NUM_TRIALS; i++) {
                List<String> lines = new ArrayList<>();
                Collections.shuffle(TreeEnumeratorTest.testData);
                for (int i1 = 0; i1 < treeSize; i1++) {
                    lines.add(TreeEnumeratorTest.testData.get(i1));
                }

                final double[][] weights = {
                        {0, 1, 1, 1},
                        {1, 0, 1, 1},
                        {1, 1, 0, 1},
                        {1, 1, 1, 0}
                };
                final List<Node> species = parser.speciesList(lines);
                callables.add(new Callable<long[]>() {
                    @Override
                    public long[] call() throws Exception {
                        return runMixed(species, weights);
                    }
                });

                final List<Node> species0 = parser.speciesList(lines);
                callables.add(new Callable<long[]>() {
                    @Override
                    public long[] call() throws Exception {
                        return runCubic(species0, weights);
                    }
                });
            }

        }

        try {
            futures = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("n\tmixed time\tcubic time\t# compact mixed\t# mp cubic\t# compact\t# contractions");
        long[][] averageResults = new long[treeSizes.length][7];
        for (int i = 0; i < futures.size(); i += 2) {
            long[] res1;
            long[] res2;
            try {
                res1 = futures.get(i).get(); //{n, mixed time, # compact mixed}
                res2 = futures.get(i + 1).get(); //{cubic time, # mp cubic, # compact, # contractions}
                int treeSize = (int) res1[0];
                int index = treeSize - treeSizes[0];

                //I'm so sorry.
                averageResults[index][0] += treeSize; //n
                averageResults[index][1] += res1[1]; //mixed time
                averageResults[index][2] += res2[0]; //cubic time
                averageResults[index][3] += res1[2]; //# compact mixed
                averageResults[index][4] += res2[1]; //# mp cubic
                averageResults[index][5] += res2[2]; //# compact
                averageResults[index][6] += res2[3]; //# contractions
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < averageResults.length; i++) {
            for (int i1 = 0; i1 < 3; i1++) {
                System.out.print(Math.round((float) averageResults[i][i1] / NUM_TRIALS) + "\t");
            }
            for (int i1 = 3; i1 < averageResults[i].length; i1++) {
                System.out.print(((float) averageResults[i][i1] / NUM_TRIALS) + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void testContraction() {
        final int NUM_TRIALS = 10;
        int[] treeSizes = {4, 5, 6, 7/*, 8, 9/*, 10, 11, 12, 13, 14/*, 15*/};
        for (final int treeSize : treeSizes) {
            for (int i = 0; i < NUM_TRIALS; i++) {
                List<String> lines = new ArrayList<>();
                Collections.shuffle(TreeEnumeratorTest.testData);
                for (int i1 = 0; i1 < treeSize; i1++) {
                    lines.add(TreeEnumeratorTest.testData.get(i1));
                }

                final double[][] weights = {
                        {0, 1, 2, 3},
                        {1, 0, 3, 2},
                        {2, 3, 0, 1},
                        {3, 2, 1, 0}
                };
                final List<Node> species = parser.speciesList(lines);

                CubicTreeEnumerator cubicTreeEnumerator = new CubicTreeEnumerator(species, weights);
                Set<Node> mostCompactCubic = compactCubic(cubicTreeEnumerator.sankoffEnumerate(), weights);
                int cubicSize = mostCompactCubic.iterator().next().size();

                MixedTreeEnumerator mixedTreeEnumerator = new MixedTreeEnumerator(species, weights);
                Set<Node> mostCompactMixed = compactMixed(mixedTreeEnumerator.sankoffEnumerate());
                int mixedSize = mostCompactMixed.iterator().next().size();
                assertEquals("Cubic contracted to " + cubicSize + " while mixed were of size " + mixedSize,
                        mixedSize, cubicSize);
            }

        }
    }

    public long[] runMixed(List<Node> species, double[][] weights) {
        long before = System.currentTimeMillis();
        MixedTreeEnumerator treeEnumerator = new MixedTreeEnumerator(species, weights);
        Set<Node> mostParsimonious = treeEnumerator.sankoffEnumerate();
        Set<Node> mostCompact = compactMixed(mostParsimonious);


        long time = System.currentTimeMillis() - before;
//        System.out.println("Took " + time + "ms for mixed trees with " + species.size() + " input species.");
        return new long[]{species.size(), time, mostCompact.size()};
    }

    private Set<Node> compactMixed(Set<Node> mostParsimonious) {
        Set<Node> mostCompact = new HashSet<>();
        int mostCompactSize = Integer.MAX_VALUE;
        for (Node tree : mostParsimonious) {
            int thisSize = tree.size();
            if (thisSize <= mostCompactSize) {
                if (tree.size() < mostCompactSize) {
                    mostCompact.clear();
                }
                mostCompact.add(tree);
                mostCompactSize = thisSize;
            }
        }
        return mostCompact;
    }

    public long[] runCubic(List<Node> species, double[][] weights) {
        long before = System.currentTimeMillis();
        CubicTreeEnumerator treeEnumerator = new CubicTreeEnumerator(species);
        Set<Node> mostParsimonious = treeEnumerator.sankoffEnumerate();
        Set<Node> mostCompact = compactCubic(mostParsimonious, weights);
        int initialSize = mostParsimonious.iterator().next().size();
        int numContractions = initialSize - mostCompact.iterator().next().size();
        long time = System.currentTimeMillis() - before;
//        System.out.println("Took " + time + "ms for cubic trees with " + species.size() + " input species.");
        return new long[]{time, mostParsimonious.size(), mostCompact.size(), numContractions};
    }

    private Set<Node> compactCubic(Set<Node> mostParsimonious, double[][] weights) {
        Set<Node> mostCompact = new HashSet<>();
        int mostCompactSize = Integer.MAX_VALUE;
        for (Node tree : mostParsimonious) {
            EdgeContractor edgeContractor = new EdgeContractor(weights);
            Node compactTree = edgeContractor.edgeContraction(tree);
            int thisSize = compactTree.size();
            if (thisSize <= mostCompactSize) {
                if (compactTree.size() < mostCompactSize) {
                    mostCompact.clear();
                }
                mostCompact.add(compactTree);
                mostCompactSize = thisSize;
            }
        }
        return mostCompact;
    }
}
