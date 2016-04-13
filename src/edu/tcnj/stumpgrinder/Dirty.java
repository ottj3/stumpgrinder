package edu.tcnj.stumpgrinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class Dirty
{
    static ArrayList<String> labels = new ArrayList<String>();
    static ArrayList<String> data   = new ArrayList<String>();

    static SetList<Character> worldSet = new SetList<Character>();

    static ArrayList<Node<List<SetList<Character>>>> species = 
        new ArrayList<Node<List<SetList<Character>>>>();

    public static void main(String[] args) throws IOException
    {
        getInput();
        makeNodes();
        //testFitch();
        testEnumeration();
        //testTreefromString();
    }

    public static void getInput() throws IOException
    {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        String line;

        do {
            line = stdin.readLine();
            if (line != null && line.length() > 0) {
                labels.add(line.split(":", 2)[0]);
                data.add(line.split(":", 2)[1]);
            }
        } while (line != null && line.length() > 0);
    }

    public static void makeNodes()
    {
        for (int index = 0; index < data.get(0).length(); index++) {
            worldSet.add(new HashSet<Character>());
        }

        for (int index = 0; index < labels.size(); index++) {
            List<SetList<Character>> sets = 
                new ArrayList<SetList<Character>>();
            sets.add(new SetList<Character>(data.get(0).length()));

            for (int index_ = 0; index_ < data.get(index).length(); index_++) {
                sets.get(0).set(index_, data.get(index).charAt(index_));
                worldSet.get(index_).add(data.get(index).charAt(index_));
            }

            Node<List<SetList<Character>>> node = 
                new Node<List<SetList<Character>>>(labels.get(index),
                                                        sets);
            species.add(node);
        }
    }

    public static void testFitch()
    {
        for (int index = 1; index < species.size(); index++) {
            System.out.println("---");
            List<SetList<Character>> sets = new ArrayList<SetList<Character>>();
            sets.add(species.get(index - 1).getData().get(0));
            sets.add(species.get(index).getData().get(0));
            System.out.println(Fitch.fitch(sets).fst());
            System.out.println(Hartigan.hartigan(sets, worldSet).fst());

            String one = Fitch.fitch(sets).snd().get(0).toString();
            String two = Hartigan.hartigan(sets, worldSet).snd().get(0).toString();
            SetList<Character> three = Hartigan.hartigan(sets, worldSet).snd().get(1);
            
            if (one.compareTo(two) == 0) {
                System.out.println("Matching VH sets");
                for (int index_ = 0; index_ < three.size(); index_++) {
                    if (!three.get(index_).isEmpty()) {
                        System.out.println((index_ + 1) + ":" + three.get(index_) +
                                            ":" + worldSet.get(index_));
                    }
                }
            } else {
                System.out.println("FITCH: ");
                System.out.println(one);
                System.out.println("HARTIGAN: ");
                System.out.println(two);
            }
        }
    }

    public static void testTrees()
    {
        Node<Integer> a = new Node<Integer>("A");
        Node<Integer> b = new Node<Integer>("B");
        Node<Integer> c = new Node<Integer>("C");
        Node<Integer> d = new Node<Integer>("D");
        Node<Integer> e = new Node<Integer>("E");

        a.makeChild(b);
        a.makeChild(c);

        b.makeChild(d);
        b.makeChild(e);

        ArrayList<Node<Integer>> list = new ArrayList<Node<Integer>>();
        list.add(a); list.add(b); list.add(c); list.add(d); list.add(e);

        Tree<Integer> tree = new Tree<Integer>(list, list.get(0));
        System.out.println(tree);
    }

    
    public static void testEnumeration()
    {
        Tree<List<SetList<Character>>> tree = 
            new Tree<List<SetList<Character>>>(species);

        HashSet<String> trees = TreeEnumeration.enumerate(tree);
        System.out.println(trees.size());

        trees = TreeEnumeration.fitchScoredEnumerate(tree);
        System.out.println(trees.size());
        for (String s : trees) {
            System.out.println(s);
        }

        trees = TreeEnumeration.hartiganScoredEnumerate(tree, worldSet);
        System.out.println(trees.size());
        for (String s : trees) {
            System.out.println(s);
        }
    }
    
    public static void testTreefromString()
    {
    	Tree<SetList<Character>> tree = new Tree<SetList<Character>>(species.subList(1, species.size()), species.get(0));
    	
    	System.out.println(tree);
        
        System.out.println(new Tree(tree.fromString("(E:1,(D:1,B:1):1,C:1)A:0").getChildren(), tree.fromString("(E:1,(D:1,B:1):1,C:1)A:0")));
    }    
}
