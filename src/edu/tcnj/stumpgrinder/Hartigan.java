package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class
Hartigan
{
    public static <T> Pair<Integer, List<SetList<T>>>
    hartigan(List<SetList<T>> sets,
             SetList<T> worldSet)
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
            k = new HashSet<T>(); kMinusOne = new HashSet<T>(worldSet.get(index));
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

    public static <T> int
    bottomUp(Tree<List<SetList<T>>> tree,
             SetList<T> worldSet)
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

        if (current.getChildren().size() == 2) {
            List<SetList<T>> sets =
                new ArrayList<SetList<T>>(current.getChildren().size());

            for (Node<List<SetList<T>>> child : current.getChildren()) {
                sets.add(child.getData().get(0));
            }
                
            Pair<Integer, List<SetList<T>>> hartiganResults = hartigan(sets,
                                                                       worldSet);
            score += hartiganResults.fst();
            current.setData(hartiganResults.snd());
        }
            
        return score;
    }

    public static <T> void
    topDown(Tree<List<SetList<T>>> tree)
    {
        Node<List<SetList<T>>> root = tree.getRoot();
        
        /** For the root perform a union between VH and VL **/
        SetList<T> vv = new SetList<T>(root.getData().get(0));
        vv.addAll(root.getData().get(1));
        root.getData().add(vv);

        topDownRecursive(root);
        return;
    }

    public static <T> void
    topDownRecursive(Node<List<SetList<T>>> current)
    {
        Node<List<SetList<T>>> parent = current.getParent();

        if (parent != null) {
            SetList<T> vh = current.getData().get(0),
                       vl = current.getData().get(1),
                       vv = parent.getData().get(2);
            if (vh.containsAll(vv)) {
                current.getData().add(vv);
            } else {
                SetList<T> newVH = new SetList<T>(vh),
                           newVL = new SetList<T>(vl);
                newVL.retainAll(vv);
                newVH.addAll(newVL);
                current.getData().add(newVH);
            }
        }

        return;
    }
}
