package edu.tcnj.stumpgrinder;

import java.util.List;

/**************************************************************************
 * The edge contraction algorithm.
 *
 * @author  Angela Huang <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 **************************************************************************/
public class
EdgeContraction
{
  private static int
  bestCompactnessScore;

  public static <T> void edgeContraction(Tree<Characters<T>> tree,
                                         Characters<T> worldSet,
                                         int length)
    {
      Hartigan.bottomUp(tree, worldSet);
      Hartigan.topDown(tree);

      // edges = 
      
      if (edges.size() == 0)
        {

        }
      else
        {
          edgeContractionRecursive(tree, worldSet, length);
        }
    }

  private static <T> void edgeContractionRecursive(Tree<Characters<T> tree,
                                                   Characters<T> worldSet,
                                                   int length)
    {


    }

  /**
   * Helper method to get all parent nodes up to the root. Allows us to root a
   * tree by that node.
   *
   * @param node Node that we want to root the tree by
   * @return A list of parent nodes within the path to the root node
   */
  public static <T> List<Node<Characters<T>>> getNodesToRoot(Node<Characters<T> node)
    {
      List<Node<Characters<T>> nodesToRoot = new ArrayList<Node<Characters<T>>>();
      return getNodesToRootRecursive(node.getParent(), nodesToRoot);
    }

  private static <T> List<Node<Characters<T>>> getNodesToRootRecursive(Node<Characters<T>> node,
                                                                       List<Node<Characters<T>> list)
    {
      if (node == null)
        {
          return null;
        }
      else
        {
          list.add(node);
          getNodesToRootRecursive(node.getParent(), list);
        }
      return list;
    }
}
