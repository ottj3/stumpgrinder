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
        String string = "";

        if (current.getChildren().size() > 0) {
            string = ")" + string;
        }

        for (Node<T> child: current.getChildren()) {
            if (child.getDistanceToParent() != null) {
                string = child.getLabel() + ":" + child.getDistanceToParent() +
                        "," + string;
            } else {
                string = child.getLabel() + "," + string;
            }
            string = toStringRecursive(child) + string;
        }

        if (current.getChildren().size() > 0) {
            string = "(" + string;
        }

        return string;
    }

	/**************************************************************************
	 * Returns the node in the tree with the specified label.
	 * @param label of node
	 * @return Node belonging to tree if node is found
	 **************************************************************************/
    public Node getNode(String label)
    {
        for (Node node : this.nodes) {
            if (node.getLabel() == label) {
                return node;
            }
        }

        if (this.getRoot().getLabel() == label) {
            //TODO: Should be some sort of special indicator
            return this.getRoot();
        }

        return null;
    }

	/**************************************************************************
	 * Returns the node in the tree with the specified label or 
	 * constructs an unlabelled node if the requested node does not exist. 
	 * @param label of node
	 * @return Node belonging to tree if node is found; unlabelled node otherwise
	 **************************************************************************/
	public Node createNode(String label) {
		Node current = new Node("");
		if (this.getRoot().getLabel().equals(label)) {
			//current = this.getRoot();
			current = new Node (this.getRoot().getLabel(), this.getRoot().getData());
		} else {
			for (int i = 0; i < this.nodes.size(); i++) {
				if (label != "" && this.nodes.get(i).getLabel().equals(label)) {
					current = new Node (this.nodes.get(i).getLabel(), this.nodes.get(i).getData());
					//current = this.nodes.get(i);
				}
			}
		}
		return current;

	}

	/**************************************************************************
	 * Returns the root Node of the reconstructed tree.
	 * @param A string representation of the tree.
	 **************************************************************************/
	public Node fromString(String treeString) {
		/* Find the root node and make it the parent */
		int last = treeString.lastIndexOf(':');
		
		String label = treeString.substring(last - 1, last);
	
		return fromStringRecursive(treeString, new Node(this.createNode(label).getLabel(), this.createNode(label).getData()), 0, last);
	}

	/**************************************************************************
	 * Recursively builds the tree from its string representation.
	 * @param A string representation of the tree.
	 * @param Node 
	 * @param start index of the string
	 * @param end index of the string
	 * 
	 **************************************************************************/
	public Node fromStringRecursive(String s, Node parent, int start, int end) {
		/*Check if the substring contains a single label.
		 * If so, return the node. Else, continue parsing the string*/
		if (s.charAt(start) != '(') {
			return parent;
		}

		int brackets = 0; // counts parenthesis
		int colon = 0; // marks the position of the colon
		int marker = start; // marks the position of string
		String label = ""; //stores the label of the node

		for (int i = start; i < end; i++) {
			char c = s.charAt(i);

			if (c == '(')
				brackets++;
			else if (c == ')')
				brackets--;
			else if (c == ':')
				colon = i;

			if (brackets == 0 && c == ')' || brackets == 1 && c == ',') {

				if (!(s.charAt(colon - 1) == ')')) {
					label = s.substring(colon - 1, colon);

				}

				else {
					label = "";

				}
				parent.makeChild(fromStringRecursive(s, new Node(this.createNode(label).getLabel(), this.createNode(label).getData()), marker + 1, colon));
				marker = i;
			}
		}
		return parent;
	}
}
