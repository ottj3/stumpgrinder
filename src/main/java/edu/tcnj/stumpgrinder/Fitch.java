package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An algorithm used to score binary phylogenetic trees based on maximum
 * parsimony criteria.
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 */
public class Fitch
{
  /** Class Methods **/


  /** TODO
   * Perform the fitch operations on two character sets.
   *
   * @param xs The characters of the left child.
   * @param ys The characters of the right child.
   * @return A pair containing the score, and the characters of the parent.
   */
  public static <T> Pair<Integer, Characters<T>> fitch(Characters<T> xs,
                                                       Characters<T> ys)
    {
      /** Assertion: xs.size() == ys.size() **/
      Characters<T> zs = new Characters<T>(xs.getUpperSet().size());
      Set<T> x, y, z;
      int score = 0;

      for (int i = 0; i < zs.size(); i++)
        {
          x = xs.getFromUpperSet(i);
          y = ys.getFromUpperSet(i);

          z = new HashSet<T>(x);     /** Intersection **/
          z.retainAll(y);

          if (z.isEmpty())
            {
              z = new HashSet<T>(x); /** Union **/
              z.addAll(y);
              score += 1;
            }

          zs.addToUpperSet(i, z);
        }

      return new Pair<Integer, Characters<T>>(score, zs);
    }

  /** TODO
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

  /** TODO
   * Perform the bottom-up portion of Fitch's algorithm recursively on subtrees
   * of the current tree.
   * <p>
   * This method performs Fitch's algorithm on subtrees, treating the current
   * node as the root of a subtree.
   * 
   * @param current The root of the current subtree.
   * @return The score of the current subtree
   */
  public static <T> int bottomUpRecursive(Node<Characters<T>> current)
    {
      int score = 0;

      for (Node<Characters<T>> child : current.getChildren())
        {
          score += bottomUpRecursive(child);
        }

      if (current.getChildren().size() == 2)
        {
          /** TODO **/
          Characters<T> left =  current.getChild(0).getData();
          Characters<T> right = current.getChild(1).getData();

          Pair<Integer, Characters<T>> results = fitch(left, right);

          score += results.fst();
          current.setData(results.snd());
        }

      return score;
    }
}
