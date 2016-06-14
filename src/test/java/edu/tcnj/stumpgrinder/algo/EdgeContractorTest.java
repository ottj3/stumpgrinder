package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EdgeContractorTest {

    //Enumerate all mixed trees
    //All cubic trees
    //(returned: most parsimonious of both)
    //Contract all cubic trees given
    //Compare size to most compact of mixed
    public Parser parser = new Parser();

    @Test
    public void testContraction() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<String>> callables = new ArrayList<>();
        List<Future<String>> futures;
        int[] treeSizes = {4, 5, 6, 7, 8, 9/*, 10, 11, 12, 13, 14, 15*/};
        for (final int treeSize : treeSizes) {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < treeSize; i++) {
                lines.add(TreeEnumeratorTest.testData.get(i));
            }

            final CharacterList<Character> worldSet;
            final List<Node<Character>> species = new ArrayList<>();
            List<Set<Character>> worldSet0 = new ArrayList<>();
            parser.speciesList(lines, species, worldSet0);
            worldSet = new CharacterList<>(worldSet0);
            callables.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return runMixed(species, worldSet);
                }
            });

            final CharacterList<Character> worldSet1;
            final List<Node<Character>> species0 = new ArrayList<>();
            List<Set<Character>> worldSet2 = new ArrayList<>();
            parser.speciesList(lines, species0, worldSet2);
            worldSet1 = new CharacterList<>(worldSet2);
            callables.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return runCubic(species0, worldSet1);
                }
            });
        }

        try {
            futures = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("n\tmixed time\t# compact mixed\tcubic time\t# mp cubic\t# compact\t# contractions");
        for (Future<String> future : futures) {
            String res = "";
            try {
                res = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (!res.isEmpty()) {
                System.out.print(res);
            }
        }
    }

    public String runMixed(List<Node<Character>> species, CharacterList<Character> worldSet) {
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
        return species.size() + "\t" + time + "\t" + mostCompact.size() + "\t";
    }

    public String runCubic(List<Node<Character>> species, CharacterList<Character> worldSet) {
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
        return time + "\t" + mostParsimonious.size() + "\t" + mostCompact.size() + "\t" + numContractions + "\n";
    }
}
