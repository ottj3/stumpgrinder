package edu.tcnj.stumpgrinder.data;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class CharacterList<S> extends CopyOnWriteArrayList<Set<S>> {
    public CharacterList() {
    }

    public CharacterList(List<Set<S>> sets) {
        super(sets);
    }
}
