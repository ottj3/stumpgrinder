package edu.tcnj.stumpgrinder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*******************************************************************************
 * This class runs Hartigan's bottom-up and top-down algorithms on a given tree.
 * It then recursively performs the edge-contraction algorithm.
 * 
 * @author Angela Huang <huanga9@tcnj.edu>
 * @date (Spring 2016)
 * @version 1.0
 ******************************************************************************/
public class
EdgeContraction
{
    public static <T> void
    edgeContraction(Tree<List<SetList<T>>> tree, SetList<T> worldSet)
    {
    	//Run bottom-up on tree
    	Hartigan.bottomUp(tree, worldSet);
    	
    	//Run top-down and get 0 min-cost edges
    	ArrayList<ArrayList<Node<List<SetList<T>>>>> edges = Hartigan.topDown(tree);

    	if (edges.size() == 0){          	
    		System.out.println("Cannot compact the tree any further.");
    	}
    	
    	else{
    	
    		ArrayList<Node<List<SetList<T>>>> edge = edges.get(0);

    		//make a copy of the tree
    		Tree<List<SetList<T>>> newTree = (Tree<List<SetList<T>>>)deepClone(tree);
    	
    		//get the nodes connected by the edge
    		Node<List<SetList<T>>> u = edge.get(0);
    		Node<List<SetList<T>>> v = edge.get(1);

    		ArrayList<Node<List<SetList<T>>>> nodestoRoot = getNodestoRoot(u);
    	

    	//If u has any parent nodes, reconstruct the tree as if the tree is rooted at u
    	if (nodestoRoot != null){
    		
    		Node<List<SetList<T>>> parent = nodestoRoot.get(0);
    	
    		//reverse the parent-child relationship of all ancestors of u
    		for (int i=nodestoRoot.size()-2; i>=0; i--){
    			Node<List<SetList<T>>> ancestor = nodestoRoot.get(i+1);
    			Node<List<SetList<T>>> current = nodestoRoot.get(i);
    			current.makeNotParent(ancestor);
    			current.makeChild(ancestor);
    		}
    		
    		//reverse the parent-child relationship of u and its immediate parent
    		u.makeNotParent(parent);
    		u.makeChild(parent);    		
    	}
    		
        	// Remove v as the child of u
        	u.makeNotChild(v);

        	// Make all children of v the children of u
        	for (Node<List<SetList<T>>> child : v.getChildren()){
        		child.makeParent(u);
        	}
        	
        	/**Keep contracting edges until no 0 min-cost edges remain.*/       	
        	edgeContraction(new Tree(u.getChildren(), u), worldSet);
    	}
    		
    	
    }
 
    /*******************************************************************************
     * Helper method to get all parent nodes up to the root.
     * Allows us to root a tree by that node.
     * 
     * @param node Node that we want to root the tree by
     * @return A list of parent nodes within the path to the root node
     ******************************************************************************/
    public static <T>  ArrayList<Node<List<SetList<T>>>> getNodestoRoot(Node<List<SetList<T>>> node)
    {
    	ArrayList<Node<List<SetList<T>>>> nodesToRoot = new ArrayList<Node<List<SetList<T>>>>();
    	return getNodestoRootRecursive(node.getParent(), nodesToRoot);
        
    }
    
    public static <T>  ArrayList<Node<List<SetList<T>>>> getNodestoRootRecursive(Node<List<SetList<T>>> node, ArrayList<Node<List<SetList<T>>>> list){
    	//If the node does not have a parent, this is the root of the sub-tree.
    	if(node == null) {
    		return null;
    	}
    	else
    	{
    		list.add(node);
    		getNodestoRootRecursive(node.getParent(), list);  
    	}
    	return list;
	}

    /*******************************************************************************
	 * Allows for a deep copy of any object.
	 * Used to allocate space for node and tree objects to allow 
	 * modification of references.
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
