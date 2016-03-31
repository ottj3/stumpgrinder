package edu.tcnj.stumpgrinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dirty
{
    static ArrayList<String> labels = new ArrayList<String>();
    static ArrayList<String> data   = new ArrayList<String>();
    static ArrayList<Node<SetList<Character>>> species = 
        new ArrayList<Node<SetList<Character>>>();

    public static void main(String[] args) throws IOException
    {
        getInput();
        makeNodes();
        testTrees();
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
        for (int index = 0; index < labels.size(); index++) {
            SetList<Character> set = 
                new SetList<Character>(data.get(index).length());
            for (int index_ = 0; index_ < data.get(index).length(); index_++) {
                set.set(index_, data.get(index).charAt(index_));
            }

            Node<SetList<Character>> node = 
                new Node<SetList<Character>>(labels.get(index),
                                             set);
            species.add(node);
        }
    }

    public static void testFitch()
    {
        for (int index = 1; index < species.size(); index++) {
            System.out.println("---");
            ArrayList<SetList<Character>> sets = new ArrayList<SetList<Character>>();
            sets.add(species.get(index - 1).getData());
            sets.add(species.get(index).getData());
            System.out.println(PhylogeneticAlgorithms.fitch(sets).first);
            System.out.println(PhylogeneticAlgorithms.hartigan(sets).first);

            String one = PhylogeneticAlgorithms.fitch(sets).second.toString();
            String two = PhylogeneticAlgorithms.hartigan(sets).second.toString();
            
            System.out.println(one.compareTo(two));
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
}
