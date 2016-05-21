package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/*******************************************************************************
 * This class overrides Hartigan's bottom-up algorithm. 
 * Modified bottom-up of Hartigan's to work with edge contraction algorithm.
 * 
 * @author Angela Huang <huanga9@tcnj.edu> and Andrew Miller <millea18@tcnj.edu> 
 * @date (Spring 2016)
 * @version 3.0
 ******************************************************************************/

public class
ModifiedHartigan extends Hartigan
{
   
    /*******************************************************************************
     * Performs a modified version of Hartigan's bottom-up algorithm.
     * This version is modified to compute VU and VL sets for all nodes except for 
     * the root node. 
     * Used with the edge contraction algorithm to update the VU and VL sets 
     * of the new tree after contracting an edge. 
     * @param tree Tree to be computed
     * @param worldSet This set contains all possible character states. 
     * @param data This is the data of the labelled node
     *        (Used when K=1)	
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
            
            /*Perform regular Hartigan's on all nodes, except for the root node.*/
           if (current.hasParent()) {
            Pair<Integer, List<SetList<T>>> hartiganResults = hartigan(sets,
                                                                       worldSet, current);
            score += hartiganResults.fst();
            current.setData(hartiganResults.snd());
            }
            
           else{
        	   current.getData().remove(2);
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
