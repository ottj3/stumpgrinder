package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/*******************************************************************************
 * This class contains methods to perform Hartigan's bottom-up algorithm.
 * The bottom-up implementation also accounts for internal labelled nodes 
 * (fast-hartigan's algorithm).
 * 
 * @date (Spring 2016)
 * @version 2.0
 ******************************************************************************/
public class
FastHartigan extends Hartigan
{
	/*******************************************************************************
	 * Performs Fast Hartigan's algorithm:
	 * Performs the calculation of upper and lower sets as well as MP-score of  
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
            
            T label = data.get(index).iterator().next();
            
            k.add(label);
           
            int kv = count.get(index).get(label);
            
            score += sets.size() - kv;
            
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
	 * @Override Overrides bottom up method in Hartigan class
	 * 
	 * Performs bottom up of Hartigan's algorithm (see Theorem2 of Hartigan's paper).
	 * If the node is unlabelled, perform regular Hartigan's algorithm.
	 * If the node is labelled, perform fast Hartigan's
	 * to calculate MP score and VU, VL, VV sets.
	 * 
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
            
            /*If node is unlabeled, perform regular Hartigan's.*/
            if (current.getData()==null) {
            Pair<Integer, List<SetList<T>>> hartiganResults = hartigan(sets,
                                                                       worldSet, current);
            score += hartiganResults.fst();
            current.setData(hartiganResults.snd());
            }
            
            /*If node is labeled, perform Fast Hartigan's.*/
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
}
