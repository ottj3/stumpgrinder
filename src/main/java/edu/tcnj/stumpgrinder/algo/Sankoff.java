package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.DNABase;
import edu.tcnj.stumpgrinder.data.Node;

public class Sankoff {
    public static double bottomUp(Node current, double[][] weights) {
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

    private static void sankoff(Node current, double[][] weights) {
        for (Node child : current.children) {
            for (int i = 0; i < Node.chars; i++) {
                for (DNABase parentBase : DNABase.values()) {
                    double minScore = Double.MAX_VALUE;
                    for (DNABase childBase : DNABase.values()) {
                        double score = child.costs.get(i)[childBase.value] + weights[parentBase.value][childBase.value];
                        if (score < minScore) minScore = score;
                    }
                    if (!Double.valueOf(Double.POSITIVE_INFINITY).equals(current.costs.get(i)[parentBase.value])) {
                        current.costs.get(i)[parentBase.value] += minScore;
                    }
                }
            }
        }
    }
}
