package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.DNABase;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.HashMap;
import java.util.Map;

public class Sankoff {
    public static double bottomUp(Node current, Map<Integer, HashMap<Integer, Double>> weights) {
        for (Node child : current.children) {
            bottomUp(child, weights);
        }
        if (current.children.size() >= 1) {
            sankoff(current, weights);
        }
        double totalCost = 0;
        for (double[] cost : current.costs) {
            double minCost = Double.MAX_VALUE;
            for (int i = 0; i < cost.length; i++) {
                if (cost[i] < minCost) minCost = cost[i];
            }
            totalCost += minCost;
        }
        return totalCost;
    }

    private static void sankoff(Node current, Map<Integer, HashMap<Integer, Double>> weights) {
        for (Node child : current.children) {
            for (int i = 0; i < Node.chars; i++) {
                for (int j = 0; j < DNABase.NUM_BASES; j++) {
                    double minScore = Double.MAX_VALUE;
                    for (int k = 0; k < DNABase.NUM_BASES; k++) {
                        double score = child.costs.get(i)[k] + weights.get(j).get(k);
                        if (score < minScore) minScore = score;
                    }
                    if (!Double.valueOf(Double.POSITIVE_INFINITY).equals(current.costs.get(i)[j])) {
                        current.costs.get(i)[j] += minScore;
                    }
                }
            }
        }

    }
}
