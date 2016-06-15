package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.DNABase;
import edu.tcnj.stumpgrinder.data.Node;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SankoffTest {
    @Test
    public void testSankoff() {
        Node.chars = 1;
        Node c1 = new Node("C1", "C");
        Node a1 = new Node("A1", "A");
        Node c2 = new Node("C2", "C");
        Node a2 = new Node("A2", "A");
        Node g1 = new Node("G1", "G");
        Node in1 = new Node("");
        Node.linkNodes(in1, c1);
        Node.linkNodes(in1, a1);
        Node in2 = new Node("");
        Node.linkNodes(in2, a2);
        Node.linkNodes(in2, g1);
        Node in3 = new Node("");
        Node.linkNodes(in3, c2);
        Node.linkNodes(in3, in2);
        Node root = new Node("");
        Node.linkNodes(root, in1);
        Node.linkNodes(root, in3);

        Map<Integer, HashMap<Integer, Double>> weights = new HashMap<>();
        HashMap<Integer, Double> aWeights = new HashMap<>();
        aWeights.put(DNABase.A.value, 0.0);
        aWeights.put(DNABase.C.value, 2.5);
        aWeights.put(DNABase.G.value, 1.0);
        aWeights.put(DNABase.T.value, 2.5);
        weights.put(DNABase.A.value, aWeights);

        HashMap<Integer, Double> cWeights = new HashMap<>();
        cWeights.put(DNABase.A.value, 2.5);
        cWeights.put(DNABase.C.value, 0.0);
        cWeights.put(DNABase.G.value, 2.5);
        cWeights.put(DNABase.T.value, 1.0);
        weights.put(DNABase.C.value, cWeights);

        HashMap<Integer, Double> gWeights = new HashMap<>();
        gWeights.put(DNABase.A.value, 1.0);
        gWeights.put(DNABase.C.value, 2.5);
        gWeights.put(DNABase.G.value, 0.0);
        gWeights.put(DNABase.T.value, 2.5);
        weights.put(DNABase.G.value, gWeights);

        HashMap<Integer, Double> tWeights = new HashMap<>();
        tWeights.put(DNABase.A.value, 2.5);
        tWeights.put(DNABase.C.value, 1.0);
        tWeights.put(DNABase.G.value, 2.5);
        tWeights.put(DNABase.T.value, 0.0);
        weights.put(DNABase.T.value, tWeights);

        System.out.println(Sankoff.bottomUp(root, weights));
    }
}
