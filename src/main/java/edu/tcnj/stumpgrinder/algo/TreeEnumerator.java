package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TreeEnumerator<S> {

    /**
     * Class Variables
     **/
    protected CharacterList<S> worldSet = new CharacterList<>();
    /**
     * The current best parsimony score for any bounded tree enumeration.
     */
    protected int parsimonyScore = -1;
    //The list of labelled nodes received from the input
    protected List<Node<S>> labelledNodes = new ArrayList<>();
    /**
     * The set of tree topologies.
     */
    protected Set<Node<S>> trees = new HashSet<>();
    protected int treeCounter = 0;
    //The root of the current tree
    protected Node<S> root = new Node<>("");

    /**
     */
    protected void addNodeToEdge(Node<S> current, Node<S> parent, Node<S> internal, Node<S> leaf) {
        Node.unlinkNodes(parent, current);

        Node.linkNodes(parent, internal);
        Node.linkNodes(internal, current);
        Node.linkNodes(internal, leaf);
    }

    protected void removeNodeFromEdge(Node<S> current, Node<S> parent, Node<S> internal, Node<S> leaf) {
        Node.unlinkNodes(internal, leaf);
        Node.unlinkNodes(internal, current);
        Node.unlinkNodes(parent, internal);

        Node.linkNodes(parent, current);
    }

    protected void updateMPlist(int thisParsimonyScore) {
        if (thisParsimonyScore <= parsimonyScore) {
            if (thisParsimonyScore < parsimonyScore) {
                parsimonyScore = thisParsimonyScore;
                trees.clear();
            }
            trees.add(root.clone());
        } else if (parsimonyScore == -1) {
            parsimonyScore = thisParsimonyScore;
            trees.add(root.clone());
        }
    }
}
