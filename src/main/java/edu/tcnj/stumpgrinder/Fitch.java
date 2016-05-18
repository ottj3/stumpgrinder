package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An algorithm used to score binary phylogenetic trees based on maximum
 * parsimony criteria.
 * <p>
 * This algorithm is a specialized version of Hartigan's. Due to its specialized
 * nature it is much simpler and can be run more quickly.
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 */
public class Fitch
{
  /** Class Methods **/


  /**
   * Perform the fitch operations on two character sets.
   *
   * @param lefts The characters of the left child.
   * @param rights The characters of the right child.
   * @return A pair containing the score, and the characters of the parent.
   */
  private static <T> Pair<Integer, Characters<T>> fitch(Characters<T> lefts,
                                                       Characters<T> rights)
    {
      Characters<T> parents = new Characters<T>(lefts.getUpperSet().size());
      Set<T> left, right, parent;
      int score = 0;

      for (int i = 0; i < parents.size(); i++)
        {
          left = lefts.getFromUpperSet(i);
          right = rights.getFromUpperSet(i);

          // Intersection
          parent = new HashSet<T>(left);
          parent.retainAll(right);

          // Union, if intersection is empty.
          if (parent.isEmpty())
            {
              parent = new HashSet<T>(left);
              parent.addAll(right);
              score += 1;
            }

          parents.addToUpperSet(i, parent);
        }

      return new Pair<Integer, Characters<T>>(score, parents);
    }

  /**
   * Perform the bottom-up portion of Fitch's algorithm on a tree.
   * <p>
   * This method is a wrapper around the bottumUpRecursive method.
   *
   * @param tree The tree to score.
   * @return The score of the tree.
   */
  public static <T> int bottomUp(Tree<Characters<T>> tree)
    {
      tree.root();
      int score = bottomUpRecursive(tree.getRoot());
      tree.unroot();
      return score;
    }

  /**
   * Perform the bottom-up portion of Fitch's algorithm recursively on subtrees
   * of the current tree.
   * 
   * @param current The root of the current subtree.
   * @return The score of the current subtree
   */
  private static <T> int bottomUpRecursive(Node<Characters<T>> current)
    {
      int score = 0;

      for (Node<Characters<T>> child : current.getChildren())
        {
          score += bottomUpRecursive(child);
        }

      if (current.getChildren().size() == 2)
        {
          Characters<T> left =  current.getChild(0).getData();
          Characters<T> right = current.getChild(1).getData();

          Pair<Integer, Characters<T>> results = fitch(left, right);

          score += results.fst();
          current.setData(results.snd());
        }

      return score;
    }
}
