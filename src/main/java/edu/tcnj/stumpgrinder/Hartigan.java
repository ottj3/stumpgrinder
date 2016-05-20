package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An algorithm used to score phylogenetic trees based on maximum parsimony
 * criteria.
 * <p>
 * This algorithm is a generalized version of Fitch's algorithm. Due to its
 * generalized nature it is more complicated and runs slower.
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 */
public class
Hartigan
{
  /** Class Methods **/


  /**
   * Perform the hartigan operations on a list of character sets.
   *
   * @param sets The character sets to perform the operations on.
   * @param worldSet The set containing all possible values for every character
   *                 state.
   * @return A pair containing the score, and the characters of the parent.
   */
  private static <T> Pair<Integer, Characters<T>>
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

  /**
   * Perform the bottom-up portion of Hartigan's algorithm on a tree.
   * <p>
   * This method is a wrapper around the bottumUpRecursive method.
   *
   * @param tree The tree to score.
   * @param worldSet The set containing all possible values for every character
   *                 state.
   * @return The score of the tree.
   */
  public static <T> int
  bottomUp(Tree<Characters<T>> tree, Characters<T> worldSet)
    {
      return bottomUpRecursive(tree.getRoot(), worldSet);
    }

  /**
   * Perform the bottom-up portion of Hartigan's algorithm recursively on
   * subtrees of the current tree.
   * <p>
   * This method perform Hartigan's algorithm on subtrees, treating the current
   * node as the root of a subtree.
   *
   * @param current The root of the current subtree.
   * @param worldSet The set containing all possible values for every character
   *                 state.
   * @return The score of the current subtree.
   */
  private static<T> int
  bottomUpRecursive(Node<Characters<T>> current, Characters<T> worldSet)
    {
      int score = 0;

      for (Node<Characters<T>> child : current.getChildren()) {
          score += bottomUpRecursive(child, worldSet);
      }

      if (current.getChildren().size() >= 1) {
          List<Characters<T>> sets =
              new ArrayList<Characters<T>>(current.getChildren().size());

          for (Node<Characters<T>> child : current.getChildren()) {
              sets.add(child.getData());
          }
              
          Pair<Integer, Characters<T>> results = hartigan(sets, worldSet);
          current.setData(results.snd());
          score += results.fst();
      }

      return score;
    }

  /**
   * Performs the top-down portion of Hartigan's algorithm on a tree.
   * <p>
   * This method initializes the characters of the root node before invoking
   * topDownRecursive on the root of the tree.
   *
   * @param tree The tree to refine the character states of.
   */
  public static <T> void topDown(Tree<Characters<T>> tree)
    {
      Node<Characters<T>> root = tree.getRoot();
      Characters<T> r = root.getData();
      r.setRootSet(r.getUpperSet());

      topDownRecursive(root);
    }

  /**
   * Performs the top-down portion of Hartigan's algortihm recursively on
   * subtrees of the current tree.
   *
   * @param current The root of the current subtree.
   */
  private static <T> void topDownRecursive(Node<Characters<T>> current)
    {
      Node<Characters<T>> parent = current.getParent();
      List<Set<T>> vv, vh, vl;


      if (parent != null)
        {
          Characters<T> c = current.getData();
          Characters<T> p = parent.getData();

          vv = new ArrayList<Set<T>>(p.getRootSet());
          vh = new ArrayList<Set<T>>(c.getUpperSet());
          vl = new ArrayList<Set<T>>(c.getLowerSet());

          for (int i = 0; i < vv.size(); i++)
            {
              if (vh.get(i).containsAll(vv.get(i)))
                {
                  vh.set(i, vv.get(i));
                }
              else
                {
                  if (vl.size() >= vh.size())
                    {
                      vv.get(i).retainAll(vv.get(i));
                      vh.get(i).addAll(vv.get(i));
                    }
                  else
                    {
                      vh.get(i).addAll(vv.get(i));
                    }

                }
            }
            c.setRootSet(vh);
        }

      for (Node<Characters<T>> child : current.getChildren())
        {
          topDownRecursive(child);
        }
    }
}
