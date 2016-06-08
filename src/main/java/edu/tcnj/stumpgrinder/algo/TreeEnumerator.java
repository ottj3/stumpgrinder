package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeEnumerator<S> {
    public TreeEnumerator() {
    }

    public TreeEnumerator(List<Node<S>> labelledNodes) {
        this.labelledNodes = labelledNodes;
    }
    /** Class Variables **/
    private Fitch fitch = new Fitch();
    /**
     * The current best parsimony score for any bounded tree enumeration.
     */
    private int parsimonyScore = -1;
    //The list of labelled nodes received from the input
    private List<Node<S>> labelledNodes = new ArrayList<>();
    /**
     * The set of tree topologies.
     */
    private Set<Node<S>> trees = new HashSet<>();

    //The root of the current tree
    private Node<S> root = new Node<>("");

    /** Class methods **/


    /**
     * Generates the base topology for a tree with n leaves.
     * <p>
     * This method initializes a tree topology to give us a starting point for
     * enumerating unrooted trees.
     */
    private void initializeTree() {
        if (labelledNodes.size() == 1) {
            root = labelledNodes.get(0);
        } else if (labelledNodes.size() == 2) {
            root = labelledNodes.get(0);
            Node.linkNodes(root, labelledNodes.get(1));
        } else {
            root = new Node<>("");

            for (int i = 0; i < 3; i++) {
                Node.linkNodes(root, labelledNodes.get(i));
            }
        }
    }

    /**
     */
    private void addNodeToEdge(Node<S> current, Node<S> parent, Node<S> internal, Node<S> leaf) {
        Node.unlinkNodes(parent, current);

        Node.linkNodes(parent, internal);
        Node.linkNodes(internal, current);
        Node.linkNodes(internal, leaf);
    }

    private void removeNodeFromEdge(Node<S> current, Node<S> parent, Node<S> internal, Node<S> leaf) {
        Node.unlinkNodes(internal, leaf);
        Node.unlinkNodes(internal, current);
        Node.unlinkNodes(parent, internal);

        Node.linkNodes(parent, current);
    }

    /**
     * Enumerates all possible tree topologies for a given set of species.
     */
    public Set<Node<S>> enumerate() {
        // Reset the state of the algorithm by clearing trees.
        trees = new HashSet<>();

        initializeTree();
        if (labelledNodes.size() < 4) {
            trees.add(root.clone());
        } else {
            enumerateRecursive(root, 3);
        }
        return trees;
    }

    /**
     *
     */
    private void enumerateRecursive(Node<S> current, int size) {
        if (size == labelledNodes.size()) {
            trees.add(root.clone());
        } else {
            for (int i = 0; i < current.children.size(); i++) {
                enumerateRecursive(current.children.get(0), size);
            }
            if (current != root) {
                Node<S> internal = new Node<>("");
                Node<S> leaf = labelledNodes.get(size);
                Node<S> parent = current.parent;

                addNodeToEdge(current, parent, internal, leaf);

                enumerateRecursive(root, size + 1);

                removeNodeFromEdge(current, parent, internal, leaf);
            }
        }
    }

    public Set<Node<S>> fitchEnumerate() {
        // Reset the state of the algorithm by clearing trees.
        trees = new HashSet<>();
        parsimonyScore = -1;

        initializeTree();
        if (labelledNodes.size() < 4) {
            trees.add(root.clone());
        } else {
            fitchEnumerateRecursive(root, 3);
        }
        return trees;
    }

    private void fitchEnumerateRecursive(Node<S> current, int size) {
        if (size == labelledNodes.size()) {
            root = Fitch.cubicToBinary(root);
            int score = fitch.bottomUp(root);
            if (score <= parsimonyScore) {
                if (score < parsimonyScore) {
                    parsimonyScore = score;
                    trees.clear();
                }
                trees.add(root.clone());
            } else if (parsimonyScore == -1) {
                parsimonyScore = score;
                trees.add(root.clone());
            }
            root = Fitch.binaryToCubic(root);
        } else {
            for (int i = 0; i < current.children.size(); i++) {
                fitchEnumerateRecursive(current.children.get(0), size);
            }

            root = Fitch.cubicToBinary(root);
            int thisScore = fitch.bottomUp(root);
            root = Fitch.binaryToCubic(root);

            if (current != root && (thisScore <= parsimonyScore || parsimonyScore == -1)) {
                Node<S> internal = new Node<>("");
                Node<S> leaf = labelledNodes.get(size);
                Node<S> parent = current.parent;

                addNodeToEdge(current, parent, internal, leaf);

                fitchEnumerateRecursive(root, size + 1);

                removeNodeFromEdge(current, parent, internal, leaf);
            }
        }
    }
    //
    //    public Set<String> hartiganEnumerate(Tree<Characters<S>> tree,
    //                                                    Characters<S> worldSet)
    //    {
    //        // Reset the state of the algorithm by clearing trees.
    //        trees = new HashSet<String>();
    //        parsimonyScore = -1;
    //
    //        initializeTree(tree);
    //        if (tree.size() < 4)
    //        {
    //            trees.add(tree.toString());
    //        }
    //        else
    //        {
    //            hartiganEnumerateRecursive(tree, tree.getRoot(), 3, worldSet);
    //        }
    //        return trees;
    //    }
    //
    //    private void hartiganEnumerateRecursive(Tree<Characters<S>> tree,
    //                                                       Node<Characters<S>> current,
    //                                                       int size,
    //                                                       Characters<S> worldSet)
    //    {
    //        if (size == tree.size())
    //        {
    //            int score = Hartigan.bottomUp(tree, worldSet);
    //            if (score <= parsimonyScore)
    //            {
    //                if (score < parsimonyScore)
    //                {
    //                    parsimonyScore = score;
    //                    trees.clear();
    //                }
    //                trees.add(tree.toString());
    //            }
    //            else if (parsimonyScore == -1)
    //            {
    //                parsimonyScore = score;
    //                trees.add(tree.toString());
    //            }
    //        }
    //        else
    //        {
    //            for (int i = 0; i < current.getChildren().size(); i++)
    //            {
    //                hartiganEnumerateRecursive(tree, current.getChild(0), size,
    //                        worldSet);
    //            }
    //            if (current != tree.getRoot()
    //                    && (Hartigan.bottomUp(tree, worldSet) <= parsimonyScore
    //                    || parsimonyScore == -1))
    //            {
    //                Node<Characters<S>> internal = new Node<Characters<S>>();
    //                Node<Characters<S>> leaf = tree.getNodes().get(size);
    //                Node<Characters<S>> parent = current.getParent();
    //
    //                addNodeToEdge(current, parent, internal, leaf);
    //
    //                hartiganEnumerateRecursive(tree, tree.getRoot(), size + 1,
    //                        worldSet);
    //
    //                removeNodeFromEdge(current, parent, internal, leaf);
    //            }
    //        }
    //        return;
    //    }

    // public  <S> Set<String> mixedEnumerate(Tree<Characters<S>> tree,
    //                                              Characters<S> worldSet)
    //   {
    //     // Reset the state of the algorithm by clearing trees.
    //     trees = new HashSet<String>();
    //     parsimonyScore = -1;

    //     initializeTree(tree);
    //     if (tree.size() < 4)
    //       {
    //         trees.add(tree.toString());
    //       }
    //     else
    //       {
    //         mixedEnumerateRecursive(tree, tree.getRoot(), 3, worldSet);
    //       }
    //     return trees;
    //   }

    // private  <S> Set<String> mixedEnumerateRecursive(Tree<Characters<S>> tree,
    //                                                        Node<Characters<S>> current,
    //                                                        int size,
    //                                                        Characters<S> worldSet)
    //   {
    //     if (size == tree.size())
    //       {
    //         trees.add(tree.toString());
    //       }
    //     else
    //       {
    //         for (int i = 0; i < current.getChildren().size(); i++)
    //           {
    //             mixedEnumerateRecursive(tree, current, size, worldSet);
    //           }
    //         if (current != tree.getRoot())
    //           {
    //             Node<Characters<S>> internal = new Node<Characters<S>>();
    //             Node<Characters<S>> leaf = tree.getNodes().get(size);
    //             Node<Characters<S>> parent = current.getParent();

    //             addNodeToEdge(current, parent, internal, leaf);
    //             mixedEnumerateRecursive(tree, current, size + 1, worldSet);
    //             removeNodeFromEdge(current, parent, internal, leaf);

    //             mixedEnumerateRecursive(tree, current, size + 1, worldSet);

    //             mixedEnumerateRecursive(tree, current, size + 1, worldSet);
    //             mixedEnumerateRecursive(tree, current, size + 1, worldSet);
    //           }
    //       }
    //     return;
    //   }
}
