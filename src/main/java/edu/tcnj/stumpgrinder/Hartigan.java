package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class
Hartigan
{
    public static <T> Pair<Integer, Characters<T>>
    hartigan(List<Characters<T>> sets, Characters<T> worldSet)
    {
        int score = 0;
        int length = sets.get(0).getUpperSet().size();

        Characters<T> zs = new Characters<T>(length);

        List<Map<T, Integer>> count = new ArrayList<Map<T, Integer>>(length);
        for (int i = 0; i < length; i++) {
            count.add(new HashMap<T, Integer>());
        }

        for (Characters<T> set : sets) {
            for (int i = 0; i < length; i++) {
                for (T state : set.getFromUpperSet(i)) {
                    if (count.get(i).containsKey(state)) {
                        count.get(i).put(state, count.get(i).get(state) + 1);
                    } else {
                        count.get(i).put(state, 1);
                    }
                }
            }
        }

        Set<T> k, kMinusOne;
        int occurences, kOccurences, kMinusOneOccurences;
        for (int i = 0; i < length; i++) {
            k = new HashSet<T>();
            kOccurences = 1;

            kMinusOne = new HashSet<T>(worldSet.getFromRootSet(i));
            kMinusOneOccurences = 0;

            for (T state : count.get(i).keySet()) {
                occurences = count.get(i).get(state);
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
            zs.addToUpperSet(i, k);
            zs.addToLowerSet(i, kMinusOne);
        }
        
        return new Pair<Integer, Characters<T>>(score, zs);
    }

    public static <T> int
    bottomUp(Tree<Characters<T>> tree,
             Characters<T> worldSet)
    {
        return bottomUpRecursive(tree.getRoot(), worldSet);
    }

    public static<T> int
    bottomUpRecursive(Node<Characters<T>> current,
                      Characters<T> worldSet)
    {
        int score = 0;

        for (Node<Characters<T>> child : current.getChildren()) {
            score += bottomUpRecursive(child,
                                       worldSet);
        }

        if (current.getChildren().size() > 0) {
            List<Characters<T>> sets =
                new ArrayList<Characters<T>>(current.getChildren().size());

            for (Node<Characters<T>> child : current.getChildren()) {
                sets.add(child.getData());
            }
                
            Pair<Integer, Characters<T>> results = hartigan(sets,
                                                           worldSet);
            score += results.fst();
            current.setData(results.snd());
        }

        return score;
    }

    public static <T> void
    topDown(Tree<Characters<T>> tree)
    {
        Node<Characters<T>> root = tree.getRoot();
        List<Set<T>> upper, lower;
        Characters<T> r;
        
        /** For the root perform a union between VH and VL **/
        Characters<T> characters = root.getData();
        /** TODO **/
        characters.setRootSet(new ArrayList<Set<T>>(characters.getUpperSet()));
        characters.getRootSet().addAll(characters.getLowerSet());

        r = root.getData();
        r.setRootSet(r.getUpperSet());

        topDownRecursive(root);
    }

    public static <T> void topDownRecursive(Node<Characters<T>> current)
      {
        Node<Characters<T>> parent = current.getParent();
        List<Set<T>> upper, lower;

        if (parent != null)
          {
            Characters<T> c = current.getData();
            Characters<T> p = parent.getData();

            if (c.getUpperSet().containsAll(p.getRootSet()))
              {
                c.setRootSet(p.getRootSet());
              }
            else
              {
                upper = new ArrayList<Set<T>>(c.getUpperSet());
                lower = new ArrayList<Set<T>>(c.getLowerSet());

                for (int i = 0; i < lower.size(); i++)
                  {
                    lower.get(i).retainAll(p.getRootSet().get(i));
                    upper.get(i).addAll(lower.get(i));
                    // lower.set(i, lower.get(i));
                    // upper.set(i, upper.get(i));
                  }
                c.setRootSet(upper);
              }
          }

        for (Node<Characters<T>> child : current.getChildren())
          {
            topDownRecursive(child);
          }
      }
}
