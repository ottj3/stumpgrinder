package edu.tcnj.stumpgrinder;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class TreeEnumeration
{
  /**
   * The current best parsimony score for any bounded tree enumeration 
   */
  private static int bestParsimonyScore;

  public static <T> Set<String> enumerate(String mode, Tree<Characters<T>> tree,
                                          Characters<T> worldSet)
    {
      Set<String> trees = new HashSet<String>();
      bestParsimonyScore = -1;

      if (tree.size() == 1)
        {
          tree.setRoot(tree.getNodes().get(0));
          trees.add(tree.toString());
        }
      else if (tree.size() == 2)
        {
          tree.setRoot(tree.getNodes().get(0));
          tree.getRoot().makeChild(tree.getNodes().get(1));
          trees.add(tree.toString());
        }
      else if (tree.size() >= 3)
        {
          Node<Characters<T>> internal = new Node<Characters<T>>();

          tree.setRoot(internal);

          for (int index = 0; index < 3; index++)
            {
              internal.makeChild(tree.getNodes().get(index));
            }

          if (tree.size() > 3)
            {
              trees = enumerateRecursive(mode, trees, tree, tree.getRoot(), 3,
                                         worldSet);
            }
          else
            {
              trees.add(tree.toString());
            }
        }

      return trees;
    }

  public static <T> Set<String> enumerateRecursive(String mode,
                                                   Set<String> trees,
                                                   Tree<Characters<T>> tree,
                                                   Node<Characters<T>> current,
                                                   int size,
                                                   Characters<T> worldSet)
    {
      //TODO
      // Set<String> trees = new HashSet<String>();
      int parsimonyScore;

      if (size == tree.size())
        {
          if (mode.equals("fitch") || mode.equals("hartigan"))
            {
              if (mode.equals("fitch"))
                {
                  parsimonyScore = Fitch.bottomUp(tree);
                }
              else
                {
                  parsimonyScore = Hartigan.bottomUp(tree, worldSet);
                }

              if (parsimonyScore < bestParsimonyScore)
                {
                  trees.clear();
                  trees.add(tree.toString());
                  bestParsimonyScore = parsimonyScore;
                }
              else if (parsimonyScore == bestParsimonyScore)
                {
                  trees.add(tree.toString());
                }
              else if (bestParsimonyScore == -1)
                {
                  bestParsimonyScore = parsimonyScore;
                  trees.add(tree.toString());
                }
            }
          else
            {
              trees.add(tree.toString());
            }
        }
      else
        {
          for (int index = 0; index < current.getChildren().size(); index++)
            {
              trees.addAll(enumerateRecursive(mode,
                                              trees,
                                              tree,
                                              current.getChildren().get(0),
                                              size,
                                              worldSet));
            }
          if (current != tree.getRoot())
            {
              if ((mode.equals("normal")) ||
                  (mode.equals("fitch") &&
                   (Fitch.bottomUp(tree) <= bestParsimonyScore ||
                    bestParsimonyScore == -1)) ||
                  (mode.equals("hartigan") &&
                   (Hartigan.bottomUp(tree, worldSet) <= bestParsimonyScore ||
                    bestParsimonyScore == -1)))
                {
                  Node<Characters<T>> newLeaf = tree.getNodes().get(size);

                  Node<Characters<T>> internal = new Node<Characters<T>>();

                  Node<Characters<T>> parent = current.getParent();

                  parent.makeNotChild(current);
                  parent.makeChild(internal);

                  internal.makeChild(current);
                  internal.makeChild(newLeaf);

                  trees.addAll(enumerateRecursive(mode,
                                                  trees,
                                                  tree, 
                                                  tree.getRoot(),
                                                  size + 1,
                                                  worldSet));

                  newLeaf.makeNotParent(newLeaf.getParent());

                  parent.makeNotChild(internal);
                  parent.makeChild(current);
                }
            }
        }
      
      return trees;
    }
}
