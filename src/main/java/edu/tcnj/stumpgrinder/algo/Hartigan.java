package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*******************************************************************************
 * This class contains methods to perform Hartigan's bottom-up and town-down
 * algorithms for species with m characters. It also contains the FastHartigan
 * method for performing Hartigan's algorithm with labelled internal nodes.
 *******************************************************************************/
public class Hartigan {
    /*******************************************************************************
     * Performs the calculation of upper and lower sets as well as MP-score.
     * Used in bottom-up of Hartigan's algorithm.
     ********************************************************************************/
    //Initialize upper and lower sets of leaves
    public static <S> int hartigan(Node<S> current, List<Set<S>> worldSet) {
        int score = 0; //initialize maximum parsimony score of node
        //System.out.println("Making new upper/lower sets, length: " + length);
        current.upper = Node.sets();
        current.lower = Node.sets();//create upper and lower sets for each char of node

        ArrayList<HashMap<S, Integer>> count = new ArrayList<>(Node.chars);
        //array to store count of each char 
        List<Integer> kOccurrences = new ArrayList<>(Node.chars);
        //Initialize empty hashmap for each character in alignment
        for (int index = 0; index < Node.chars; index++) {
            count.add(new HashMap<S, Integer>());
            for (S state : worldSet.get(index)) {
                count.get(index).put(state, 0);
            }
            kOccurrences.add(-1);
        }

        //for each child
        for (Node<S> child : current.children) {
            //for each character in species alignment
            for (int index = 0; index < Node.chars; index++) {
                //for each character state 
                for (S state : child.upper.get(index)) {
                    //if alignment position contains the state
                    int newCount = count.get(index).get(state) + 1;
                    count.get(index).put(state, newCount);

                    if (newCount > kOccurrences.get(index)) {
                        kOccurrences.set(index, newCount);
                    }
                }
            }
        }

        //for each character in alignment
        for (int index = 0; index < Node.chars; index++) {
            for (S state : count.get(index).keySet()) {
                int occurrences = count.get(index).get(state);
                if (occurrences == kOccurrences.get(index)) {
                    current.upper.get(index).add(state);
                } else if (occurrences == kOccurrences.get(index) - 1) {
                    current.lower.get(index).add(state);
                }
            }
            score += current.children.size() - kOccurrences.get(index);
        }
        return score;
    }
    //Initialize upper and lower sets of leaves
    public static <S> int fastHartigan(Node<S> current) {
        int score = 0; //initialize maximum parsimony score of node
        //int length = current.children.iterator().next().upper.size(); //number of characters in alignment
        //System.out.println("Making new upper/lower sets, length: " + length);
        current.upper = Node.sets();
        current.lower = Node.sets(); //create upper and lower sets for each char of node

        //for each character in alignment
        for (int index = 0; index < Node.chars; index++) {
            S label = current.root.get(index).iterator().next();

            current.upper.get(index).add(label);

            for (Node<S> child : current.children) {
                if (!child.upper.get(index).contains(label)) {
                    score += 1;
                }
            }

        }
        return score;
    }

    /*******************************************************************************
     * Performs bottom up of Hartigan's algorithm (see Theorem2 of Hartigan's paper)
     ********************************************************************************/

    public static <S> int bottomUp(Node<S> current, List<Set<S>> worldSet) {
        int score = 0;

        for (Node<S> child : current.children) {
            score += bottomUp(child, worldSet);
        }

        /*If a node has at least 1 child*/
        if (current.children.size() >= 1) {
            // List<SetList<S>> sets =
            //     new ArrayList<SetList<S>>(current.getChildren().size());

            // for (Node<List<SetList<S>>> child : current.getChildren()) {
            //     sets.add(child.getData().get(0));
            // }

            // Pair<Integer, List<SetList<S>>> hartiganResults = hartigan(sets,
            //                                                            worldSet, current);
            // score += hartiganResults.fst();
            // current.setData(hartiganResults.snd());
            if (current.labelled) {
                score += fastHartigan(current);
            } else {
                score += hartigan(current, worldSet);
            }

        } else {
            // List<SetList<S>> set = new ArrayList<SetList<S>>(1);
            // set.add(current.getData().get(0));
            // Pair<Integer, List<SetList<S>>> leafResults = hartigan(set,
            //            worldSet, current);
            // current.setData(leafResults.snd());

//            int length = current.root.size();
            //System.out.println("Label: " + current.label + " setLength: " + length);
            current.upper = current.root;
            current.lower = Node.sets();
        }

        return score;
    }

    /*******************************************************************************
     * Performs top-down of Hartigan's algorithm. (See Theorem3 of Hartigan's paper.)
     ********************************************************************************/

    public static <S> List<List<Node<S>>> topDown(Node<S> current) {
        List<List<Node<S>>> edges = new ArrayList<>();
//    	int length = current.root.size();

        if (current.children.size() > 0) {
            //for each child
            for (Node<S> child : current.children) {

                //List<Set<S>> vvAll = new ArrayList<>();
                int cost = 0;

                //for each character in sequence
                for (int i = 0; i < Node.chars; i++) {

                    if (child.upper.get(i).containsAll(current.root.get(i))) {
                        child.root.add(current.root.get(i));
                    } else {
                        CharacterList<S> newVH = new CharacterList<>(),
                                newVL = new CharacterList<>();
                        //System.out.println("child.upper size: " + child.upper.size());
                        //System.out.println("child.lower size: " + child.lower.size());
                        for (Set<S> character : child.upper) {
                            newVH.add(new HashSet<>(character));
                        }
                        for (Set<S> character : child.lower) {
                            newVL.add(new HashSet<>(character));
                        }
                        // while(newVL.size() < newVH.size()) {
                        // newVL.add(new HashSet<S>());
                        // }

                        //System.out.println("newVL size: " + newVL.size() +
                        //    "current.root size: " + current.root.size());
                        newVL.get(i).retainAll(current.root.get(i));
                        newVH.get(i).addAll(newVL.get(i));
                        child.root.add(newVH.get(i));
                    }
                    //create sets to test if their intersection is empty
                    Set<S> x, y;
                    x = new HashSet<>(child.root.get(i));
                    y = current.root.get(i);
                    //get intersection of neighboring root sets
                    x.retainAll(y);
                    //keep track of the total cost of the edge
                    if (x.size() == 0) {
                        cost += 1;
                    }
                }

    		/*Add 0-min-cost edges to list.*/
                if (cost == 0) {
                    List<Node<S>> newEdge = new ArrayList<>();
                    newEdge.add(current);
                    newEdge.add(child);

                    edges.add(newEdge);
                }

                //update root set of node
//    		child.root = vvAll;

                //recursively perform top down algorithm to get all 0-min-cost edges
                edges.addAll(topDown(child));
            }
        }

        return edges;
    }
}
