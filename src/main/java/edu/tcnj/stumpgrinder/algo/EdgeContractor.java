package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.List;

public class EdgeContractor<S> {
    private int bestSize = -1;
    private Node<S> bestTree = new Node<S>("");
    private CharacterList<S> worldSet = new CharacterList<>();

    public EdgeContractor(CharacterList<S> worldSet) {
        this.worldSet = worldSet;
    }

    public Node<S> edgeContraction(Node<S> root) {
        bestSize = -1;
        bestTree = new Node<S>("");
        Hartigan.bottomUp(root, worldSet);
        edgeContractionRecursive(root);
        return bestTree;
    }

    public void edgeContractionRecursive(Node<S> root) {
        //get list of zero-cost edges
        //Hartigan.bottomUp(root, worldSet);
        List<List<Node<S>>> edgeList = Hartigan.topDown(root);
        //if there are no 0 cost edges, edge contraction is done
        if (edgeList.size() == 0) {
            int treeSize = root.size();
            if (treeSize <= bestSize || bestSize == -1) {

                System.out.println("New tree of size " + treeSize + ": " + (new Parser().toString(root, false)));
//                if (treeSize == bestSize) {
//                }
                bestSize = treeSize;
                bestTree = root.clone();
            }
        } else {
            //else, for every edge in list, contract edge and then recurse
            for (List<Node<S>> edge : edgeList) {
                String beforeString = "Before: " + (new Parser()).toString(root, false);
                //contract edge, fix sets
//                boolean parentLabelled = edge.get(0).labelled;
                contractEdge(edge);
                //recurse
                edgeContractionRecursive(root);

                uncontractEdge(edge);
                System.out.println(beforeString);
                System.out.println("After:  " + (new Parser()).toString(root, false));

            }
        }
    }


    private void contractEdge(List<Node<S>> edge) {
        Node<S> parent = edge.get(0);
        Node<S> child = edge.get(1);
        for (Node<S> childsChild : child.children) {
            Node.linkNodes(parent, childsChild);
            //Node.unlinkNodes(child, child.children.get(0));
        }
        Node.unlinkNodes(parent, child);
        if (child.labelled) {
            parent.labelled = true;
            parent.label = child.label;
            parent.upper = child.upper;
            parent.root = child.root;
            parent.lower = child.lower;
        }
//        Node<S> root = parent;
//        while (root.parent != null) {
//            root = root.parent;
//        }
//        Hartigan.bottomUp(root, worldSet);
        while (parent != null) {
            if (!parent.labelled) {
                Hartigan.hartigan(parent, worldSet);
            }
            parent = parent.parent;
        }
    }

    private void uncontractEdge(List<Node<S>> edge) {
        Node<S> parent = edge.get(0);
        Node<S> child = edge.get(1);
        for (Node<S> childsChild : child.children) {
            Node.unlinkNodes(parent, childsChild);
            childsChild.parent = child;
        }
        Node.linkNodes(parent, child);
        if (child.labelled) {
            parent.labelled = false;
            parent.label = "";
        }
//        Node<S> root = parent;
//        while (root.parent != null) {
//            root = root.parent;
//        }
//        Hartigan.bottomUp(root, worldSet);
        while (parent != null) {
            if (!parent.labelled) {
                Hartigan.hartigan(parent, worldSet);
            }
            parent = parent.parent;
        }
    }
}
