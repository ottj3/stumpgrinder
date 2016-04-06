package edu.tcnj.stumpgrinder;

import java.util.HashSet;

public class
MixedTreeEnumeration
{

    private static HashSet<String> trees = new HashSet<String>();

    public static <T> HashSet<String>
    enumerate(Tree<SetList<T>> tree)
    {     
        if (tree.size() > 1) {
        	tree.setRoot(tree.getNodes().get(0));
            tree.getRoot().makeChild(tree.getNodes().get(1));
            if (tree.size() > 2) {
            	trees = enumerateRecursive(tree, tree.getRoot(), 2);
            }
            else {
            	trees.add(tree.toString());
            }
        }
        else {
        	tree.setRoot(tree.getNodes().get(0));
        	trees.add(tree.toString());
        }

        return trees;
    }

    public static <T> HashSet<String>
    enumerateRecursive(Tree<SetList<T>> tree, 
                       Node<SetList<T>> current,
                       int size)
    {
        

        if (size == tree.size()) {
            trees.add(tree.toString());
        } else {
        	case1(tree, current, size);
        	case2(tree, current, size);
        }
        
        return trees;
    }
    
    public static <T> HashSet<String>
    case1(Tree<SetList<T>> tree, 
                       Node<SetList<T>> current,
                       int size)
    {
        for (int index = 0; index < current.getChildren().size(); index++) {
            trees.addAll(case1(tree,
                                            current.getChildren().get(0),
                                            size));
        }
        if (current != tree.getRoot()) {
            Node<SetList<T>> newLeaf = 
                tree.getNodes().get(size);

            Node<SetList<T>> internal = 
                new Node<SetList<T>>();

            Node<SetList<T>> parent =
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
        return trees;
    }
    
    public static <T> HashSet<String>
    case2(Tree<SetList<T>> tree, 
                       Node<SetList<T>> current,
                       int size)
    {
        for (int index = 0; index < current.getChildren().size(); index++) {
            trees.addAll(case2(tree,
                                            current.getChildren().get(0),
                                            size));
        }
        if (current != tree.getRoot()) {
            Node<SetList<T>> newIntNode = 
                tree.getNodes().get(size);

            Node<SetList<T>> parent =
                current.getParent();

            parent.makeNotChild(current);
            parent.makeChild(newIntNode);

            newIntNode.makeChild(current);

            trees.addAll(enumerateRecursive(tree, 
                                            tree.getRoot(),
                                            size + 1));

            current.makeNotParent(current.getParent());

            parent.makeNotChild(newIntNode);
            parent.makeChild(current);
        }
        return trees;
    }
}
