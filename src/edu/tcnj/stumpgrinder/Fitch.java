package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/********************************************************************************
 *Performs the bottom up procedure of Fitch's algorithm.
 ********************************************************************************/
public class 
Fitch
{
    /** Assertion: sets.size() == 2 **/
    public static <T> Pair<Integer, List<SetList<T>>>
    fitch(List<SetList<T>> sets)
    {
        SetList<T> xs = sets.get(0), ys = sets.get(1),
                   zs = new SetList<T>(xs.size());
        HashSet<T> x, y, z;
        int score = 0;

        for (int i = 0; i < zs.size() ; i++) {
            x = xs.get(i); y = ys.get(i);

            /** Intersection **/
            z = new HashSet<T>(x);     
            z.retainAll(y);

            if (z.isEmpty()) {         /** If Intersection is empty **/
                z = new HashSet<T>(x); /** Union **/
                z.addAll(y);
                score += 1;
            }
            zs.set(i, z);
        }

        List<SetList<T>> setResults = new ArrayList<SetList<T>>(1);
        setResults.add(zs);

        return new Pair<Integer, List<SetList<T>>>(score, setResults);
    }

    /**************************************************************************
     * Perform the bottom up portion of Fitch's algorithm on a tree.
     **************************************************************************/
    public static <T> int
    bottomUp(Tree<List<SetList<T>>> tree)
    {
        return bottomUpRecursive(tree.getRoot());
    }

    /**************************************************************************
     * Perform the bottom up portion of Fitch's algorithm on a subtree
     **************************************************************************/
    public static <T> int
    bottomUpRecursive(Node<List<SetList<T>>> current)
    {
        int score = 0;

        for (Node<List<SetList<T>>> child : current.getChildren()) {
            score += bottomUpRecursive(child);
        }

        if (current.getChildren().size() == 2) {
            List<SetList<T>> sets = 
                new ArrayList<SetList<T>>(2);

            for (Node<List<SetList<T>>> child : current.getChildren()) {
                sets.add(child.getData().get(0));
            }

            Pair<Integer, List<SetList<T>>> results = fitch(sets);
            score += results.fst();
            current.setData(results.snd());
        }

        return score;
    }
}
