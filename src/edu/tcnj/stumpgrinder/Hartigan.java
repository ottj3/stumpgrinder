package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/*******************************************************************************
 * This class contains methods to perform Hartigan's bottom-up and town-down
 * algorithms for species with m characters. 
 * The bottom-up implementation also accounts for internal labelled nodes 
 * (fast-hartigan's). 
 * 
 * @author Angela Huang <huanga9@tcnj.edu> and Andrew Miller <millea18@tcnj.edu> 
 * @date (Spring 2016)
 * @version 3.0
 ******************************************************************************/
public class
Hartigan
{
	/*******************************************************************************
	 * Performs the calculation of upper and lower sets as well as MP-score.
	 * Used in bottom-up of Hartigan's algorithm.
	 * @param sets These are the upper and lower sets of the children nodes.
	 * @param worldSet This set contains all possible character states. 
	 * 		  (used when K=1)	
	 ******************************************************************************/
    public static <T> Pair<Integer, List<SetList<T>>>
    //Initialize upper and lower sets of leaves
    hartigan(List<SetList<T>> sets, SetList<T> worldSet, Node<List<SetList<T>>> current)
    {
        int score = 0; //initialize maximum parsimony score of node
        int length = sets.get(0).size(); //number of characters in alignment
        
        SetList<T> vh = new SetList<T>(length),
                   vl = new SetList<T>(length); //create upper and lower sets for each char of node
        
        ArrayList<HashMap<T, Integer>> count = new ArrayList<HashMap<T, Integer>>(length);
        //array to store count of each char 
        
        //Initialize empty hashmap for each character in alignment
        for (int index = 0; index < length; index++) {
            count.add(new HashMap<T, Integer>());
        }

        //for each child
        for (SetList<T> set : sets) {
        	//for each character in species alignment
            for (int index = 0; index < length; index++) {
            	//for each character state 
                for (T state : set.get(index)) {
                	//if alignment position contains the state
                    if (count.get(index).containsKey(state)) {
                    	//increase count by 1
                        count.get(index).put(state, 
                                             count.get(index).get(state) + 1);
                    } else {
                        count.get(index).put(state,
                                             1);
                    }
                }
            }
        }

        HashSet<T> k, kMinusOne;
        int occurences, kOccurences, kMinusOneOccurences;

        //for each character in alignment
        for (int index = 0; index < length; index++) {
            k = new HashSet<T>(); kMinusOne = new HashSet<T>();
            kOccurences = 1; kMinusOneOccurences = 0;

            for (T state : count.get(index).keySet()) {
                occurences = count.get(index).get(state);
                if (occurences > kOccurences) {
                    if (kOccurences == occurences - 1) {
                        kMinusOne = k;
                        kMinusOneOccurences = kOccurences;
                        k = new HashSet<T>();
                        k.add(state);
                    } else {
                        k.clear();
                        k.add(state);
                    }

                    if (kMinusOne.contains(state)) {
                        kMinusOne.remove(state);
                    }

                    kOccurences = occurences;
                } else if (occurences == kOccurences) {
                    k.add(state);
                    if (kMinusOne.contains(state)) {
                        kMinusOne.remove(state);
                    }
                } else if (occurences == kMinusOneOccurences) {
                    kMinusOne.add(state);
                }
            }
          
            //This accounts for possibilities not present in sets.
            if (kOccurences==1 && current.getChildren().size()!=0) {
                for (T state : worldSet.get(index)){
                	if (!k.contains(state)){
                		kMinusOne.add(state);
                	}
                }
            }
             
            
            score += sets.size() - kOccurences;
            
            vh.set(index, k);
            vl.set(index,kMinusOne);
        }

        List<SetList<T>> setResult = new ArrayList<SetList<T>>(2);
        setResult.add(vh);
        setResult.add(vl);

        Pair<Integer, List<SetList<T>>> results =
            new Pair<Integer, List<SetList<T>>>(score, setResult);
        return results;
    }
	/*******************************************************************************
	 * Performs Fast Hartigan's algorithm:
	 * Performs the calculation of upper and lower sets as well as MP-score of a 
	 * labelled nodes.
	 * Used in bottom-up of Hartigan's algorithm.
	 * @param sets These are the upper and lower sets of the children nodes.
	 * @param worldSet This set contains all possible character states. 
	 * @param data This is the data of the labelled node
	 * 		  (Used when K=1)	
	 ******************************************************************************/   
    public static <T> Pair<Integer, List<SetList<T>>>
    fastHartigan(List<SetList<T>> sets, SetList<T> worldSet, SetList<T> data)
    {
        int score = 0; //initialize maximum parsimony score of node
        int length = sets.get(0).size(); //number of characters in alignment
        
        SetList<T> vh = new SetList<T>(length),
                   vl = new SetList<T>(length); //create upper and lower sets for each char of node
        
        ArrayList<HashMap<T, Integer>> count = new ArrayList<HashMap<T, Integer>>(length);
        //array to store count of each char 
        
        //Initialize empty hashmap for each character in alignment
        for (int index = 0; index < length; index++) {
            count.add(new HashMap<T, Integer>());
            //Initialize count of zero for all character states
            for (T state : worldSet.get(index)){
            	count.get(index).put(state, 0);
            }
        }
        
        //for each child
        for (SetList<T> set : sets) {
        	//for each character in species alignment
            for (int index = 0; index < length; index++) {
            	//for each character state 
                for (T state : set.get(index)) {
                	//if alignment position contains the state
                    if (count.get(index).containsKey(state)) {
                    	//increase count by 1
                        count.get(index).put(state, 
                                             count.get(index).get(state) + 1);
                    } else {
                        count.get(index).put(state,
                                             1);
                    }
                }
            }
        }

        HashSet<T> k, kMinusOne;

        //for each character in alignment
        for (int index = 0; index < length; index++) {
            k = new HashSet<T>(); kMinusOne = new HashSet<T>();
            
            //get the character state of labelled node
            T label = data.get(0).iterator().next();
            
            k.add(label);
           
            //int kv = count.get(index).get(label);
            
            //score += sets.size() - kv;
            
            vh.set(index, k);
            vl.set(index,kMinusOne);

        }

        List<SetList<T>> setResult = new ArrayList<SetList<T>>(2);
        setResult.add(vh);
        setResult.add(vl);

        Pair<Integer, List<SetList<T>>> results =
            new Pair<Integer, List<SetList<T>>>(score, setResult);
        return results;
    }
    
	/*******************************************************************************
	 * Performs bottom up of Hartigan's algorithm (see Theorem2 of Hartigan's paper)
	 * @param tree Tree to be computed
	 * @param worldSet This set contains all possible character states. 
	 * @param data This is the data of the labelled node
	 * 		  (Used when K=1)	
	 ******************************************************************************/   
    public static <T> int
    bottomUp(Tree<List<SetList<T>>> tree, SetList<T> worldSet)
    {
        return bottomUpRecursive(tree.getRoot(), worldSet);
    }

    public static<T>  int
    bottomUpRecursive(Node<List<SetList<T>>> current,
                      SetList<T> worldSet)
    {
        int score = 0;

        for (Node<List<SetList<T>>> child : current.getChildren()) {
            score += bottomUpRecursive(child,
                                       worldSet);
        }

        /*If a node has at least 1 child*/
        if (current.getChildren().size() >= 1) {
            List<SetList<T>> sets =
                new ArrayList<SetList<T>>(current.getChildren().size());

            for (Node<List<SetList<T>>> child : current.getChildren()) {
                sets.add(child.getData().get(0));
            }
            
            /*If node is labeled, perform regular Hartigan's.*/
            if (current.getData()==null) {
            Pair<Integer, List<SetList<T>>> hartiganResults = hartigan(sets,
                                                                       worldSet, current);
            score += hartiganResults.fst();
            current.setData(hartiganResults.snd());
            }
            
            /*If node is unlabeled, perform Fast Hartigan's.*/
            else {
            	Pair<Integer, List<SetList<T>>> fastResults = fastHartigan(sets,
                        worldSet, current.getData().get(0));
            	score += fastResults.fst();
            	current.setData(fastResults.snd());
            }
            
        }
        else {
        	List<SetList<T>> set = new ArrayList<SetList<T>>(1);
        	set.add(current.getData().get(0)); 
        	Pair<Integer, List<SetList<T>>> leafResults = hartigan(set,
                    worldSet, current);
        	current.setData(leafResults.snd());
        }

        return score;
    }
    
	/*******************************************************************************
	 * Performs top-down of Hartigan's algorithm. (See Theorem3 of Hartigan's paper.)
	 * 
	 * @param tree Tree to be refined
	 ******************************************************************************/   
    public static <T> ArrayList<ArrayList<Node<List<SetList<T>>>>>
    topDown(Tree<List<SetList<T>>> tree)
    {
        Node<List<SetList<T>>> root = tree.getRoot();
        
        int length = root.getData().get(0).size();
        
       	SetList<T> vv = new SetList<T>();
       	
        for (int index = 0; index < length; index++) {
        	
        	/** For the root, only the primary set is in the root set **/   
        	vv.add(root.getData().get(0).get(index));
        }
        
        root.getData().add(vv);

        return topDownRecursive(root);
        
    }

    public static <T> ArrayList<ArrayList<Node<List<SetList<T>>>>>
    topDownRecursive(Node<List<SetList<T>>> current)
    {   
    	ArrayList<ArrayList<Node<List<SetList<T>>>>> edges = new ArrayList();
    	int length = current.getData().get(0).size();
    	
    	if (current.getChildren().size() > 0) {
    		for (int index = 0; index < current.getChildren().size(); index++) {
    		
    		SetList<T> vvAll = new SetList<T>();
    		int cost = 0;
    
    		for (int i = 0; i<length; i++){
            SetList<T> vh = current.getChildren().get(index).getData().get(0),
                    vl = current.getChildren().get(index).getData().get(1),
                    vv = current.getData().get(2);
            
            if (vh.get(i).containsAll(vv.get(i))) {
            	vvAll.add(vv.get(i));

            } 
            
            else {
                SetList<T> newVH = new SetList<T>(vh),
                           newVL = new SetList<T>(vl);
                newVL.retainAll(vv);
                newVH.addAll(newVL);
                vvAll.add(newVH.get(i));
                
                cost +=1;
            }
           }
    		
    		if (cost == 0){
    			ArrayList<Node<List<SetList<T>>>> newEdge = new ArrayList<Node<List<SetList<T>>>>();
    			newEdge.add(current);
    			newEdge.add(current.getChildren().get(index));
    			
    			edges.add(newEdge);	
    		}
    		
    		current.getChildren().get(index).getData().add(vvAll);
    		edges.addAll (topDownRecursive(current.getChildren().get(index)));
    		}
    	}
    	
        return edges;
     } 
}
