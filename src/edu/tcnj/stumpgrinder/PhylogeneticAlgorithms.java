package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class
PhylogeneticAlgorithms
{
    public static HashSet<String>
    enumerate(Tree<SetList<Character>> tree)
    {
        int parsimonyScore = -1;
        HashSet<String> trees = new HashSet<String>();
        
        if (tree.size() == 1) {

        } else if (tree.size() == 2) {

        } else if (tree.size() >= 3) {
            Node<SetList<Character>> internal = new Node<SetList<Character>>();

        }


        return trees;
    }

    /**************************************************************************
     * @param sets A list of sets 
     * @return A pair containing the score and new set generated.
     **************************************************************************/
    public static Pair<Integer, SetList<Character>>
    fitch(List<SetList<Character>> sets)
    {
        int score = 0, length = sets.get(0).size();
        SetList<Character> xs = sets.get(0), ys = sets.get(1),
                           set = new SetList<Character>(length);
        HashSet<Character> x, y, z, intersection, union;

        for (int index = 0; index < length; index++) {
            x = xs.get(index); y = ys.get(index);

            intersection = new HashSet<Character>(x); intersection.retainAll(y);

            if (intersection.isEmpty()) {
                union = new HashSet<Character>(x); union.addAll(y);
                set.set(index, union);
                score += 1;
            } else {
                set.set(index, intersection);
            }
        }

        Pair<Integer, SetList<Character>> results =
            new Pair<Integer, SetList<Character>>(score, set);
        return results;
    }

    public static int
    fitchBottomUp(Tree<SetList<Character>> tree)
    {
        Node<SetList<Character>> current = tree.getRoot();
        int score = 0;

        fitchBottomUpRecursive(current);

        return score;
    }

    public static int
    fitchBottomUpRecursive(Node<SetList<Character>> current)
    {
        int score = 0;

        for (Node<SetList<Character>> child : current.getChildren()) {
            score += fitchBottomUpRecursive(child);
        }

        if (current.getChildren().size() == 2) {
            ArrayList<SetList<Character>> sets = 
                new ArrayList<SetList<Character>>(2);

            for (Node<SetList<Character>> child : current.getChildren()) {
                sets.add(child.getData());
            }

            Pair<Integer, SetList<Character>> fitchResults = fitch(sets);
            score += fitchResults.fst();
            current.setData(fitchResults.snd());
        }

        return score;
    }

    public static Pair<Integer, SetList<Character>>
    hartigan(List<SetList<Character>> sets)
    {
        int score = 0, length = sets.get(0).size();
        SetList<Character> vh = new SetList<Character>(length),
                           vl = new SetList<Character>(length);
        ArrayList<HashMap<Character, Integer>> count = 
            new ArrayList<HashMap<Character, Integer>>(length);
        for (int index = 0; index < length; index++) {
            count.add(new HashMap<Character, Integer>());
        }

        for (SetList<Character> set : sets) {
            for (int index = 0; index < length; index++) {
                for (Character state : set.get(index)) {
                    if (count.get(index).containsKey(state)) {
                        count.get(index).put(state, 
                                             count.get(index).get(state) + 1);
                    } else {
                        count.get(index).put(state,
                                             1);
                    }
                }
            }
        }

        HashSet<Character> k, kMinusOne;
        int occurences, kOccurences, kMinusOneOccurences;

        for (int index = 0; index < length; index++) {
            k = new HashSet<Character>(); kMinusOne = new HashSet<Character>();
            kOccurences = 1; kMinusOneOccurences = 0;

            for (Character state : count.get(index).keySet()) {
                occurences = count.get(index).get(state);
                if (occurences > kOccurences) {
                    k.clear();
                    k.add(state);
                    kOccurences = occurences;
                } else if (occurences == kOccurences) {
                    k.add(state);
                } else if (occurences == kMinusOneOccurences) {
                    kMinusOne.add(state);
                }
            }

            score += sets.size() - kOccurences; 
            vh.set(index, k);
            vl.set(index,kMinusOne);
        }

        Pair<Integer, SetList<Character>> results =
            new Pair<Integer, SetList<Character>>(score, vh);
        return results;
    }

    public static int
    hartiganBottomUp(Tree<SetList<Character>> tree)
    {
        Node<SetList<Character>> current = tree.getRoot();
        int score = 0;

        hartiganBottomUpRecursive(current);

        return score;
    }

    public static int
    hartiganBottomUpRecursive(Node<SetList<Character>> current)
    {
        int score = 0;

        for (Node<SetList<Character>> child : current.getChildren()) {
            score += hartiganBottomUpRecursive(child);
        }

        /** TODO: As long as all the children have sets **/
        if (current.getChildren().size() == 2) {
            ArrayList<SetList<Character>> sets = 
                new ArrayList<SetList<Character>>(2);

            for (Node<SetList<Character>> child : current.getChildren()) {
                sets.add(child.getData());
            }

            Pair<Integer, SetList<Character>> fitchResults = hartigan(sets);
            score += fitchResults.fst();
            current.setData(fitchResults.snd());
        }

        return score;
    }
}

class Pair<T1, T2>
{
    public final T1
    first;

    public final T2
    second;

    public
    Pair(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
    }

    public T1
    fst()
    {
        return first;
    }

    public T2
    snd()
    {
        return second;
    }
}
