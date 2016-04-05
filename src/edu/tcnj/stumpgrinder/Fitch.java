package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class 
Fitch
{
    public static <T> Pair<Integer, SetList<T>>
    fitch(List<SetList<T>> sets)
    {
        int score = 0, length = sets.get(0).size();
        SetList<T> xs = sets.get(0), ys = sets.get(1),
                   zs = new SetList<T>(xs.size());
        HashSet<T> x, y, z;

        for (int index = 0; index < length; index++) {
            x = xs.get(index); y = ys.get(index);

            z = new HashSet<T>(x); z.retainAll(y);

            if (z.isEmpty()) {
                z = new HashSet<T>(x); z.addAll(y);
                score += 1;
            }
            zs.set(index, z);
        }

        Pair<Integer, SetList<T>> results =
            new Pair<Integer, SetList<T>>(score, zs);
        return results;
    }

    /** Perform the bottom up portion of Fitch's algorithm on a tree */
    public static <T> int
    bottomUp(Tree<SetList<T>> tree)
    {
        int score = 0;

        score += bottomUpRecursive(tree.getRoot());

        return score;
    }

    /** Perform the bottom up portion of Fitch's algorithm on a subtree */
    public static <T> int
    bottomUpRecursive(Node<SetList<T>> current)
    {
        int score = 0;

        for (Node<SetList<T>> child : current.getChildren()) {
            score += bottomUpRecursive(child);
        }

        if (current.getChildren().size() == 2) {
            ArrayList<SetList<T>> sets = 
                new ArrayList<SetList<T>>(2);

            for (Node<SetList<T>> child : current.getChildren()) {
                sets.add(child.getData());
            }

            Pair<Integer, SetList<T>> fitchResults = fitch(sets);
            score += fitchResults.fst();
            current.setData(fitchResults.snd());
        }

        return score;
    }
}
