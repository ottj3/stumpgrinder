package edu.tcnj.stumpgrinder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Tree<T> implements Serializable{
	/** A list of the nodes in the tree **/
	private ArrayList<Node<T>> nodes;

	/** A pointer to the root of the tree **/
	private Node<T> root;

	/**************************************************************************
	 * Constructs a tree with no nodes and no root.
	 **************************************************************************/
	public Tree() {
		this.root = null;
		this.nodes = new ArrayList<Node<T>>();
	}


	/**************************************************************************
	 * Constructs a tree with nodes and no root.
	 * 
	 * @param nodes
	 *            The nodes that are to be placed in the tree.
	 **************************************************************************/
	public Tree(Collection<Node<T>> nodes) {
		this.nodes = new ArrayList<Node<T>>(nodes);
		this.root = null;
	}

	/**************************************************************************
	 * Constructs a tree with nodes and a root.
	 * 
	 * @param nodes
	 *            The nodes that are to be placed in the tree.
	 * @param root
	 *            The node that will be the root of the tree.
	 **************************************************************************/
	public Tree(Collection<Node<T>> nodes, Node<T> root) {
		this.nodes = new ArrayList<Node<T>>(nodes);
		this.root = root;
	}

	/**************************************************************************
	 * Returns a list of the nodes in this tree.
	 * 
	 * @return A list of the nodes in this tree.
	 **************************************************************************/
	public ArrayList<Node<T>> getNodes() {
		return this.nodes;
	}
	
	/**************************************************************************
	 * Returns a list of the nodes in this tree.
	 * 
	 * @return A list of the nodes in this tree.
	 **************************************************************************/
	//ArrayList<Node<T>> allNodes = new ArrayList<Node<T>>();
	public ArrayList<Node<T>> getAllNodes(ArrayList<Node<T>> nodes) {

		ArrayList<Node<T>> allNodes = nodes;
		//allNodes.addAll(nodes);
		
		for (int i =0; i<allNodes.size(); i++) {
			if (allNodes.get(i).getChildren().size()>0){
				
				allNodes.addAll(getAllNodes(allNodes.get(i).getChildren()));			
			}
		}
		
		return allNodes;
	}

	/**************************************************************************
	 * Returns the root of this tree.
	 * 
	 * @return The root of this tree.
	 **************************************************************************/
	public Node<T> getRoot() {
		return this.root;
	}

	/**************************************************************************
	 * Replaces the nodes in this tree.
	 * 
	 * @param nodes
	 *            A list of the new nodes in this tree.
	 **************************************************************************/
	public void setNodes(Collection<Node<T>> nodes) {
		this.nodes = new ArrayList<Node<T>>(nodes);
	}

	/**************************************************************************
	 * Replaces the root of this tree.
	 * 
	 * @param root
	 *            The new root of the tree.
	 **************************************************************************/
	public void setRoot(Node<T> root) {
		this.root = root;
	}

	/**************************************************************************
	 * Checks if a node is in this tree.
	 * 
	 * @param node
	 *            The node to search this tree for.
	 * @return true if the node is in this tree.
	 **************************************************************************/
	public boolean inTree(Node<T> node) {
		return nodes.contains(node);
	}

	/**************************************************************************
	 * Adds a node to this tree.
	 * 
	 * @param node
	 *            The node to be added to this tree.
	 * @return true if node is not already in this tree.
	 **************************************************************************/
	public boolean addNode(Node<T> node) {
		if (inTree(node)) {
			return false;
		} else {
			nodes.add(node);
			return true;
		}
	}

	/**************************************************************************
	 * Removes a node from this tree.
	 * 
	 * @param node
	 *            The node to be removed from this tree.
	 * @return true if node is not in this tree.
	 **************************************************************************/
	public boolean removeNode(Node<T> node) {
		if (inTree(node)) {
			nodes.remove(node);
			return true;
		} else {
			return false;
		}
	}

	/**************************************************************************
	 * Returns the number of nodes in the tree.
	 * 
	 * @return The number of nodes in the tree.
	 **************************************************************************/
	public int size() {
		return nodes.size();
	}

	/**************************************************************************
	 * Returns a string representation of the tree. (Newick Tree Format)
	 * 
	 * @return A string representation of the tree.
	 **************************************************************************/
	public String toString() {
		String string = this.root.getLabel() + ":" + root.getCost() + ";";
		string = toStringRecursive(root) + string;
		string = string.replace(",)", ")");
		return string;
	}

	/**************************************************************************
	 * Returns a string representation of the subtree from a specified node.
	 * 
	 * @return A string representation of the subtree from a specified node.
	 **************************************************************************/
	public String toStringRecursive(Node<T> current) {
		String string = "";

		if (current.getChildren().size() > 0) {
			string = ")" + string;
		}

		for (Node<T> child : current.getChildren()) {
			string = child.getLabel() + ":" + child.getCost() + "," + string;
			string = toStringRecursive(child) + string;
		}

		if (current.getChildren().size() > 0) {
			string = "(" + string;
		}

		return string;
	}
	
	/**************************************************************************
	 * Returns a string representation of the tree. (Newick Tree Format)
	 * Includes the data contained in each node.
	 * 
	 * @return A string representation of the tree.
	 **************************************************************************/
	public String toStringData() {
		String string = this.root.getLabel() + ":" + root.getData() + ";";
		string = toStringDataRecursive(root) + string;
		string = string.replace(",)", ")");
		return string;
	}

	/**************************************************************************
	 * Returns a string representation of the subtree from a specified node.
	 * Includes the data contained in each node.
	 * 
	 * @return A string representation of the subtree from a specified node.
	 **************************************************************************/
	public String toStringDataRecursive(Node<T> current) {
		String string = "";

		if (current.getChildren().size() > 0) {
			string = ")" + string;
		}

		for (Node<T> child : current.getChildren()) {
			string = child.getLabel() + ":" + child.getData() + "," + string;
			string = toStringDataRecursive(child) + string;
		}

		if (current.getChildren().size() > 0) {
			string = "(" + string;
		}

		return string;
	}
	
	/**************************************************************************
	 * Returns the node in the tree with the specified label.
	 * 
	 * @param label of node
	 * @return Node belonging to tree if node is found
	 **************************************************************************/
	 public Node<T> getNode(Node<T> edgeNode, Node<T> treeNode)
	 {
    		Node <T> current = new Node<T>();
    		//Node <T> treeNode = this.getRoot();
    		if (treeNode.getLabel().equals(edgeNode.getLabel()) && treeNode.getData().equals(edgeNode.getData())){
    			return treeNode;
    	  }
    		else if (treeNode.getChildren().size()>0){
    			for (Node<T> child : treeNode.getChildren()){
    				return getNode(edgeNode, child);
    			}
        	}
    	   }
        	return current;
    	}
    

	/**************************************************************************
	 * Returns the node in the tree with the specified label or 
	 * constructs an unlabelled node if the requested node does not exist. 
	 * 
	 * @param label of node
	 * @return Node belonging to tree if node is found; unlabelled node otherwise
	 **************************************************************************/
	public Node<T> createNode(String label) {
		Node<T> current = new Node<T>("");
		if (this.getRoot().getLabel().equals(label)) {
			//current = new Node<T> (this.getRoot().getLabel(), this.getRoot().getData());
			current = (Node<T>)deepClone(this.getRoot());
		} else {
			for (int i = 0; i < this.nodes.size(); i++) {
				if (label != "" && this.nodes.get(i).getLabel().equals(label)) {
					//current = new Node<T> (this.nodes.get(i).getLabel(), this.nodes.get(i).getData());
					current = (Node<T>)deepClone(this.nodes.get(i));;
				}
			}
		}
		return current;

	}

	/**************************************************************************
	 * Returns the root Node of the reconstructed tree.
	 * 
	 * @param A string representation of the tree.
	 **************************************************************************/
	public Node<T> fromString(String treeString) {
		/* Find the root node and make it the parent */
		int last = treeString.lastIndexOf(':');
		
		String label = treeString.substring(last - 1, last);
	
		return fromStringRecursive(treeString, new Node<T>(this.createNode(label).getLabel(), this.createNode(label).getData()), 0, last);
	}

	/**************************************************************************
	 * Recursively builds the tree from its string representation.
	 * 
	 * @param A string representation of the tree.
	 * @param Node 
	 * @param start index of the string
	 * @param end index of the string
	 * 
	 **************************************************************************/
	public Node<T> fromStringRecursive(String s, Node<T> parent, int start, int end) {
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
				parent.makeChild(fromStringRecursive(s, new Node<T>(this.createNode(label).getLabel(), this.createNode(label).getData()), marker + 1, colon));
				marker = i;
			}
		}
		return parent;
	}
	
	 /*******************************************************************************
	 * Allows for a deep copy of tree object.
	 * 
	 * @param object to be copied
	 * @return a clone of the object
	 ******************************************************************************/ 
	public static Object deepClone(Object object) {
		   try {
			 //Write to object output stream
		     ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();	     
		     ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);	     
		     objectOutput.writeObject(object);
		     
		     //Read back object through input stream.
		     ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());	     
		     ObjectInputStream objectInput = new ObjectInputStream(byteInput);
		     
		     return objectInput.readObject();
		   }
		   catch (Exception e) {
		     e.printStackTrace();
		     return null;
		   }
		 }
}
