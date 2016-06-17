package edu.tcnj.stumpgrinder;

import edu.tcnj.stumpgrinder.algo.CubicTreeEnumerator;
import edu.tcnj.stumpgrinder.algo.EdgeContractor;
import edu.tcnj.stumpgrinder.algo.MixedTreeEnumerator;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.*;
import java.util.concurrent.*;

public class Stumpgrinder {

    public static void main(String[] args) {
        (new Stumpgrinder()).testContraction();
    }

    public Parser parser = new Parser();

    private static String print(long[] res) {
        String s = "";
        for (int i = 0; i < res.length; i++) {
            s += res[i] + "\t";
        }
        return s;
    }
    public void testContraction() {
        long startTime = System.currentTimeMillis();
        int NUM_TRIALS = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<long[]>> callables = new ArrayList<>();
        List<Future<long[]>> futures;
        int[] treeSizes = {4, 5, 6, 7, 8, 9, 10, 11/*, 12, 13, 14/*, 15*/};
        for (final int treeSize : treeSizes) {
            for (int i = 0; i < NUM_TRIALS; i++) {
                List<String> lines = new ArrayList<>();
                Collections.shuffle(testData);
                for (int i1 = 0; i1 < treeSize; i1++) {
                    lines.add(testData.get(i1));
                }

                final CharacterList<Character> worldSet;
                final List<Node<Character>> species = new ArrayList<>();
                List<Set<Character>> worldSet0 = new ArrayList<>();
                parser.speciesList(lines, species, worldSet0);
                worldSet = new CharacterList<>(worldSet0);
                callables.add(new Callable<long[]>() {
                    @Override
                    public long[] call() throws Exception {
                        long[] res = runMixed(species, worldSet);
                        System.out.print("Mixed n: " + treeSize + "\t" + print(res) + "\n");
                        return res;
                    }
                });

                final CharacterList<Character> worldSet1;
                final List<Node<Character>> species0 = new ArrayList<>();
                List<Set<Character>> worldSet2 = new ArrayList<>();
                parser.speciesList(lines, species0, worldSet2);
                worldSet1 = new CharacterList<>(worldSet2);
                callables.add(new Callable<long[]>() {
                    @Override
                    public long[] call() throws Exception {
                        long[] res = runCubic(species0, worldSet1);
                        System.out.println("Cubic n: " + treeSize + "\t" + print(res) + "\n");
                        return res;
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
        System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    public long[] runMixed(List<Node<Character>> species, CharacterList<Character> worldSet) {
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

        long time = System.currentTimeMillis() - before;
        return new long[]{species.size(), time, mostCompact.size()};
    }

    public long[] runCubic(List<Node<Character>> species, CharacterList<Character> worldSet) {
        long before = System.currentTimeMillis();
        CubicTreeEnumerator<Character> treeEnumerator = new CubicTreeEnumerator<>(species);
        Set<Node<Character>> mostParsimonious = treeEnumerator.fitchEnumerate();
        List<Node<Character>> mostCompact = new ArrayList<>();
        int numContractions = 0;
        int mostCompactSize = Integer.MAX_VALUE;
        int initialSize = mostParsimonious.iterator().next().size();
        for (Node<Character> tree : mostParsimonious) {
            EdgeContractor<Character> edgeContractor = new EdgeContractor<>(worldSet);
            Node<Character> compactTree = edgeContractor.edgeContraction(tree);
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
        return new long[]{time, mostParsimonious.size(), mostCompact.size(), numContractions};
    }

    public static List<String> testData = new ArrayList<>();
    static {
        testData.add("A:GAGGACCCCAGATATTACGCGGGTCGAACA");
        testData.add("B:GAAGATCCCAGATACTTTGCCGGAGAACAA");
        testData.add("C:GAGGATCCGCGTTACTTTAGCGGTATTCAA");
        testData.add("D:GAGGACCCCCGTTACTTTGCCGGCGAGGCC");
        testData.add("E:GAGGATCCCAGATATTTTGCGGGTGAGGCT");
        testData.add("F:GAAGACCCGCGCTACTTTGCCGGCACCGGC");
        //      testData.add("G:GAAGATCC?CGTTTCTTCGCAGGAGAAGCA"); // true sequence with ? = A or G
        //      testData.add("G:GAAGATCC{AG}CGTTTCTTCGCAGGAGAA"); // original sequence without last three chars
        testData.add("G:GAAGATCCCAGACGTTTCTTCGCAGGAGAA"); // Angela's replaced { with C and } with A
        testData.add("H:GAAGATCCACGCTACTATGCAGGACCTCAA");
        testData.add("I:GAAGACCCTCGCTATTACGCCGGTCCGCAA");
        testData.add("J:GAGGACCCACGATATTACGCGGGAGAAGGA");
        testData.add("K:GAGGATCCGCGCTACTTTGCCGGCCCGCAG");
        testData.add("L:GAAGACCCGCGATATTTTGCCGGAGAATCA");
        testData.add("M:GAAGATCCTCGATATTTTGCCGGTCCGCAA");
        testData.add("N:GAAGATCCTCGATATTTTGCCGGTCCGCAA");
        testData.add("O:GAAGACCCGCGTTATTTTGCCGGTACCAGC");
        testData.add("P:GAGGACCCGAGAATGTTCGCTGGCGTTGCC");
        testData.add("Q:GAGGATCCTAGGTTTTATGCGGGCGAGGGC");
        testData.add("R:GAAGACCCACGTTATTTCGCCGGCACCAGC");
        testData.add("S:GAGGACCCCAGATATTTTGCGGGTGAGGCT");
        testData.add("T:GAAGACCCGCGTTACTATGCGGGCACAGAT");
        testData.add("U:GAGGACCCGCGTTACTATGCGGGCACAGAC");
        testData.add("V:GAAGACCCGCGTTACTATGCGGGCACAGAT");
        testData.add("W:GAAGACCCGCGCTACTTTGCCGGCACCGGC");
        testData.add("X:AAGGACCCTTGTTATATTTCCGGCCCGCGT");
        testData.add("Y:GAGGACCCGCGCTACTTCGCGGGCGAAGGA");
        testData.add("Z:GAGGACCCGCGTTACTATGCGGGCACAGAT");
    }
}