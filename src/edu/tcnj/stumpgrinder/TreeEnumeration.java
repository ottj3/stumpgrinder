package edu.tcnj.stumpgrinder;

import java.util.List;
import java.util.HashSet;

public class
TreeEnumeration
{
    /** The current best parsimony score for any bounded tree enumeration **/
    private static int bestParsimonyScore = -1;

    public static <T> HashSet<String>
    enumerate(Tree<List<SetList<T>>> tree)
    {
        HashSet<String> trees = new HashSet<String>();
        bestParsimonyScore = -1;
        
        if (tree.size() == 1) {
            tree.setRoot(tree.getNodes().get(0));
            trees.add(tree.toString());
        } else if (tree.size() == 2) {
            tree.setRoot(tree.getNodes().get(0));
            tree.getRoot().makeChild(tree.getNodes().get(1));
            trees.add(tree.toString());
        } else if (tree.size() >= 3) {
            // Reconsider
            Node<List<SetList<T>>> internal = 
                new Node<List<SetList<T>>>();

            tree.setRoot(internal);

            for (int index = 0; index < 3; index++) {
                internal.makeChild(tree.getNodes().get(index));
            }

            if (tree.size() > 3) {
                trees = enumerateRecursive(tree, tree.getRoot(), 3);
            } else {
                trees.add(tree.toString());
            }
        }

        return trees;
    }

    public static <T> HashSet<String>
    enumerateRecursive(Tree<List<SetList<T>>> tree, 
                       Node<List<SetList<T>>> current,
                       int size)
    {
        HashSet<String> trees = new HashSet<String>();

        if (size == tree.size()) {
            trees.add(tree.toString());
        } else {
            for (int index = 0; index < current.getChildren().size(); index++) {
                trees.addAll(enumerateRecursive(tree,
                                                current.getChildren().get(0),
                                                size));
            }
            if (current != tree.getRoot()) {
                Node<List<SetList<T>>> newLeaf = 
                    tree.getNodes().get(size);

                Node<List<SetList<T>>> internal = 
                    new Node<List<SetList<T>>>();

                Node<List<SetList<T>>> parent =
                    current.getParent();

                parent.makeNotChild(current);
                parent.makeChild(internal);

                internal.makeChild(current);
                internal.makeChild(newLeaf);

                trees.addAll(enumerateRecursive(tree, 
                                                tree.getRoot(),
                                                size + 1));

                newLeaf.makeNotParent(newLeaf.getParent());

                parent.makeNotChild(internal);
                parent.makeChild(current);
            }
        }
        
        return trees;
    }

    public static <T> HashSet<String>
    fitchScoredEnumerate(Tree<List<SetList<T>>> tree)
    {
        HashSet<String> trees = new HashSet<String>();
        bestParsimonyScore = -1;
        
        if (tree.size() == 1) {
            tree.setRoot(tree.getNodes().get(0));
            trees.add(tree.toString());
        } else if (tree.size() == 2) {
            tree.setRoot(tree.getNodes().get(0));
            tree.getRoot().makeChild(tree.getNodes().get(1));
            trees.add(tree.toString());
        } else if (tree.size() >= 3) {
            // Reconsider
            Node<List<SetList<T>>> internal = 
                new Node<List<SetList<T>>>();
            tree.setRoot(internal);

            for (int index = 0; index < 3; index++) {
                internal.makeChild(tree.getNodes().get(index));
            }

            if (tree.size() > 3) {
                trees = fitchScoredEnumerateRecursive(tree, tree.getRoot(), 3);
            } else {
                trees.add(tree.toString());
            }
        }

        return trees;
    }

    public static <T> HashSet<String>
    fitchScoredEnumerateRecursive(Tree<List<SetList<T>>> tree,
                                  Node<List<SetList<T>>> current,
                                  int size)
    {
        HashSet<String> trees = new HashSet<String>();
        int parsimonyScore;

        if (size == tree.size()) {
            parsimonyScore = Fitch.bottomUp(tree);
            if (parsimonyScore < bestParsimonyScore) {
                trees.clear();
                trees.add(tree.toString());
                bestParsimonyScore = parsimonyScore;
            } else if (parsimonyScore == bestParsimonyScore) {
                trees.add(tree.toString());
            } else if (bestParsimonyScore == -1) {
                bestParsimonyScore = parsimonyScore;
                trees.add(tree.toString());
            }
        } else {
            for (int index = 0; index < current.getChildren().size(); index++) {
                trees.addAll(fitchScoredEnumerateRecursive(tree,
                                                current.getChildren().get(0),
                                                size));
            }
            if (current != tree.getRoot()) {
                if (Fitch.bottomUp(tree) <= bestParsimonyScore || 
                    bestParsimonyScore == -1) {

                    Node<List<SetList<T>>> newLeaf = 
                        tree.getNodes().get(size);

                    Node<List<SetList<T>>> internal = 
                        new Node<List<SetList<T>>>();

                    Node<List<SetList<T>>> parent =
                        current.getParent();

                    parent.makeNotChild(current);
                    parent.makeChild(internal);

                    internal.makeChild(current);
                    internal.makeChild(newLeaf);

                    trees.addAll(fitchScoredEnumerateRecursive(tree, 
                                                               tree.getRoot(),
                                                               size + 1));

                    newLeaf.makeNotParent(newLeaf.getParent());

                    parent.makeNotChild(internal);
                    parent.makeChild(current);
                }
            }
        }
        
        return trees;
    }

    public static <T> HashSet<String>
    hartiganScoredEnumerate(Tree<List<SetList<T>>> tree,
                            SetList<T> worldSet)
    {
        HashSet<String> trees = new HashSet<String>();
        bestParsimonyScore = -1;
        
        if (tree.size() == 1) {
            tree.setRoot(tree.getNodes().get(0));
            trees.add(tree.toString());
        } else if (tree.size() == 2) {
            tree.setRoot(tree.getNodes().get(0));
            tree.getRoot().makeChild(tree.getNodes().get(1));
            trees.add(tree.toString());
        } else if (tree.size() >= 3) {
            // Reconsider
            Node<List<SetList<T>>> internal = new Node<List<SetList<T>>>();
            tree.setRoot(internal);

            for (int index = 0; index < 3; index++) {
                internal.makeChild(tree.getNodes().get(index));
            }

            if (tree.size() > 3) {
                trees = hartiganScoredEnumerateRecursive(tree, worldSet, 
                                                         tree.getRoot(), 3);
            } else {
                trees.add(tree.toString());
            }
        }

        return trees;
    }

    public static <T> HashSet<String>
    hartiganScoredEnumerateRecursive(Tree<List<SetList<T>>> tree,
                                     SetList<T> worldSet,
                                     Node<List<SetList<T>>> current,
                                     int size)
    {
        HashSet<String> trees = new HashSet<String>();
        int parsimonyScore;

        if (size == tree.size()) {
            parsimonyScore = Hartigan.bottomUp(tree, worldSet);
            if (parsimonyScore < bestParsimonyScore) {
                trees.clear();
                trees.add(tree.toString());
                bestParsimonyScore = parsimonyScore;
            } else if (parsimonyScore == bestParsimonyScore) {
                trees.add(tree.toString());
            } else if (bestParsimonyScore == -1) {
                bestParsimonyScore = parsimonyScore;
                trees.add(tree.toString());
            }
        } else {
            for (int index = 0; index < current.getChildren().size(); index++) {
                trees.addAll(hartiganScoredEnumerateRecursive(tree,
                                                            worldSet,
                                                            current.getChild(0),
                                                            size));
            }
            if (current != tree.getRoot()) {
                if (Hartigan.bottomUp(tree, worldSet) <= bestParsimonyScore ||
                    bestParsimonyScore == -1) {

                    Node<List<SetList<T>>> newLeaf = 
                        tree.getNodes().get(size);

                    Node<List<SetList<T>>> internal = 
                        new Node<List<SetList<T>>>();

                    Node<List<SetList<T>>> parent =
                        current.getParent();

                    parent.makeNotChild(current);
                    parent.makeChild(internal);

                    internal.makeChild(current);
                    internal.makeChild(newLeaf);

                    trees.addAll(hartiganScoredEnumerateRecursive(tree,
                                                               worldSet,
                                                               tree.getRoot(),
                                                               size + 1));

                    newLeaf.makeNotParent(newLeaf.getParent());

                    parent.makeNotChild(internal);
                    parent.makeChild(current);
                }
            }
        }

        return trees;
    }
}
