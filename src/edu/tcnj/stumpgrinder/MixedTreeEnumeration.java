package edu.tcnj.stumpgrinder;

import java.util.HashSet;
import java.util.List;

/*******************************************************************************
 * This class enumerates all cubic, multifurcating, and mixed labelled trees.
 * 
 * @author Angela Huang <huanga9@tcnj.edu>
 * @date (Spring 2016)
 * @version 0.2
 ******************************************************************************/

public class
MixedTreeEnumeration
{
	/** HashSet to store trees as Newick strings */
	private static
    HashSet<String> trees = new HashSet<String>();

	/***************************************************************************
	 * Initializes the enumeration of all possible trees.
	 * 
	 * Establishes the base cases for n = {1,2} assigning a root if needed.
	 * 
	 * For n>2, this method will call the enumerateRecursive() method on the
	 * root to recursively enumerate trees.
	 * 
	 * @param tree
	 *            List of tree structures.
	 **************************************************************************/
	public static <T> HashSet<String>
    enumerate(Tree<List<SetList<T>>> tree)
    {
		if (tree.size() > 1) {
			tree.setRoot(tree.getNodes().get(0));
			tree.getRoot().makeChild(tree.getNodes().get(1));
			if (tree.size() > 2) {
				trees = enumerateRecursive(tree, tree.getRoot(), 2);
			} else {
				trees.add(tree.toString());
			}
		} else {
			tree.setRoot(tree.getNodes().get(0));
			trees.add(tree.toString());
		}

		return trees;
	}

	/***************************************************************************
	 * This method recursively applies all four cases of the recurrence to a
	 * given tree. Trees are enumerated one species at a time for each of the
	 * four cases, in a breadth first manner.
	 * 
	 * @param tree
	 *            The tree structure.
	 * @param current
	 *            The Node pointing to the root or pseudo-root of the tree.
	 * @param size
	 *            An integer containing the number of leaves that have been
	 *            added to the tree.
	 **************************************************************************/
	public static <T> HashSet<String>
    enumerateRecursive(Tree<List<SetList<T>>> tree,
                       Node<List<SetList<T>>> current,
			           int size)
    {
		if (size == tree.size()) {
			trees.add(tree.toString());
		} else {
			case1(tree, current, size);
			case2(tree, current, size);
			case3(tree, current, size);
			case4(tree, current, size);
		}

		return trees;
	}

	/***************************************************************************
	 * Case 1: Enumerates bifurcating trees.
	 * 
	 * @param tree
	 *            The tree structure.
	 * @param current
	 *            The Node pointing to the root or pseudo-root of the tree.
	 * @param size
	 *            An integer containing the number of leaves that have been
	 *            added to the tree.
	 **************************************************************************/
	public static <T> HashSet<String>
    case1(Tree<List<SetList<T>>> tree, Node<List<SetList<T>>> current, int size)
    {
		for (int index = 0; index < current.getChildren().size(); index++) {
			trees.addAll(case1(tree, current.getChildren().get(0), size));
		}
		if (current != tree.getRoot()) {
			Node<List<SetList<T>>> newLeaf = tree.getNodes().get(size);

			Node<List<SetList<T>>> internal = new Node<List<SetList<T>>>();

			Node<List<SetList<T>>> parent = current.getParent();

			parent.makeNotChild(current);
			parent.makeChild(internal);

			internal.makeChild(current);
			internal.makeChild(newLeaf);

			trees.addAll(enumerateRecursive(tree, tree.getRoot(), size + 1));

			newLeaf.makeNotParent(newLeaf.getParent());

			parent.makeNotChild(internal);
			parent.makeChild(current);
		}

		return trees;
	}

	/***************************************************************************
	 * Case 2: Enumerates multifurcating trees. Inserts a labelled node into any
	 * edge.
	 * 
	 * @param tree
	 *            The tree structure.
	 * @param current
	 *            The Node pointing to the root or pseudo-root of the tree.
	 * @param size
	 *            An integer containing the number of leaves that have been
	 *            added to the tree.
	 **************************************************************************/
	public static <T> HashSet<String>
    case2(Tree<List<SetList<T>>> tree, Node<List<SetList<T>>> current, int size)
    {
		for (int index = 0; index < current.getChildren().size(); index++) {
			trees.addAll(case2(tree, current.getChildren().get(0), size));
		}
		if (current != tree.getRoot()) {
			Node<List<SetList<T>>> newIntNode = tree.getNodes().get(size);

			Node<List<SetList<T>>> parent = current.getParent();

			parent.makeNotChild(current);
			parent.makeChild(newIntNode);

			newIntNode.makeChild(current);

			trees.addAll(enumerateRecursive(tree, tree.getRoot(), size + 1));

			current.makeNotParent(current.getParent());

			parent.makeNotChild(newIntNode);
			parent.makeChild(current);
		}

		return trees;
	}

	/***************************************************************************
	 * Case 3: Enumerates multifurcating trees. Inserts a child into any
	 * labelled or unlabelled node (including the root).
	 * 
	 * @param tree
	 *            The tree structure.
	 * @param current
	 *            The Node pointing to the root or pseudo-root of the tree.
	 * @param size
	 *            An integer containing the number of leaves that have been
	 *            added to the tree.
	 **************************************************************************/

	public static <T> HashSet<String>
    case3(Tree<List<SetList<T>>> tree, Node<List<SetList<T>>> current, int size)
    {
		for (int index = 0; index < current.getChildren().size(); index++) {
			trees.addAll(case3(tree, current.getChildren().get(index), size));
		}
		Node<List<SetList<T>>> newLeaf = tree.getNodes().get(size);

		current.makeChild(newLeaf);

		trees.addAll(enumerateRecursive(tree, tree.getRoot(), size + 1));

		current.makeNotChild(newLeaf);

		return trees;
	}

	/***************************************************************************
	 * Enumerates multifurcating trees. Labels any unlabelled node.
	 * 
	 * @param tree
	 *            The tree structure.
	 * @param current
	 *            The Node pointing to the root or pseudo-root of the tree.
	 * @param size
	 *            An integer containing the number of leaves that have been
	 *            added to the tree.
	 **************************************************************************/
	public static <T> HashSet<String>
    case4(Tree<List<SetList<T>>> tree, Node<List<SetList<T>>> current, int size)
    {
		for (int index = 0; index < current.getChildren().size(); index++) {
			trees.addAll(case4(tree, current.getChildren().get(index), size));
		}
		if (current.getData() == null) {
			Node<List<SetList<T>>> newNode = tree.getNodes().get(size);

			String prevLabel = current.getLabel();
			current.setLabel(newNode.getLabel());
			current.setData(newNode.getData());

			trees.addAll(enumerateRecursive(tree, tree.getRoot(), size + 1));

			current.setLabel(prevLabel);
			current.setData(null);
		}
		return trees;
	}
}
