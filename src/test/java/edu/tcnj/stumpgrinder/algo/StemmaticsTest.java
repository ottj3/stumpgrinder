package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StemmaticsTest {

    //Enumerate all mixed trees
    //All cubic trees
    //(returned: most parsimonious of both)
    //Contract all cubic trees given
    //Compare size to most compact of mixed
    public Parser parser = new Parser();
    static List<String> testData = new ArrayList<>();
    static {
        testData.add("p12:ABAAAA?AAAAAAAAAAAAAAAAACABAAAAAAAABAABAAAAAAABAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBABABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAABAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAAAAAAAB?AAAAADAABACAAAAAAAAABAAABAAAAAAAAAAAAAAAAAAAA?AAAAAAAABBBBAABAAABABAAABAAABAAAABAAABBAAAAAABAAAAAAAAAAAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAABAABAAAAAAAAAAAAAAAAAAAAAAABAAAABBAAAABAA?AAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAABBBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBAAAABBAAAACD?AAAAAB?AAAABAAAABAAAAAAAAAAAAAAAABBAABAAAABAAAAAAAAAAAABBBAC?AA?AAAAC?AAAAAABAC?AAAAAAAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAAACABACAAAABAAAAAAAAAAAAAAAAAAAAABBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAAAABBAAACAAABBAAAAAAAAAAAAACABACAAAABAAAAABAAAAAAAADCAAABAAABBBAAACAAAAAAAAAAABB");
        testData.add("p13:ABAAAA?AAAAAAAAAAABAAAAACABAAAAAAAABAABAAAAAAABAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBAAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?AAABBAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAAAAAAAB?AAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAABAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAABAABAAAAAAAAAAAAAAAAAAAAAAABAAAABBAAAABAA?AAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBAAAABBAAAACD?AAAAAB?AAAABAAAABAAAAAAAAAAAAAAAACAAABAAAABAAAAAAAAAAAABBBAC?AA?AAAAC?AAAAAABAC?AAAC?AAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAAACABACAAAABAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAAAABBAAACAAABAAAAAAAAAAAAAACABACAAAAAAAAAABAAAAAAAACCAAABAAABBBAAACAAAAAAAAAAABB");
        testData.add("p15:ABAAAA?AAAAAAAABAABAAAAACABAAAAAAAABAABAAAAAAABAAAAAAAABAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBAAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAABAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABAAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAAAAAAAB?AAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAAAAAAABAAABBAAAAAABAAAAAAAAAAAABAAAAABAAAAAB?AB?AAAAAAAAABAAAAABAAAAAAAAAAAAAAAABAAAAAAAAABAAAABBAAAABAA?AAABAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAAABBAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAACE?AAAAAC?AAAABAAAABAAAAAAAAAAAAAAAABBAABAAAABAAAAAAAAAAAABBBAC?AAAAAAABAAAACAABAC?AAAC?AAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAAACABAABAAABAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAABAAA?BAAAAABAAABAAAAABABAAAAAABAAACAAABAAAAAAAAAAAAAACABACAAAAAAAAAABAAAAAAAACEAAABAAABABAAACAAAAAAAAAAABC");
        testData.add("p16:ABAAAA?AAAAAAAABAABAABAACABAAAAAAAABAABAAAAAAABAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBAAABBAABAAAAAAAABAAAAAAAAAAAAAAAAAABAAAEAAAAAABAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAA?AAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAABAAAAB?AAAAADAABACAAAAABAAABAAAAAAAAAA?AAAAAAAAAAAAA?BAAAAAAABCABAABAAABBABAAAAAAB?AAABAAABAAAAAAAA?AAAAAAAAB?ABAAAABBAAAAABAAAAAAAAAAAAABAABAABAABAAAAAAAAAAAAAAABAAAAAAABAAAABBAAAABAA?AAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAAABAAAAAAABAAAAAAAAAAACBAAAAAAAAAAAABAAAAAABAAAAAAAAAABBAAAABBAAAACD?AAAAAB?AAAABAAAAAABAAAAAAAAAAAAAACAAABAAAABABAAAAAAAAAABBABBAAAAAAAAAAAAABAAAAC?AAAAAAAAAAAAAAAAAAA??????????CAAAAAAAAAAAAABAAAAAAAAABBDAAAABAAAAAAAABAAAAAAAAAAABBBBAAAAAABAAAAAAA?BAAABAAAAABAAAAAAABAAABAAAAAAAAAABAAAAAAAAABAAAACABAABAAAAAAAAABAAAAAAAACCAAABAAABBAAAABAAAAAAAAAAABA");
        testData.add("p2:ABAAAA?AAAAAAAABAABAAAAACABAAAAAAAABAABAAAAAAABAAAAAAAAAAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBAAABABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAABAAAAAAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAA?ABAABAAAABBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAAAAAAAB?AAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAAA?AAAAA?BAAAAAAABBBBAABAAAABAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAAAAABAAAAABAAAAAA?AB?AAAAAAAAABAAABABABBAAAAAAAAAAAAAAAAAAAAAAABAAAAABAAAABAA?AAABAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAABC?AAAAAB?AAAABAAAABAAAAAAAAAAAAAAAABBAABAAAABAAAAAAAAAAAABBBAC?AAAAAAACAAAAAAABAC?AAAC?AAAAAAAAAAAAAA??????????ABAAAAAAAAAAAABAAAAAAACABACAAAABAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAAAAABAAACAAABAAAAAAAAAAAAAACABACAAAAAAAACABAAAAAAAACCAAABAACBBBAAACAAAAAAAAAAABB");

        testData.add("p5:ABAAAA?AAACAAABBAABA?AAACABAAAAAAAABABBBAAAAAABAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBAAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAAACAAAAAABAAAAAAAAB?AAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAABC?AAAAABAAAAAAAAAAABBAAAAABAAAAAA?AB?AAAAAAAAABAAAAAAABBAAAAAAAAAAAAAAAAAAAAAAABAAAABBAAAABAA?AAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBBAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAAABAAAACAAAAAAAB?AAAABAAAAAABAAAAAAAAAAAAAAAAAABAAAABAAAAAAAAAAAABBBAD?AA?AAAABABAAAAABABADAAB?BAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAAAAABACAAAABAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAAAAAAAAABAAABAAAAABABAAAAABBAAACAAAABAAAAAAAAAAAAACACACAAAABAAAC????????????AAACAABBBBAAACAAAAAAAAAAABB");
        testData.add("p10:ABAAAA?AAAAAAAABAABA?AAACABAAAAAAABBABBBAAAAAABAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBAAABCBABAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAFAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAABAAAAAAAAAABAAAAAAAAAAAAAAAAAAABA?ACABBAAABBBAAAAAAAABBAACAAAAAAAAAAAAAAAAAAAAAABAABAAAAAAAAAAAABAAAAAAAAAAAAAAABAABABAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAAAABAAAAAAAAAAAAA?AB?AAAAAAAAABAAAAABABBAAAAAAAAAAAAAAAAAAAAAAABAAAAABAAAABAA?AAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBBAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAAABAAAACAAAAAAAC?AAAABAAAAAABAAAAAAAAAAAAAAAAAABAAAABAAAAAAAAAAAABBBAD?AA?AAAABABAAAAABCBABAAB?BAAAAAAAAAAAAAAAAAAAAAAABBAAAAAAAAAAAABAAAAAAAAABAAAAAABAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAA?CABBAAACAAAABAAAAAAAAAAAAACABACAAAABAAACABAAAAAAAACCAAABAABBBBAAACAAAAAAAAAAABB");
        testData.add("p11:ABAAAA?AAAAAAAABAABAAAAACABAAAAAAAABABBBAAAAAAAAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBEAABCBABAAAAAAAAAAAAAAAAAAAABAAAAAAAAAACAAAAAAAAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ACABBAAABBBAAAAAAAABAAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAACAAAAAAAAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAABAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAAAABBAAAAAAAAAAAAAAAAAAAAAAABAAAABBAAAABAA?AAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBAAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAABB?AAAAAB?AAAABAAAAAABAAAAAAAAAAAAAACAAABAAAABAAAAAAAAAAAABBBAA?AA?BAAADAAAAAAA???????????AAAAAAAAAAAAAAAAAAAAAADBAAAAAAAAAAAABAAAAAAAAABCCAAAAAAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAACABBAAACAAAABAAAAAAAAAAAAACACACAAAABAAAC?BAAAAAAAACDAAAAAABABBAAABAAAAAAAAAAABB");
        testData.add("p8:ABAAAA?AAAAAAAABAABAAAAACABAAAAAAAABABBBAAAAAAAAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBCAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABBAAAAAABAAAAAAAAAAAAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAABAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAAAABBAAAAAAAAAAAAAAAAAAAAAAABAAAABBAAAABAA?AAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBAAAAAAAAAAAAABAAAAAAAAAAAAAAAAABAAAAABBAAAACD?AAAAAB?AAAABAAAAAABAAAAAAAAAAAAAACAAACAAAABAAAAAAAAAAAABBBAC?AAACAAACAAAAAAABABADAAAABAAAAAAAAAAAAAAAAAAAAAAABBAAAAAAAAAAAABAAAAAAAAABCCAAAABAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAAAABBAAACAAAABAAAAAAAAAAAAACACACAAAABAAACABAAAAAAAACCAAABAABBBBAAACAACAAAAAAAAAB");
        testData.add("p6:ABABAA?AAAAAAAABAABAAAAACABAAAAAAAAAABBBAAAAAABAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBDAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAAAAAAAB?AAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAA??????????AAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAABAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAABABBAAAAAAAAAAAAAAAAAAAAAAABAAAABBAAAABAAAAAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBAAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAACD?AAAAAB?AAAABAAAABABAAAAAAAAAAAAAACAAACAAAABAAAAAAAAAAAABBBAC?AAACAAACAAAAAAABAD?DAAC?BAAAAAAAAAAAAA??????????BBAAAAAAAAAAAAAAAAAAAACAACCAAAABAAAAAAAAAAAAAAAAAAAAABBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAAAABBAAACAAAABAAAAAAAAAAAAACABACAAAABAAACABAAAAAAAAACAAAAAABBBBAAACAACAAAAAAAABB");
        testData.add("p14:ABAAAA?AAAAAAAABAABAAAAACABAAAAAAAAAABBBAAAAAABAAAAAAAABAAAAAAABAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAABBBAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAAAAAAAAAAAAAADAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAA????????????BBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAAB?ABAAAAABAAAAAA?AB?AAAAAAAAABAAAAABABBAAAAAAAAAAAAAAAAAAAAAAABAAAABBAAAABAA?AAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBAAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAABAF?AAAAAB?AAAABAAAAAABAAAAAAAAAAAAAACAAAAAAAABAAAAAAAAAAAABBBAC?AA?BAAAC?AAAAAABABACAAC?AAAAAAAAAAAAAA??????????BBAAAAAAAAAAAAAAAAAAAAAABCCAAAABAAAAAAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAAAABBAAACAAAABAAAABAAAAAAAAC?AACAAAABAAABAAAAAAAAAABBAAAAAABBBBAAACAACAAAAAAAABB");

        testData.add("p9:ABAAAC?AAAAAAAABAABAAAAABABAAAAAAAABAABAAAAAAABAAAAAAAABAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAABDAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAACAAAABAAAAAAAAAAAAAAAAABAABAAAABCAAAAAABAAAAAAAAB?AAAAACAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAABAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAABABBAAAAAAAAAAAAAAAAAAAAAAABAAAABAAAAABAA?AAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABBBAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAACD?AAAAAB?AAAABAAAABAAAAAAAAAAAAAAAACAAABAAAABAAABAAAAAAAAABBAC?AAAAAAABAAAACAACAC?AAAC?AAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAAACABCCAAAABAAAAAAAAAAAAAAAAAAAAABBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAACABBAAACAAABAAAAAAAAAAAAAACABACAAAAAAAACABAAAAAAAACCAAABAABBBBAAABAAAAAAAAAAABB");
        testData.add("p3:ABAAACAAAAAAAAACAABAAAAAAABAAAAAAAABAABAAAAABABAAAAAA?ABAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAABBDAAA?BABAAAAAAAAAAAAAAAAAAAAAAAA?AAAAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAA?AAAAAAAAAAAAAAAAAAA?ABABBAAABAAAAAAAAA??CAAAAABABAAAAAAAAAAAAABAAAAAABABAACCAAAAAAAAAAAAAAAB?AAAAACAABAAAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAABAAAAAAABBAAAAAAAABAAA?AAAAABAAAAAABAAAAAAAAABAABAAAAABAAAAAA?AB?AAAAAAAAAAAAAAABABBAAAAABAAAAAAAAAAAAAAAAABAAAAAAAAAAAA??AA?CAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAABAAABABAAAAAAA?AAAABAAAAAAAAAAAAAAAAAABAAAABAAAAACD?AAAAAAAAAAAAAAAABABAABAAAAAAAAAAACAAABAAAAAAAAAAAAAAAAABAAAE?AA?AAAABABAACAACAA?AAAAABAAAAAAAAAAAAA??????????AB?AAAAAAAAA?ABAAAAAABBABACAAAABAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAA?BAAAAACAAAAAAAAABAAAAAAABBAAACAAABAAAAAAAA?AAAAAAABACAAAABAAAAABAAAAAAAACAAAABAABBBBAAAAAABAAAAAAAACB");
        testData.add("p7:ABAAAC?AAAAAAAACAABAAAAABABAAAAAAAABAABAAAAAAABAAAAAA?ABAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAABBDAABCBABAAAAAAAABAAAAAAAAAAAAAAABAAAAAACAAAAAABAAAABAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAABABBAAABBBAAAAAAAABCAACAABABAAAAAAAAAAAAAAAAABAABABAACCAAAAAABAAAAAAAAB?AAAAACAABACAAAAABAAABAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAAABAAAAAABAAAAAAAAABAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAABABBAAAAAAAAAAAAAAAAAAAAAAABAAAABAAAAABAA?AA?CAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAAAAABABAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAACD?AAAAAAAAAAABAAAABAAAAAAAAAAAAAAAACAAABAAAAAAAAAAAAAAAAABBBAC?AA?AAAABABAACAACAC?AAAC?BAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAABBABDCAAAABAAAABAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAACABBAAACAAABAAAAAAAA?AAAAACABACAAAABAAAAABAAAAAAAACCAAABAABBBBAAAAAAAAAAAAAAABB");
        testData.add("p4:AAAAAB?AAABAAAABBABAAAAACABAAAAAAAABAAAAAAAAAABAAAAAAAABAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAABADAABCBABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAAAAAABAAAABAAAAAAAAAA?ABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAAABAABAABABAAAAAAAAAAAAAAAAABAAAAAAABBAAAAAABAAAAAAAAB?AAAAAAAAAACAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAA?BAAAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAAAAABAAAAABAAAAAA?AB?AAAAAAABABAAAAABABBAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAABAA?AAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAABAAAAAAAAAABAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAACD?AAAAAB?AAAABAAAABAAAAAAAAAAAAAAAACAAABAAAABAAAAAAAAAAAABBBAC?AAAAAAABABAACAACBC?AAAC?AAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAAACABABAAAABAAAABAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAACABBAAABAAABAAAAAAAAAAAAAABABABAAAABAAACABAAAAAAAABAAAABAAABBAAAACAAAAAAAAAAABB");
        testData.add("p1:AAAAAB?AAABAAAABBABAAAAACAAAAAAAAAABAAAAAAAAAABAAAAAAAABAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAABBDAABCBABAAAAAAAABAAAAAAAAAAAAAAAAAAAAAADAAAAAABAAAABAAAAAAAAAA?ABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?ABABBAAABBBAAAAAAAABBAABAABABAAAAAAAAAAAAAAAAABAAAAAAABCAAAAAABAAAAAAAAB?AAAAAAAABACAAAAABAAABAAAAAAAAAAAAAAAAA??????????AAAAAABBBBAABAAABBAAAAAAAABAAAABAAABBAAAAAABAAAAAAAAAAAABAAAAABAAAAAA?AB?AAAAAAAAABAAAAABABBAAAAAAAAAAAAAAAAAAAAAAABAAAACAAAAABAA?AAACAABAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAABAAAAAAABAAAAAAAAAABAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAABBAAAABBAAAACD?AABAAB?AAAABAAAABABAAAAAAAAAAAAAACAAABAAAABAAAAAAAAAAAABBBAC?AAAAAAABABAACAACBC?AAAC?AAAAAAAAAAAAAA??????????BBAAAAAAAAAAAABAAAAAAAAABABAAAABAAAABAAAAAAAAAAAAAAABBBAAAAAAAAAAAAAAA?BAAAAABAAABAAAAABABAAABABBAAABAAABAAAAAAAAAAAAAACABACAAAABABACABAAAAAAAABCAAABAAABBAAAACAAAAAAAAAAABB");
    }

    @Test
    public void testContraction() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<Callable<long[]>> callables = new ArrayList<>();
        List<Future<long[]>> futures;
        int[] treeSizes = {8};

        for (final int treeSize : treeSizes) {
            final int trialNum = 0;
            final CharacterList<Character> worldSet;
            final List<Node<Character>> species = new ArrayList<>();
            List<Set<Character>> worldSet0 = new ArrayList<>();
            parser.speciesList(testData.subList(0, treeSize), species, worldSet0);
            worldSet = new CharacterList<>(worldSet0);
            callables.add(new Callable<long[]>() {
                @Override
                public long[] call() throws Exception {
                    Node.chars = species.get(0).root.size();
                    return runMixed(species, worldSet, trialNum, -1);
                }
            });

            final CharacterList<Character> worldSet1;
            final List<Node<Character>> species0 = new ArrayList<>();
            List<Set<Character>> worldSet2 = new ArrayList<>();
            parser.speciesList(testData.subList(0, treeSize), species0, worldSet2);
            worldSet1 = new CharacterList<>(worldSet2);
            callables.add(new Callable<long[]>() {
                @Override
                public long[] call() throws Exception {
                    Node.chars = species.get(0).root.size();
                    return runCubic(species0, worldSet1, trialNum);
                }
            });

        }

        try {
            futures = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        /*System.out.println("n\tmixed time\tmixed stddev\tcubic time\tcubic stddev\t# compact mixed\t# mp cubic\t# compact\t# contractions");
        long[][] totalResults = new long[treeSizes.length][7];
        long[][][] deviation = new long[treeSizes.length][NUM_TRIALS][2];
        for (int i = 0; i < futures.size(); i += 2) {
            long[] res1;
            long[] res2;
            try {
                res1 = futures.get(i).get(); //{n, mixed time, # compact mixed}
                res2 = futures.get(i + 1).get(); //{cubic time, # mp cubic, # compact, # contractions}
                int treeSize = (int) res1[0];
                int index = treeSize - treeSizes[0];

                //I'm so sorry.
                totalResults[index][0] += treeSize; //n
                totalResults[index][1] += res1[1]; //mixed time
                deviation[index][(i / 2) % NUM_TRIALS][0] = res1[1];
                totalResults[index][2] += res2[0]; //cubic time
                deviation[index][(i / 2) % NUM_TRIALS][1] = res2[0];
                totalResults[index][3] += res1[2]; //# compact mixed
                totalResults[index][4] += res2[1]; //# mp cubic
                totalResults[index][5] += res2[2]; //# compact
                totalResults[index][6] += res2[3]; //# contractions
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < deviation.length; i++) {
            long mixedDeviation = 0;
            long cubicDeviation = 0;
//            System.out.println("Size: " + (i+treeSizes[0]));
            for (int j = 0; j < deviation[i].length; j++) {
//                System.out.print(deviation[i][j][0] + " " + deviation[i][j][1] + "\n");
                deviation[i][j][0] -= totalResults[i][1] / NUM_TRIALS;
                mixedDeviation += Math.pow(deviation[i][j][0], 2);
                deviation[i][j][1] -= totalResults[i][2] / NUM_TRIALS;
                cubicDeviation += Math.pow(deviation[i][j][1], 2);
            }
            mixedDeviation /= NUM_TRIALS;
            cubicDeviation /= NUM_TRIALS;
            deviation[i][0][0] = (long) Math.sqrt(mixedDeviation);
            deviation[i][0][1] = (long) Math.sqrt(cubicDeviation);
        }
        for (int i = 0; i < totalResults.length; i++) {
            System.out.print(Math.round((float) totalResults[i][0] / NUM_TRIALS) + "\t");
            for (int i1 = 1; i1 < 3; i1++) {
                System.out.print(Math.round((float) totalResults[i][i1] / NUM_TRIALS) + "\t");
                System.out.print(deviation[i][0][i1 - 1] + "\t");
            }
            for (int i1 = 3; i1 < totalResults[i].length; i1++) {
                System.out.print(((float) totalResults[i][i1] / NUM_TRIALS) + "\t");
            }
            System.out.println();
        }*/
    }

    public long[] runMixed(List<Node<Character>> species, CharacterList<Character> worldSet, int trialNum, int numUnlabelled) {
        long before = System.currentTimeMillis();
        MixedTreeEnumerator<Character> treeEnumerator = new MixedTreeEnumerator<>(species, worldSet, numUnlabelled);
        Map<Integer, Set<Node<Character>>> mostParsimonious = treeEnumerator.hartiganEnumerate();
        Set<Node<Character>> mostCompact = new HashSet<>();
//        int mostCompactSize = -1;
//        for (Node<Character> tree : mostParsimonious) {
//            int thisSize = tree.size();
//            if (thisSize <= mostCompactSize || mostCompactSize == -1) {
//                if (tree.size() < mostCompactSize) {
//                    mostCompact.clear();
//                }
//                mostCompact.add(tree);
//                mostCompactSize = thisSize;
//            }
//        }

        long time = System.currentTimeMillis() - before;
//        for (Node<Character> tree : mostCompact) {
//            String out = "Mixed #" + species.size() + " " + new Parser().toString(tree, false) + " " + Hartigan.bottomUp(tree, worldSet);
//            System.out.println(out);
//        }
        for (Map.Entry<Integer, Set<Node<Character>>> entry : mostParsimonious.entrySet()) {
            System.out.println("Size: " + entry.getKey() + ", score: " + Hartigan.bottomUp(entry.getValue().iterator().next(), worldSet));
        }
//        System.out.println("Mixed #" + species.size() + "-" + trialNum + "\t" + species.size() + "\t" + time + "\t" + mostCompact.size());
        return new long[]{species.size(), time, mostCompact.size()};
    }

    public long[] runCubic(List<Node<Character>> species, CharacterList<Character> worldSet, int trialNum) {
        long before = System.currentTimeMillis();
        CubicTreeEnumerator<Character> treeEnumerator = new CubicTreeEnumerator<>(species);
        Set<Node<Character>> mostParsimonious = treeEnumerator.fitchEnumerate();
        List<Node<Character>> mostCompact = new ArrayList<>();
        int numContractions = 0;
        int mostCompactSize = Integer.MAX_VALUE;
        int initialSize = mostParsimonious.iterator().next().size();
        for (Node<Character> tree : mostParsimonious) {
            EdgeContractor<Character> edgeContractor = new EdgeContractor<>(worldSet);
            for (Node<Character> compactTree : edgeContractor.edgeContraction(tree)) {
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
        for (Node<Character> tree : mostCompact) {
            String out = "Cubic #" + species.size() + " " + new Parser().toString(tree, false) + " " + Hartigan.bottomUp(tree, worldSet);
            System.out.println(out);
        }
//        System.out.println("Took " + time + "ms for cubic trees with " + treeSize + " input species.");
//        System.out.println("Cubic #" + species.size() + "-" + trialNum + "\t" + species.size() + "\t" + time + "\t" + mostParsimonious.size() +
//                "\t" + mostCompact.size() + "\t" + numContractions);
        return new long[]{time, mostParsimonious.size(), mostCompact.size(), numContractions};
    }

    @Test
    public void testByDelimeter() throws Exception{
        String[] nodes = {
//                "p1:",
                "p3:",
//                "p4:",
                "p7:",
                "p9:",

//                "p5:",
//                "p6:",
//                "p8:",
//                "p10:",
//                "p11:",
//                "p14:",

                "p2:",
                "p12:",
                "p13:",
                "p15:",
                "p16:",
        };
        int internalNodes = -1;
        String delim = "\n";
        List<String> nexusData = getNexusFromFiles(delim);
        List<String> filteredData = new ArrayList<>();
        for (String node : nodes) {
            for (String s : nexusData) {
                if (s.contains(node)) filteredData.add(s);
            }
        }

        final CharacterList<Character> worldSet;
        final List<Node<Character>> species = new ArrayList<>();
        List<Set<Character>> worldSet0 = new ArrayList<>();
        parser.speciesList(filteredData, species, worldSet0);
        worldSet = new CharacterList<>(worldSet0);
        runMixed(species, worldSet, 0, internalNodes);


        final CharacterList<Character> worldSet1;
        final List<Node<Character>> species0 = new ArrayList<>();
        List<Set<Character>> worldSet2 = new ArrayList<>();
        parser.speciesList(filteredData, species0, worldSet2);
        worldSet1 = new CharacterList<>(worldSet2);
        runCubic(species0, worldSet1, 0);
    }
    public List<String> getNexusFromFiles(String delim) throws Exception {
        List<String> nexusData = new ArrayList<>();
        List<String> raws = new ArrayList<>();
        int chars = 0;
        for (int i = 1; i <= 16; i++) {
            String fileName = "p" + i + ".txt";
            File inputFile = new File(fileName);
            FileInputStream fis = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            String raw = "";
            while ((line = br.readLine()) != null) {
                raw += line + "\n";
            }
            raw = raw.substring(0, raw.lastIndexOf('\n'));
            raws.add(raw);
            br.close();
        }
        chars = raws.get(0).split(delim).length;
        List<Map<Character,String>> nexusInfo = new ArrayList<>(chars);
        List<Character> nextChars = new ArrayList<>(chars);
        for (int i = 0; i < chars; i++) {
            nextChars.add((char) ('A' - 1));
            nexusInfo.add(new HashMap<Character, String>());
        }
        for (int i = 0; i < raws.size(); i++) {
            nexusData.add("p" + (i + 1) + ":" + Parser.rawToNexus(raws.get(i), nexusInfo, nextChars, delim));
        }
//        for (String s : nexusData) {
//            System.out.println(s + "; " + s.length());
//        }
        return nexusData;
    }

    @Test
    public void getAdjMat() {
        String fName = "correct.txt";
        File file = new File(fName);
        Parser parser = new Parser();
        Node<Character> root = parser.fromAdjacencyMatrix(file);

        /*
        List<String> filteredData = testData;
        final List<Node<Character>> species = new ArrayList<>();
        List<Set<Character>> worldSet0 = new ArrayList<>();
        parser.speciesList(filteredData, species, worldSet0);
        parser.fillNodes(root, species);
        */

        String newick = parser.toString(root, false);
        System.out.println(newick);
    }
}
