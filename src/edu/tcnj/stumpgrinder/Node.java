package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;

/**************************************************************************
 * A node in a tree.
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 1.0
 **************************************************************************/
public class
Node<T> extends Vertex<T>
{
    /** The node that is this node's parent **/
    private 
    Node<T> parent;

    /** The distance between this node and it's parent **/
    private
    Integer distanceToParent;

    /** A list containing nodes that are children of this node */
    private 
    ArrayList<Node<T>> children;


    /**************************************************************************
     * Constructs a node with no label, no data, no children and no parent.
     **************************************************************************/
    public
    Node()
    {
        super();
        parent = null;
        distanceToParent = null;
        children = new ArrayList<Node<T>>();
    }

    /**************************************************************************
     * Constructs a node with a label, but no data, no children and no parent.
     * @param label The unique label to be used to identify this vertex.
     **************************************************************************/
    public
    Node(String label)
    {
        super(label);
        parent = null;
        distanceToParent = null;
        children = new ArrayList<Node<T>>();
    }

    /**************************************************************************
     * Constructs a node with a label and data, but no children and no parent.
     * @param label The unique label to be used to identify this vertex.
     * @param data  The data to be associated with this vertex.
     **************************************************************************/
    public
    Node(String label, T data)
    {
        super(label, data);
        parent = null;
        distanceToParent = null;
        children = new ArrayList<Node<T>>();
    }

    /**************************************************************************
     * Constructs a node with a label, data and a parent, but no children.
     * @param label  The unique label to be used to identify this vertex.
     * @param data   The data to be associated with this vertex.
     * @param parent The node to be assigned as this node's parent.
     **************************************************************************/
    public
    Node(String label, T data, Node<T> parent)
    {
        super(label, data);
        makeParent(parent);
        distanceToParent = null;
        children = new ArrayList<Node<T>>();
    }

    /**************************************************************************
     * Constructs a node with a label, data and children, but no parent.
     * @param label    The unique label to be used to identify this vertex.
     * @param data     The data to be associated with this vertex.
     * @param children The list of nodes to be assigned as this node's children.
     **************************************************************************/
    public
    Node(String label, T data, Collection<? extends Node<T>> children)
    {
        super(label, data);
        parent = null;
        distanceToParent = null;
        children = new ArrayList<Node<T>>();
        for (Node<T> child : children) {
            makeChild(child);
        }
    }

    /**************************************************************************
     * Constructs a node with a label, data, a parent, and children.
     * @param label    The unique label to be used to identify this vertex.
     * @param data     The data to be associated with this vertex.
     * @param parent The node to be assigned as this node's parent.
     * @param children The list of nodes to be assigned as this node's children.
     **************************************************************************/
    public
    Node(String label, T data, Node<T> parent, Collection<Node<T>> children)
    {
        super(label, data);
        makeParent(parent);
        distanceToParent = null;
        children = new ArrayList<Node<T>>();
        for (Node<T> child : children) {
            makeChild(child);
        }
    }

    /**************************************************************************
     * Returns the parent of this node.
     * @return The parent of this node.
     **************************************************************************/
    public Node<T>
    getParent()
    {
        return parent;
    }

    /**************************************************************************
     * Returns the distance from this node to it's parent.
     * @return The distance from this node to it's parent.
     **************************************************************************/
    public Integer
    getDistanceToParent()
    {
        return distanceToParent;
    }

    /**************************************************************************
     * Returns the list of the children of this node.
     * @return The list of the children of this node.
     **************************************************************************/
    public ArrayList<Node<T>>
    getChildren()
    {
        return children;
    }

    
    /**************************************************************************
     * Returns the child at the specified position in the list of children. 
     * @param index Index of the child to return.
     * @return The child at the specified position in the list of children.
     **************************************************************************/
    public Node<T>
    getChild(int index)
    {
        return children.get(index);
    }

    /**************************************************************************
     * Replaces the parent of this node.
     * @param parent The new parent for this node.
     **************************************************************************/
    public void
    setParent(Node<T> parent)
    {
        this.parent = parent; 
    }

    /**************************************************************************
     * Changes the distance to this node's parent.
     * @param distanceToParent The new distance to this nodes parent.
     **************************************************************************/
    public void
    setDistanceToParent(Integer distanceToParent)
    {
        this.distanceToParent = distanceToParent;
    }

    /**************************************************************************
     * Replaces the child at the specified position in the list of children.
     * @param index Index of the child to return.
     * @param child The child to be stored at the specified position.
     **************************************************************************/
    public void
    setChild(int index, Node<T> child)
    {
        this.children.set(index, child);
    }

    /**************************************************************************
     * Replaces the list of children for this node.
     * @param children The new list of children for this node.
     **************************************************************************/
    public void
    setChildren(ArrayList<Node<T>> children)
    {
        this.children = children; 
    }


    /**************************************************************************
     * Checks if a node is this node's child.
     * @param node The node to search the children list for.
     * @return true if node is a child of this node and false otherwise.
     **************************************************************************/
    public boolean
    isChild(Node<T> node)
    {
        return children.contains(node) && node.getParent() == this;
    }

    /**************************************************************************
     * Checks if a node is this node's parent.
     * @param node The node to check against this node's parent.
     * @return true if node this node's parent and false otherwise.
     **************************************************************************/
    public boolean
    isParent(Node<T> node)
    {
        return node.getChildren().contains(this) && parent == node;
    }

    /**************************************************************************
     * Makes a node this node's child.
     * @param node The node to make this node's child.
     * @return true if the node is not already this node's child.
     **************************************************************************/
    public boolean
    makeChild(Node<T> node)
    {
        if (isChild(node) || isParent(node)) {
            return false;
        } else {
            if (node.getParent() != null) {
                node.getParent().getChildren().remove(node);
            }
            children.add(node);
            node.setParent(this);
            makeAdjacent(node);
            return true;
        }
    }

    /**************************************************************************
     * Makes a node not this node's child.
     * @param node The node to make not this node's child.
     * @return true if the node is not already this node's child.
     **************************************************************************/
    public boolean
    makeNotChild(Node<T> node)
    {
        if (isChild(node)) {
            node.setParent(null);
            children.remove(node);
            makeNotAdjacent(node);
            return true;
        } else {
            return false;
        }
    }

    /**************************************************************************
     * Makes a node this node's parent.
     * @param node The node to make this node's parent.
     * @return true if the node is not already this node's child or parent.
     **************************************************************************/
    public boolean
    makeParent(Node<T> node)
    {
        if (isChild(node) || isParent(node)) {
            return false;
        } else {
            node.getChildren().add(this);
            makeAdjacent(node);
            parent = node;
            return true;
        }
    }

    /**************************************************************************
     * Makes a node not this node's parent.
     * @param node The node to make not this node's parent.
     * @return true if the node is already this node's child or parent.
     **************************************************************************/
    public boolean
    makeNotParent(Node<T> node)
    {
        if (isParent(node)) {
            node.getChildren().remove(this);
            makeNotAdjacent(node);
            parent = null;
            return true;
        } else {
            return false;
        }
    }

    /**************************************************************************
     * Returns a node's child at a certain index.
     * @param index of ArrayList where child is contained.
     **************************************************************************/
   public Node getChild(int index) {
        return (Node) children.get(index);
   }
   
   /**************************************************************************
    * Returns the number of children a node has.
    **************************************************************************/
   public int numChildren() {
       return children.size();
  }

  /**************************************************************************
   * Checks whether a child has a parent.
   **************************************************************************/
  public boolean hasParent() {
	   return (parent != null);
  }
 
  /**************************************************************************
   * Returns the cost of a child node to its parent.
   **************************************************************************/
   public int getCost() {
	   int cost = 0;
	   if (this.hasParent()){
		   if (this.getLabel()==this.getParent().getLabel()){
			   cost = 0;
		   }
		   else {
			   cost = 1;
		   }
	   }
	   else{
		   cost = 0;
	   }
	   return cost;
   }
  
}
