package edu.tcnj.stumpgrinder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*******************************************************************************
 * This class contracts all MP-cubic trees by enumerating all 
 * most parsimonious tree fits and contracting all 0-cost edges in the fitted tree.
 * 
 * **NOTE: This class is no longer needed, but I am leaving it here since 
 * it can be used to verify the output of the EdgeContraction class.
 * 
 * @author Angela Huang <huanga9@tcnj.edu>
 * @date (Spring 2016)
 * @version 2.0
 ******************************************************************************/
public class
MultipleContractions
{		 
    /**Start enumerating root assignments**/
    public static <T> void
    assign(Tree<List<SetList<T>>> tree)
    { 

    	HashSet<Node<List<SetList<T>>>> nodes = new HashSet<Node<List<SetList<T>>>>();
    	
        for (T state: tree.getRoot().getData().get(0).get(0)){
        	
        	/**create a new Root**/
        	Node<List<SetList<T>>> newRoot = (Node<List<SetList<T>>>)copy(tree.getRoot());
        	
        	newRoot.getData().clear();
        	
        	SetList<T> vf = new SetList<T>();
                vf.add(state);
                newRoot.getData().add(vf);
            
                Tree<List<SetList<T>>> tree2 = new Tree(newRoot.getChildren(), newRoot);
            
                nodes.addAll(assignRecursive(tree2, newRoot).snd());
            
                nodes.add(newRoot);
        }
        
        HashSet<Node<List<SetList<T>>>> rootNodes = new HashSet<Node<List<SetList<T>>>>();
        
        for (Node<List<SetList<T>>> nodeinTree: nodes) {
        	rootNodes.addAll(getRoot(nodeinTree));        	
        }
        
    	
    	HashMap<Tree<List<SetList<T>>>, Pair <Integer, ArrayList<ArrayList<Node<List<SetList<T>>>>>>> treesToEdges = new HashMap<Tree<List<SetList<T>>>, Pair <Integer, ArrayList<ArrayList<Node<List<SetList<T>>>>>>>();
    	
    	/**Find trees with maximum number of contractable edges*/
        for (Node<List<SetList<T>>> root: rootNodes) {
        	
        	Tree<List<SetList<T>>> fittedTree= new Tree<List<SetList<T>>>(root.getChildren(),root);

        	int numZeroEdges = countEdges(fittedTree).fst();
        	
        	ArrayList<ArrayList<Node<List<SetList<T>>>>> edges = countEdges(fittedTree).snd();
        	
        	Pair <Integer, ArrayList<ArrayList<Node<List<SetList<T>>>>>> zeroEdges = new Pair (numZeroEdges, edges);
        	
			treesToEdges.put(fittedTree, zeroEdges);
        }
        
        
        int max=0;
        for (Pair <Integer, ArrayList<ArrayList<Node<List<SetList<T>>>>>> value : treesToEdges.values()){
        	if (value.fst() > max) {
        		max = value.fst();
        	}
        }
        
        for (Tree<List<SetList<T>>> key : treesToEdges.keySet()){
        	if (treesToEdges.get(key).fst() != max){
        		treesToEdges.remove(key);
        	}
        }
        
        
        /**For each tree,contract all 0-cost edges*/
        for (Tree<List<SetList<T>>> key : treesToEdges.keySet()) {
        	 ArrayList<ArrayList<Node<List<SetList<T>>>>> zeroEdges = treesToEdges.get(key).snd();
        	
        	ArrayList<Node<List<SetList<T>>>> allNodes = key.getAllNodes(key.getNodes());
        	
        	for (int i = 0; i<zeroEdges.size(); i++){
        		ArrayList<Node<List<SetList<T>>>> edge = zeroEdges.get(i);
        		
        		
        		/**Modified*/
        		Node<List<SetList<T>>> parent = key.getNode(edge.get(0), key.getRoot());
        		Node<List<SetList<T>>> child = key.getNode(edge.get(1), key.getRoot());

        		child.makeNotParent(parent);
        		
        		}
        	
      		/**for all nodes in tree 
    			if node has a parent
    			find parent in list of child nodes in edges
    			make node child of parent's parent*/
        	
        	for (Node<List<SetList<T>>> current : allNodes) {
    			if (current.getParent()!=null){
    				for (int j=0; j<zeroEdges.size(); j++ ){				
    					if ((current.getParent().equals(zeroEdges.get(j).get(1)))){
    						zeroEdges.get(j).get(0).makeChild(current);
    					}
    				}
    			
    			}
        	}      	
        }
    
    }
    
    /**Enumerate all assignments**/
    public static <T> Pair<Node<List<SetList<T>>>, HashSet<Node<List<SetList<T>>>>>
    assignRecursive(Tree<List<SetList<T>>> tree, Node<List<SetList<T>>> current)
    {  	
    
    	HashSet<Node<List<SetList<T>>>> nodes = new HashSet<Node<List<SetList<T>>>>();
    	
      	if (current.getChildren().size() > 0) {
        	for (int index = 0; index < current.getChildren().size(); index++) {
        		
        		SetList<T> vh = current.getChildren().get(index).getData().get(0),
                        vl = current.getChildren().get(index).getData().get(1),
                        vv = current.getData().get(0);
                
                if (vh.get(0).containsAll(vv.get(0))) {
                	
                	current.getChildren().get(index).getData().clear();
                	current.getChildren().get(index).getData().add(vv);
                    
                	current.makeChild(assignRecursive(tree, current.getChildren().get(index)).fst());

                } 
                
                else {

                    SetList<T> newVH = new SetList<T>(vh),
                               newVL = new SetList<T>(vl),
                               vf = new SetList<T>();
                    	
                    
                    newVL.retainAll(vv);                    
                    newVH.addAll(newVL);
                    
                    if (newVH.get(0).size() == 1){
                    	vf.add(newVH.get(0).iterator().next());
                    	current.getChildren().get(index).getData().add(vf);
                    	current.makeChild(assignRecursive(tree, current.getChildren().get(index)).fst());
                    	
                    }
                    
                    else{
                    
                    /**for all but first (or last) state?*/
                    for (T state: newVH.get(0)){ 
                    	
                    	Node<List<SetList<T>>> newCurrent = (Node<List<SetList<T>>>)copy(current);

                    	newCurrent.getChildren().get(index).getData().clear();
                    	vf.add(state);
                    	newCurrent.getChildren().get(index).getData().add(vf);
 
                    	
                    	newCurrent.makeChild(assignRecursive(tree, newCurrent.getChildren().get(index)).fst());
                    	
                    	nodes.add(newCurrent);
                    }
                    }
                } 
             }	
    	}
      	
      	else {
      		SetList<T> vcf = new SetList<T>(current.getData().get(0));
      		current.getData().clear();
      		current.getData().add(vcf);
      	}
      
      	Pair<Node<List<SetList<T>>>, HashSet<Node<List<SetList<T>>>>> results = 
    			new Pair(current, nodes);
      	
      	return results;
    }
    
    public static <T> Pair<Integer, ArrayList<ArrayList<Node<List<SetList<T>>>>>> 
    countEdges(Tree<List<SetList<T>>> tree)
    {
    	Node<List<SetList<T>>> root = tree.getRoot();
    	
    	return countEdgesRecursive(root);
    }
    
    
    public static <T> Pair<Integer, ArrayList<ArrayList<Node<List<SetList<T>>>>>> 
    countEdgesRecursive(Node<List<SetList<T>>> current)
    {
    	ArrayList<ArrayList<Node<List<SetList<T>>>>> edges = new ArrayList();
    	int count = 0;
    	
    	if (current.getChildren().size() > 0) {
        	for (int index = 0; index < current.getChildren().size(); index++) {
        		T parentData = current.getData().get(0).get(0).iterator().next();
        		T childData = current.getChildren().get(index).getData().get(0).get(0).iterator().next();
        		
        		if (parentData.equals(childData)){
        			count+=1;
        			
        			ArrayList<Node<List<SetList<T>>>> newEdge = new ArrayList<Node<List<SetList<T>>>>();
        			newEdge.add(current);
        			newEdge.add(current.getChildren().get(index));
        			
        			edges.add(newEdge);
        			
        		}
        		
        		count += countEdgesRecursive(current.getChildren().get(index)).fst();
        		edges.addAll(countEdgesRecursive(current.getChildren().get(index)).snd());
        	}
    	}
    	Pair <Integer, ArrayList<ArrayList<Node<List<SetList<T>>>>>> results = new Pair(count,edges);
    	
		return results;
    }
    
    
    public static <T>  HashSet<Node<List<SetList<T>>>> getRoot(Node<List<SetList<T>>> node)
    {
    	HashSet<Node<List<SetList<T>>>> nodesToRoot = new HashSet<Node<List<SetList<T>>>>();
        if(node == null) {
        	return null;
        }
        else
        {
        	nodesToRoot.add(node);
            getRoot(node.getParent());  
        }
        return nodesToRoot;
    }
    
	/**Allows for a deep copy of any object -- Used to make copy of node objects/references**/
	 public static Object copy(Object object) {
	   try {
	     ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();	     
	     ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);	     
	     objectOutput.writeObject(object);
	     
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
