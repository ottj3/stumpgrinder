package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;

public class Tree<T>
{
    /** A list of the nodes in the tree **/
    private ArrayList<Node<T>> nodes;

    /** A pointer to the root of the tree **/
    private Node<T> root;

    /**************************************************************************
     * Constructs a tree with no nodes and no root.
     **************************************************************************/
    public Tree()
    {
        this.root = null;
        this.nodes = new ArrayList<Node<T>>();
    }

    /**************************************************************************
     * Constructs a tree with nodes and no root.
     * @param nodes The nodes that are to be placed in the tree.
     **************************************************************************/
    public Tree(Collection<Node<T>> nodes)
    {
        this.nodes = new ArrayList<Node<T>>(nodes);
        this.root = null;
    }

    /**************************************************************************
     * Constructs a tree with nodes and a root.
     * @param nodes The nodes that are to be placed in the tree.
     * @param root  The node that will be the root of the tree.
     **************************************************************************/
    public Tree(Collection<Node<T>> nodes, Node<T> root)
    {
        this.nodes = new ArrayList<Node<T>>(nodes);

        if (! nodes.contains(root)) {
            nodes.add(root);
        }
        this.root = root;
    }

    /**************************************************************************
     * Returns a list of the nodes in this tree.
     * @return A list of the nodes in this tree.
     **************************************************************************/
    public ArrayList<Node<T>> getNodes()
    {
        return this.nodes;
    }

    /**************************************************************************
     * Returns the root of this tree.
     * @return The root of this tree.
     **************************************************************************/
    public Node<T> getRoot()
    {
        return this.root;
    }

    /**************************************************************************
     * Replaces the nodes in this tree.
     * @param nodes A list of the new nodes in this tree.
     **************************************************************************/
    public void setNodes(Collection<Node<T>> nodes)
    {
        this.nodes = new ArrayList<Node<T>>(nodes);
    }

    /**************************************************************************
     * Replaces the root of this tree.
     * @param root The new root of the tree.
     **************************************************************************/
    public void setRoot(Node<T> root)
    {
        this.root = root;
    }

    /**************************************************************************
     * Checks if a node is in this tree.
     * @param node The node to search this tree for.
     * @return true if the node is in this tree.
     **************************************************************************/
    public boolean inTree(Node<T> node)
    {
        return nodes.contains(node);
    }

    /**************************************************************************
     * Adds a node to this tree.
     * @param node The node to be added to this tree.
     * @return true if node is not already in this tree.
     **************************************************************************/
    public boolean addNode(Node<T> node)
    {
        if (inTree(node)) {
            return false;
        } else {
            nodes.add(node);
            return true;
        }
    }

    /**************************************************************************
     * Removes a node from this tree.
     * @param node The node to be removed from this tree.
     * @return true if node is not in this tree.
     **************************************************************************/
    public boolean removeNode(Node<T> node)
    {
        if (inTree(node)) {
            nodes.remove(node);
            return true;
        } else {
            return false;
        }
    }

    /**************************************************************************
     * Returns the number of nodes in the tree.
     * @return The number of nodes in the tree.
     **************************************************************************/
    public int size()
    {
        return nodes.size();
    }

    /**************************************************************************
     * Returns a string representation of the tree. (Newick Tree Format)
     * @return A string representation of the tree.
     **************************************************************************/
    public String toString()
    {
        String string = this.root.getLabel() + ";";
        string = toStringRecursive(root) + string;
        string = string.replace(",)", ")");
        return string;
    }

    /**************************************************************************
     * Returns a string representation of the subtree from a specified node.
     * @return A string representation of the subtree from a specified node.
     **************************************************************************/
    public String toStringRecursive(Node<T> current)
    {
        System.out.println("At node: " + current.getLabel());
        String string = "";

        if (current.getChildren().size() > 0) {
            string = ")" + string;
        }

        for (Node<T> child: current.getChildren()) {
            string = child.getLabel() + "," + string;
            string = toStringRecursive(child) + string;
        }

        if (current.getChildren().size() > 0) {
            string = "(" + string;
        }

        return string;
    }
}
