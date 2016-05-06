package edu.tcnj.stumpgrinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class
Dirty
{
    /** A list used to store the labels from the input data **/
    static List<String>
    labels = new ArrayList<String>();

    /** A list used to store the characters from the input data **/
    static List<String>
    data   = new ArrayList<String>();

    /** A list used to store the world set of the input data **/
    static Characters<Character>
    worldSet;

    static List<Node<Characters<Character>>>
    species = new ArrayList<Node<Characters<Character>>>();

    public static void
    main(String[] args) throws IOException
    {
        getInput();
        makeNodes();
        enumerateTrees();
    }

    public static void
    getInput() throws IOException
    {
        BufferedReader stdin =
            new BufferedReader(new InputStreamReader(System.in));

        String line;

        do {
            line = stdin.readLine();
            if (line != null && line.length() > 0) {
                labels.add(line.split(":", 2)[0]);
                data.add(line.split(":", 2)[1]);
            }
        } while (line != null && line.length() > 0);
    }

    public static void
    makeNodes()
    {
        worldSet = new Characters<Character>(data.get(0).length());

        for (int i = 0; i < labels.size(); i++) {
            Characters<Character> sets = 
                new Characters<Character>(data.get(0).length());

            for (int j = 0; j < data.get(i).length(); j++) {
                sets.addToRootSet(j, data.get(i).charAt(j));
                sets.addToUpperSet(j, data.get(i).charAt(j));
                worldSet.addToRootSet(j, data.get(i).charAt(j));
            }

            Node<Characters<Character>> node = 
                new Node<Characters<Character>>(labels.get(i), sets);

            species.add(node);
        }
    }

    public static void
    enumerateTrees()
    {
        Tree<Characters<Character>> tree = 
            new Tree<Characters<Character>>(species);

        Set<String> trees = TreeEnumeration.enumerate("hartigan", tree, worldSet);

        System.out.println(trees.size());
        for (String s : trees) {
            System.out.println(s);
            tree.fromString(s);
            System.out.println(Hartigan.bottomUp(tree, worldSet));
            for (Node<Characters<Character>> node : tree.getInternals()) {
                System.out.println(node.getData().getRootSet());
            }
            Hartigan.topDown(tree);
            System.out.println("AFTER");
            for (Node<Characters<Character>> node : tree.getInternals()) {
                System.out.println(node.getData().getRootSet());
            }
        }
    }
}
