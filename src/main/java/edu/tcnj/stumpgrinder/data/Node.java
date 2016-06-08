package edu.tcnj.stumpgrinder.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node<S> {

    public static int chars;
    public static <S> CharacterList<S> sets() {
        List<Set<S>> sets = new ArrayList<>(chars);
        for (int i = chars; i-- > 0;) {
            sets.add(new HashSet<S>());
        }
        return new CharacterList<>(sets);
    }

    public Node(String label) {
        this.label = label;
        this.labelled = !label.isEmpty();
    }

    public Node(String label, int cost) {
        this(label);
        this.cost = cost;
    }

    public String label;

    public int cost;

    public boolean labelled;

    public CharacterList<S> root = new CharacterList<>();
    public CharacterList<S> upper = new CharacterList<>();
    public CharacterList<S> lower = new CharacterList<>();

    public Node<S> parent;
    public List<Node<S>> children = new ArrayList<>();

}
