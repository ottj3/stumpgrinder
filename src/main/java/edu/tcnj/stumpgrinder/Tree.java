package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A tree.
 *
 * @author Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 */
public class Tree<T>
{
  /**
   * A pointer to the root of this tree 
   */
  private Node<T> root;

  /**
   * A list of the leaf nodes of this tree 
   */
  private List<Node<T>> leaves;

  /**
   * A list of the internal nodes of this tree
   */
  private List<Node<T>> internals;

  /**
   * A list of all of the nodes in this tree
   */
  private List<Node<T>> nodes;

  /**
   * Constructs an empty tree.
   */
  public Tree()
    {
      this.root      = null;
      this.leaves    = new ArrayList<Node<T>>();
      this.internals = new ArrayList<Node<T>>();
      this.nodes     = new ArrayList<Node<T>>();
    }

  /**
   * Constructs a tree with only a root node.
   *
   * @param root The node to be the root of the tree.
   */
  public Tree(Node<T> root)
    {
      this.root      = root;
      this.leaves    = new ArrayList<Node<T>>();
      this.internals = new ArrayList<Node<T>>();
      this.nodes     = new ArrayList<Node<T>>();
      nodes.add(root);
    }

  /**
   * Constructs a tree with a root and leaf nodes.
   *
   * @param root The node to be the root of the tree.
   * @param leaves A list of nodes to be used as the leaf nodes of the tree.
   */
  public Tree(Node<T> root, Collection<? extends Node<T>> leaves)
    {
      this.root      = root;
      this.leaves    = new ArrayList<Node<T>>(leaves);
      this.internals = new ArrayList<Node<T>>();
      this.nodes     = new ArrayList<Node<T>>();
      nodes.add(root);
      nodes.addAll(leaves);
    }

  /**
   * Constructs a tree with a root, leaf nodes, and internal nodes.
   *
   * @param root The node to be the root of the tree.
   * @param leaves A list of nodes to be used as the leaf nodes of the tree.
   * @param internals A list of nodes to be used as the internal nodes of the
   *                  tree.
   */
  public Tree(Node<T> root, Collection<? extends Node<T>> leaves,
              Collection<? extends Node<T>> internals)
    {
      this.root      = root;
      this.leaves    = new ArrayList<Node<T>>(leaves);
      this.internals = new ArrayList<Node<T>>();
      this.nodes     = new ArrayList<Node<T>>();
      nodes.add(root);
      nodes.addAll(leaves);
      nodes.addAll(internals);
    }

  /**
   * Constructs a tree as a collection of nodes to be structured later.
   *
   * @param nodes The nodes in the tree.
   */
  public Tree(Collection<? extends Node<T>> nodes)
    {
      this.root      = null;
      this.leaves    = new ArrayList<Node<T>>();
      this.internals = new ArrayList<Node<T>>();
      this.nodes     = new ArrayList<Node<T>>(nodes);
    }

  /**
   * Returns the root of this tree.
   *
   * @return The root of this tree.
   */
  public Node<T> getRoot()
    {
      return this.root;
    }

  /**
   * Returns a list of the leaf nodes in this tree.
   *
   * @return A list of the leaf nodes in this tree.
   */
  public List<Node<T>> getLeaves()
    {
      return this.leaves;
    }

  /**
   * Returns a list of the internal nodes in this tree.
   *
   * @return A list of the internal nodes in this tree.
   */
  public List<Node<T>> getInternals()
    {
      return this.internals;
    }

  /**
   * Returns a list of the nodes in this tree.
   *
   * @return A list of the nodes in this tree.
   */
  public List<Node<T>> getNodes()
    {
      return this.nodes;
    }

  /**
   * Replaces the root of this tree.
   *
   * @param root The new root of the tree.
   */
  public void setRoot(Node<T> root)
    {
      this.root = root;
    }

  /**
   * Replaces the list of leaf nodes in this tree.
   * @param leaves A collection of the new leaf nodes in this tree.
   */
  public void setLeaves(Collection<? extends Node<T>> leaves)
    {
      this.leaves = new ArrayList<Node<T>>(leaves);
    }

  /**
   * Replaces the list of internal nodes in this tree.
   *
   * @param internals A collection of the new internal nodes in this tree.
   */
  public void setInternals(Collection<? extends Node<T>> internals)
    {
      this.internals = new ArrayList<Node<T>>(internals);
    }

  /**
   * Replaces the list of nodes in this tree.
   *
   * @param nodes A collection of the new nodes in this tree.
   */
  public void setNodes(Collection<? extends Node<T>> nodes)
    {
      this.nodes = new ArrayList<Node<T>>(nodes);
    }

  /**
   * Checks if a node is in this tree.
   *
   * @param node The node to search this tree for.
   * @return true if the node is in this tree.
   */
  public boolean inTree(Node<T> node)
    {
      return nodes.contains(node);
    }

  /**
   * Adds a leaf node to this tree.
   *
   * @param leaf The node to be added to the leaves of this tree.
   * @return true if leaf is not already in the leaves of this tree.
   */
  public boolean addLeaf(Node<T> leaf)
    {
      if (!leaves.contains(leaf))
        {
          this.leaves.add(leaf);
          addNode(leaf);
          return true;
        }
      else
        {
          return false;
        }
    }

  /**
   * Adds an internal node to this tree.
   *
   * @param internal The node to be added to the internals of this tree.
   * @return true if internal is not already in the internals of this tree.
   */
  public boolean addInternal(Node<T> internal)
    {
      if (!internals.contains(internal))
        {
          this.internals.add(internal);
          addNode(internal);
          return true;
        }
      else
        {
          return false;
        }
    }

  /**
   * Adds a node to this tree.
   *
   * @param node The node to be added to this tree.
   * @return true if node is not already in this tree.
   */
  public boolean addNode(Node<T> node)
    {
      if (!nodes.contains(node))
        {
          this.nodes.add(node);
          return true;
        }
      else
        {
          return false;
        }
    }

  public boolean removeLeaf(Node<T> leaf)
    {
      if (leaves.contains(leaf))
        {
          this.leaves.remove(leaf);
          removeNode(leaf);
          return true;
        }
      else
        {
          return false;
        }
    }

  public boolean removeInternal(Node<T> internal)
    {
      if (internals.contains(internal))
        {
          this.internals.remove(internal);
          removeNode(internal);
          return true;
        }
      else
        {
          return false;
        }
    }

  /**
   * Removes a node from this tree.
   *
   * @param node The node to be removed from this tree.
   * @return true if node is not in this tree.
   */
  public boolean removeNode(Node<T> node)
    {
      if (nodes.contains(node))
        {
          this.nodes.remove(node);
          return true;
        }
      else
        {
          return false;
        }
    }

  /**
   * Returns the number of nodes in the tree.
   *
   * @return The number of nodes in the tree.
   */
  public int size()
    {
      return nodes.size();
    }

  /**
   * Returns a string representation of the tree. (Newick Tree Format)
   *
   * @return A string representation of the tree.
   */
  public String toString()
    {
      String string = toStringRecursive(root) + this.root.getLabel() + ";";
      return string.replace(",)", ")");
    }

  /**
   * Returns a string representation of the subtree from a specified node.
   *
   * @return A string representation of the subtree from a specified node.
   */
  public String toStringRecursive(Node<T> current)
    {
      String string = "";

      if (current.getChildren().size() > 0)
        {
          string = ")" + string;
        }

      for (Node<T> child: current.getChildren())
        {
          if (child.getDistance() != null)
            {
              string = child.getLabel() + ":" + child.getDistance() +","
                       + string;
            }
          else
            {
              string = child.getLabel() + "," + string;
            }
          string = toStringRecursive(child) + string;
        }

      if (current.getChildren().size() > 0)
        {
          string = "(" + string;
        }

      return string;
    }

  /**
   * Returns the node in the tree with the specified label.
   *
   * @param label Label of the node to be searched for.
   * @return Node belonging to tree if node is found, otherwise null
   */
  public Node<T> getNode(String label)
    {
      for (Node<T> node : this.nodes)
        {
          if (node.getLabel().equals(label))
            {
              return node;
            }
        }
      return null;
    }

  /**
   * Returns the node in the tree with the specified label if found, otherwise
   * retuns the default node.
   *
   * @param label Label of the node to be searched for.
   * @param defaultNode The default node to be returned.
   * @return Node belonging to tree if node is found, otherwise defaultNode.
   */
  public Node<T> getNode(String label, Node<T> defaultNode)
    {
      for (Node<T> node : this.nodes)
        {
          if (node.getLabel().equals(label))
            {
              return node;
            }
        }
      return defaultNode;
    }

  public void fromString(String string)
  {
      /** Check that the string is valid **/
      /** TODO **/

      /** Clear current state of the tree **/
      this.root = null;
      this.leaves.clear();
      this.internals.clear();
      for (Node<T> node : this.nodes) {
          if (node.getParent() != null) {
              node.makeNotParent(node.getParent());
          }
          if (node.getChildren().size() > 0) {
              for (Node<T> child : node.getChildren()) {
                  node.makeNotChild(child);
              }
          }
          if (node.getLabel().equals("")) {
              nodes.remove(node); /** Delete unlabelled nodes **/
          }
      }

      String rootString = null, label = null, distance = null;

      /** Extract information about the root node **/
      rootString = string.substring(string.lastIndexOf(")") + 1,
                                            string.length() - 1);
      this.root = parseNodeString(rootString);

      fromStringRecursive(string.substring(0, string.lastIndexOf(")")),
                          this.root);
  }

  public void
  fromStringRecursive(String string, Node<T> current)
  {
      /** Termination case **/
      if (string.equals("") || string.equals("(")) { /** TODO **/
          return;
      }

      int index; /** Index of the current delimiter **/
      char delimiter; /** [ ',', ')', '(' ] **/

      Node<T> node;
      String label, distance;

      index = string.lastIndexOf('(');
      delimiter = '(';

      if (index < string.lastIndexOf(',')) {
          index = string.lastIndexOf(',');
          delimiter = ',';
      }

      if (index < string.lastIndexOf(')')) {
          index = string.lastIndexOf(')');
          delimiter = ')';
      }

      switch (delimiter) {
          case '(':
              if (index != string.length() - 1) {
                  node = parseNodeString(string.substring(++index));
                  current.makeChild(node);
                  current = current.getParent();
              } else {
                  current = current.getParent();
              }
              break;
          case ',':
              if (index != string.length() - 1) {
                  node = parseNodeString(string.substring(++index));
                  current.makeChild(node);
              } else {
                  index++;
              }
              break;
          case ')':
              if (index != string.length() - 1) {
                  node = parseNodeString(string.substring(++index));
                  current.makeChild(node);
                  current = node;
              } else {
                  node = new Node<T>(); /** Internal node **/
                  current.makeChild(node);
                  internals.add(node);
                  current = node;
                  index++;
              }
              break;
      }
      fromStringRecursive(string.substring(0, index - 1), current);
  }

  private Node<T>
  parseNodeString(String string)
  {
      String label = "", distance = "";

      if (string.contains(":")) {
          label = string.split(":", 2)[0];
          distance = string.split(":", 2)[1];
      } else {
          label = string;
      }

      if (label.equals("")) {
          Node<T> internal = new Node<T>();
          internals.add(internal);
          return internal;
      } else {
          for (Node<T> node : this.nodes) {
              if (node.getLabel().equals(label)) {
                  if (!distance.equals("")) {
                      node.setDistance(Integer.parseInt(distance));
                  }
                  return node;
              }
          }
      }
      return null; /** TODO **/
  }

  /**
   * Arbitrarily add a root to the tree displacing the old root.
   * <p>
   * This method makes a new root which has the old root as it's left child
   * and the old roots rightmost child as its right child.
   */
  public void root()
    {
      Node<T> newRoot = new Node<T>();
      newRoot.makeChild(root);
      newRoot.makeChild(root.getChildren().get(root.getChildren().size() - 1));
      root = newRoot;
      return;
    }

  /**
   * Arbitrarily remove a root from the tree, displacing the root.
   * <p>
   * This method makes the left child of the current root the root of the tree
   * and makes the current roots right child the new root's child.
   */
  public void unroot()
    {
      Node<T> oldRoot = root.getChildren().get(0);
      oldRoot.makeChild(root.getChildren().get(1));
      root = oldRoot;
      return;
    }
}
