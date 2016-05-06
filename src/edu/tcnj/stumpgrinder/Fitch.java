package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 
Fitch
{
    /** Assertion: xs.size() == ys.size() **/
    public static <T> Pair<Integer, Characters<T>>
    fitch(Characters<T> xs, Characters<T> ys)
    {
        Characters<T> zs = new Characters<T>(xs.getUpperSet().size()); /** TODO **/
        Set<T> x, y, z;
        int score = 0;

        for (int i = 0; i < zs.size(); i++) {
            x = xs.getFromUpperSet(i);
            y = ys.getFromUpperSet(i);

            z = new HashSet<T>(x);     /** Intersection **/
            z.retainAll(y);

            if (z.isEmpty()) {
                z = new HashSet<T>(x); /** Union **/
                z.addAll(y);
                score += 1;
            }

            zs.addToUpperSet(i, z);
        }

        return new Pair<Integer, Characters<T>>(score, zs);
    }

    public static <T> int
    bottomUp(Tree<Characters<T>> tree)
    {
        return bottomUpRecursive(tree.getRoot());
    }

    public static <T> int
    bottomUpRecursive(Node<Characters<T>> current)
    {
        int score = 0;

        for (Node<Characters<T>> child : current.getChildren()) {
            score += bottomUpRecursive(child);
        }

        if (current.getChildren().size() == 2) { /** TODO **/
            Characters<T> left =  current.getChild(0).getData();
            Characters<T> right = current.getChild(1).getData();

            Pair<Integer, Characters<T>> results = fitch(left, right);

            score += results.fst();
            current.setData(results.snd());
        }

        return score;
    }
}
