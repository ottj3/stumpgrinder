package edu.tcnj.stumpgrinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set; 
/**
 * The driver of the stumpgrinder system.
 * <p>
 * This is the class that contains the main method and handles I/O formatting.
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 */
public class Main
{
  /**
   * A list used to store the labels from the input data
   */
  static List<String> labels = new ArrayList<String>();

  /**
   * A list used to store the characters from the input data 
   */
  static List<String> data = new ArrayList<String>();

  /**
   * A list used to store the world set of the input data
   */
  static Characters<Character> worldSet;

  static List<Node<Characters<Character>>> species =
      new ArrayList<Node<Characters<Character>>>();

  public static void main(String[] args) throws IOException
    {
      getInput();
      makeNodes();
      enumerateTrees();
    }

  public static void getInput() throws IOException
    {
      BufferedReader stdin =
          new BufferedReader(new InputStreamReader(System.in));

      String line;

      do
        {
          line = stdin.readLine();
          if (line != null && line.length() > 0)
            {
              labels.add(line.split(":", 2)[0]);
              data.add(line.split(":", 2)[1]);
            }
        }
      while (line != null && line.length() > 0);
    }

  public static void makeNodes()
    {
      worldSet = new Characters<Character>(data.get(0).length());

      // TODO
      List<Set<Character>> worldSetRootSet =
        new ArrayList<Set<Character>>(data.get(0).length());

      // TODO
      for (int i = 0; i < data.get(0).length(); i++)
        {
          worldSetRootSet.add(new HashSet<Character>());
        }

      for (int i = 0; i < labels.size(); i++)
        {
          Characters<Character> sets = 
              new Characters<Character>(data.get(0).length());

          for (int j = 0; j < data.get(i).length(); j++)
            {
              sets.addToRootSet(j, data.get(i).charAt(j));
              sets.addToUpperSet(j, data.get(i).charAt(j));
              // TODO
              worldSetRootSet.get(j).add(data.get(i).charAt(j));
            }

          Node<Characters<Character>> node = 
              new Node<Characters<Character>>(labels.get(i), sets);

          // TODO
          worldSet.setRootSet(worldSetRootSet);
          species.add(node);
        }
    }

  public static void enumerateTrees()
    {
      Tree<Characters<Character>> tree = 
          new Tree<Characters<Character>>(species);

      Set<String> trees = TreeEnumeration.fitchEnumerate(tree);
      System.out.println(trees.size());

      System.out.println(worldSet);
      for (String s : trees)
        {
          if (s.equals("((5,2),1,(4,3));"))
          {
            tree.fromString(s);
            System.out.print(Hartigan.bottomUp(tree, worldSet) + ":");
            System.out.println(s);
            for (Node<Characters<Character>> node : tree.getInternals())
              {
                System.out.print(node.getData().getRootSet());
                System.out.print(" - ");
                System.out.print(node.getData().getUpperSet());
                System.out.print(" - ");
                System.out.println(node.getData().getLowerSet());
              }
            Hartigan.topDown(tree);
            System.out.println("AFTER");
            for (Node<Characters<Character>> node : tree.getInternals())
              {
                System.out.print(node.getData().getRootSet());
                System.out.print(" - ");
                System.out.print(node.getData().getUpperSet());
                System.out.print(" - ");
                System.out.println(node.getData().getLowerSet());
              }
          }
        }
    }
}
