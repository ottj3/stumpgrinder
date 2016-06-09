package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Fitch {

    private static <S> int fitch(Node<S> current) {
        // if we are a leaf node, our current root set is already
        // correct, and the score is 0
        if (current.children.size() == 0) {
            return 0;
        }
        int score = 0;
        // create a list of the root sets of all our children
        List<CharacterList<S>> childRoots = new ArrayList<>();
        for (Node<S> child : current.children) {
            childRoots.add(child.root);
        }

        // initialize our new root set as a bunch of empty sets
        CharacterList<S> root = Node.sets();

        // for each character
        for (int i = 0; i < Node.chars; i++) {
            boolean first = true;
            boolean union = false;
            // get the root set for that character
            Set<S> currentStatesForChar = root.get(i);
            // for each child root set
            for (CharacterList<S> childRoot : childRoots) {
                if (first) {
                    // initialize our root set to the child's root for the first
                    currentStatesForChar.addAll(childRoot.get(i));
                    first = false;
                } else {
                    // intersect each remaining child root set with our root set
                    currentStatesForChar.retainAll(childRoot.get(i));
                    if (currentStatesForChar.isEmpty()) {
                        // if at any point the intersection becomes empty,
                        // it means our root set needs to be the union of them all
                        union = true;
                        break;
                    }
                }
            }
            if (union) {
                // this character had a change, so increase the score
                score += 1;
                // and set our root set to the union of all children's root sets
                for (CharacterList<S> childRoot : childRoots) {
                    currentStatesForChar.addAll(childRoot.get(i));
                }
            }
        }

        current.root = root;

        return score;
    }

    public static <S> int bottomUp(Node<S> root) {
        int score = 0;

        // recursive down the to bottom of the tree first
        for (Node<S> child : root.children) {
            score += bottomUp(child);
        }

        if (root.children.size() > 2) {
            System.out.println((new Parser()).toString(root, false));
            throw new IllegalArgumentException("Can only perform Fitch on cubic tree - got node of degree > 3");
        }

        score += fitch(root);

        return score;
    }

    static <S> Node<S> cubicToBinary(Node<S> root) {
        Node<S> newRoot = new Node<>("");
        Node.linkNodes(newRoot, root);
        Node.linkNodes(newRoot, root.children.get(root.children.size() - 1));
        Node.unlinkNodes(root, newRoot.children.get(1));
        return newRoot;
    }
    static <S> Node<S> binaryToCubic(Node<S> root) {
        Node<S> oldRoot = root.children.get(0);
        Node.linkNodes(oldRoot, root.children.get(1));
        Node.unlinkNodes(root, root.children.get(1));
        Node.unlinkNodes(root, oldRoot);
        return oldRoot;
    }
}
