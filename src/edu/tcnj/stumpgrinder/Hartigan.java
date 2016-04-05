package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class
Hartigan
{
    public static <T> Pair<Integer, SetList<T>>
    hartigan(List<SetList<T>> sets)
    {
        int score = 0, length = sets.get(0).size();
        SetList<T> vh = new SetList<T>(length),
                   vl = new SetList<T>(length);
        ArrayList<HashMap<T, Integer>> count = 
            new ArrayList<HashMap<T, Integer>>(length);
        for (int index = 0; index < length; index++) {
            count.add(new HashMap<T, Integer>());
        }

        for (SetList<T> set : sets) {
            for (int index = 0; index < length; index++) {
                for (T state : set.get(index)) {
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

        HashSet<T> k, kMinusOne;
        int occurences, kOccurences, kMinusOneOccurences;

        for (int index = 0; index < length; index++) {
            k = new HashSet<T>(); kMinusOne = new HashSet<T>();
            kOccurences = 1; kMinusOneOccurences = 0;

            for (T state : count.get(index).keySet()) {
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

        Pair<Integer, SetList<T>> results =
            new Pair<Integer, SetList<T>>(score, vh);
        return results;
    }

    public static <T> int
    bottomUp(Tree<SetList<T>> tree)
    {
        int score = 0;

        score += bottomUpRecursive(tree.getRoot());

        return score;
    }

    public static<T>  int
    bottomUpRecursive(Node<SetList<T>> current)
    {
        int score = 0;

        for (Node<SetList<T>> child : current.getChildren()) {
            score += bottomUpRecursive(child);
        }

        /** TODO: As long as all the children have sets **/
        if (current.getChildren().size() == 2) {
            ArrayList<SetList<T>> sets = 
                new ArrayList<SetList<T>>(2);

            for (Node<SetList<T>> child : current.getChildren()) {
                sets.add(child.getData());
            }

            Pair<Integer, SetList<T>> fitchResults = hartigan(sets);
            score += fitchResults.fst();
            current.setData(fitchResults.snd());
        }

        return score;
    }

    public static <T> void
    topDown(Tree<SetList<T>> tree)
    {
        Node<SetList<T>> root = tree.getRoot();
        
        /** For the root perform a union between VH and VL **/
        root.getData().get(0).addAll(root.getData().get(1));
        root.getData().remove(1);

        topDownRecursive(root);
        return;
    }

    public static <T> void
    topDownRecursive(Node<SetList<T>> current)
    {
        Node<SetList<T>> parent = current.getParent();

        if (parent != null) {
            SetList<T> vv = parent.getData().get(0), 
                       vh = current.getData().get(0),
                       vl = current.getData().get(1);
            if (vh.containsAll(vv)) {
                current.setData(vv);
            } else {
                current.setData(vh.addAll(vl.retainAll(vv)));
            }
        }

        return;
    }
}
