package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.DNABase;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                    Set<DNABase> childContributions = new HashSet<>();
                    for (DNABase childBase : DNABase.values()) {
                        double score = child.costs.get(i)[childBase.value] + weights[parentBase.value][childBase.value];
                        if (score < minScore) {
                            minScore = score;
                            childContributions.clear();
                        }
                        if (score == minScore) {
                            childContributions.add(childBase);
                        }
                    }
                    if (!Double.valueOf(Double.POSITIVE_INFINITY).equals(current.costs.get(i)[parentBase.value])) {
                        current.costs.get(i)[parentBase.value] += minScore;
                        child.parentFits.get(i)[parentBase.value] = childContributions;
                    }
                }
            }
        }
    }

    /*FIND 0 min-cost edges:
    For each character, if a state contributed to the same state in a parent,
    and that parent's state contributed to an optimal assignment of the root,
    it is a potential zero-cost edge. If all characters have non-empty sets of potential zero-cost states,
    consider every permutation of sequences built from those sets as a zero min-cost edge.
     */

    /**
     * Top down function to assign sets of possible fits to return zero-cost edges.
     * currently works like hartigan's in terms of finding edges, so the Node.data sets
     * are not particularly useful outside of finding zero-cost edges.
     *
     * @param current the current node being worked on
     * @return a list of zero-cost edges
     */
    public static List<List<Node>> topDown(Node current) {
        List<List<Node>> edges = new ArrayList<>();
        if (current.parent == null) {
            for (int i = 0; i < Node.chars; i++) {
                double[] costArr = current.costs.get(i);
                double minCost = Double.POSITIVE_INFINITY;
                for (DNABase base : DNABase.values()) {
                    double cost = costArr[base.value];
                    if (cost < minCost) {
                        minCost = cost;
                        current.data.get(i).clear();
                    }
                    if (cost == minCost) {
                        current.data.get(i).add(base);
                    }
                }
            }
        } else {
            int cost = 0;
            for (int i = 0; i < Node.chars; i++) {
                Set<DNABase> parentSet = current.parent.data.get(i);
                for (DNABase dnaBase : parentSet) {
                    current.data.get(i).addAll(current.parentFits.get(i)[dnaBase.value]);
                }
                Set<DNABase> intersection = new HashSet<>(current.data.get(i));
                intersection.retainAll(current.parent.data.get(i));
                if (intersection.size() == 0) {
                    cost++;
                }
            }
            if (cost == 0 && !(current.parent.labelled && current.labelled)) {
                List<Node> newEdge = new ArrayList<>();
                newEdge.add(current.parent);
                newEdge.add(current);

                edges.add(newEdge);
            }
        }
        for (Node child : current.children) {
            edges.addAll(topDown(child));
        }
        return edges;
    }
}
