package edu.tcnj.stumpgrinder;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of algorithms for enumerating all possible tree topologies.
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 */
public class TreeEnumeration
{
  /** Variables **/

  /**
   * The current best parsimony score for any bounded tree enumeration.
   */
  private static int parsimonyScore;

  /**
   * The set of tree topologies.
   */
  private static Set<String> trees;

  /** Class methods **/


  private static <T> void initializeTree(Tree<T> tree)
    {
      if (tree.size() == 1)
        {
          tree.setRoot(tree.getNodes().get(0));
        }
      else if (tree.size() == 2)
        {
          tree.setRoot(tree.getNodes().get(0));
          tree.getRoot().makeChild(tree.getNodes().get(1));
        }
      else
        {
          Node<T> root = new Node<T>();
          tree.setRoot(root);

          for (int i = 0; i < 3; i++)
            {
              root.makeChild(tree.getNodes().get(i));
            }
        }
    }

  private static <T> void addNodeToEdge(Node<T> current,
                                        Node<T> parent,
                                        Node<T> internal,
                                        Node<T> leaf)
    {
      parent.makeChild(internal);
      internal.makeChild(current);
      internal.makeChild(leaf);

      return;
    }

  private static <T> void removeNodeFromEdge(Node<T> current,
                                             Node<T> parent,
                                             Node<T> internal,
                                             Node<T> leaf)
    {
      leaf.makeNotParent(leaf.getParent());
      parent.makeNotChild(internal);
      parent.makeChild(current);

      return;
    }

  /**
   * Enumerates all possible tree topologies for a given set of species.
   *
   * @param tree An uninitialized tree representing a set of species. This tree
   *             should have no structure and just be a list of nodes containing
   *             the labels and characters of the species.
   * @return A set of Newick format strings representing all possible tree
   *         topologies.
   */
  public static <T> Set<String> enumerate(Tree<T> tree)
    {
      // Reset the state of the algorithm by clearing trees.
      trees = new HashSet<String>();

      initializeTree(tree);
      if (tree.size() < 4)
        {
          trees.add(tree.toString());
        }
      else
        {
          enumerateRecursive(tree, tree.getRoot(), 3);
        }
      return trees;
    }

  /**
   *
   * @param tree    The tree whose topology is being explored.
   * @param current The current node being examined.
   * @param size    The current number of leaf nodes in the tree.
   */
  private static <T> void enumerateRecursive(Tree<T> tree,
                                             Node<T> current,
                                             int size)
    {
      if (size == tree.size())
        {
          trees.add(tree.toString());
        }
      else
        {
          for (int i = 0; i < current.getChildren().size(); i++)
            {
              enumerateRecursive(tree, current.getChild(0), size);
            }
          if (current != tree.getRoot())
            {
              Node<T> internal = new Node<T>();
              Node<T> leaf = tree.getNodes().get(size);
              Node<T> parent = current.getParent();

              addNodeToEdge(current, parent, internal, leaf);

              enumerateRecursive(tree, tree.getRoot(), size + 1);

              removeNodeFromEdge(current, parent, internal, leaf);
            }
        }
      return;
    }

  public static <T> Set<String> fitchEnumerate(Tree<Characters<T>> tree)
    {
      // Reset the state of the algorithm by clearing trees.
      trees = new HashSet<String>();
      parsimonyScore = -1;

      initializeTree(tree);
      if (tree.size() < 4)
        {
          trees.add(tree.toString());
        }
      else
        {
          fitchEnumerateRecursive(tree, tree.getRoot(), 3);
        }
      return trees;
    }

  private static <T> void fitchEnumerateRecursive(Tree<Characters<T>> tree,
                                                  Node<Characters<T>> current,
                                                  int size)
    {
      if (size == tree.size())
        {
          int score = Fitch.bottomUp(tree);
          if (score <= parsimonyScore)
            {
              if (score < parsimonyScore)
                {
                  parsimonyScore = score;
                  trees.clear();
                }
              trees.add(tree.toString());
            }
          else if (parsimonyScore == -1)
            {
              parsimonyScore = score;
              trees.add(tree.toString());
            }
        }
      else
        {
          for (int i = 0; i < current.getChildren().size(); i++)
            {
              fitchEnumerateRecursive(tree, current.getChild(0), size);
            }
          if (current != tree.getRoot()
              && (Fitch.bottomUp(tree) <= parsimonyScore
              || parsimonyScore == -1))
            {
              Node<Characters<T>> internal = new Node<Characters<T>>();
              Node<Characters<T>> leaf = tree.getNodes().get(size);
              Node<Characters<T>> parent = current.getParent();

              addNodeToEdge(current, parent, internal, leaf);

              fitchEnumerateRecursive(tree, tree.getRoot(), size + 1);

              removeNodeFromEdge(current, parent, internal, leaf);
            }
        }
      return;
    }

  public static <T> Set<String> hartiganEnumerate(Tree<Characters<T>> tree,
                                                  Characters<T> worldSet)
    {
      // Reset the state of the algorithm by clearing trees.
      trees = new HashSet<String>();
      parsimonyScore = -1;

      initializeTree(tree);
      if (tree.size() < 4)
        {
          trees.add(tree.toString());
        }
      else
        {
          hartiganEnumerateRecursive(tree, tree.getRoot(), 3, worldSet);
        }
      return trees;
    }

  private static <T> void hartiganEnumerateRecursive(Tree<Characters<T>> tree,
                                                     Node<Characters<T>> current,
                                                     int size,
                                                     Characters<T> worldSet)
    {
      if (size == tree.size())
        {
          int score = Hartigan.bottomUp(tree, worldSet);
          if (score <= parsimonyScore)
            {
              if (score < parsimonyScore)
                {
                  parsimonyScore = score;
                  trees.clear();
                }
              trees.add(tree.toString());
            }
          else if (parsimonyScore == -1)
            {
              parsimonyScore = score;
              trees.add(tree.toString());
            }
        }
      else
        {
          for (int i = 0; i < current.getChildren().size(); i++)
            {
              hartiganEnumerateRecursive(tree, current.getChild(0), size,
                                         worldSet);
            }
          if (current != tree.getRoot()
              && (Hartigan.bottomUp(tree, worldSet) <= parsimonyScore
              || parsimonyScore == -1))
            {
              Node<Characters<T>> internal = new Node<Characters<T>>();
              Node<Characters<T>> leaf = tree.getNodes().get(size);
              Node<Characters<T>> parent = current.getParent();

              addNodeToEdge(current, parent, internal, leaf);

              hartiganEnumerateRecursive(tree, tree.getRoot(), size + 1,
                                         worldSet);

              removeNodeFromEdge(current, parent, internal, leaf);
            }
        }
      return;
    }
}
