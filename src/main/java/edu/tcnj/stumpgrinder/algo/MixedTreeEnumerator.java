package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.List;
import java.util.Set;

public class MixedTreeEnumerator<S> extends TreeEnumerator<S> {
    public MixedTreeEnumerator() {
    }

    public MixedTreeEnumerator(List<Node<S>> labelledNodes, CharacterList<S> worldSet) {
        super(labelledNodes, worldSet);
    }

    public MixedTreeEnumerator(List<Node<S>> labelledNodes) {
        super(labelledNodes);
    }

    @Override
    protected void initializeTree() {
        if (labelledNodes.size() >= 1) {
            root = labelledNodes.get(0);
            if (labelledNodes.size() >= 2) {
                Node.linkNodes(root, labelledNodes.get(1));
            }
        }
    }

    public Set<Node<S>> enumerate() {
        initializeTree();
        if (labelledNodes.size() <= 2) {
            trees.add(root.clone());
        } else {
            enumerateRecursive(root, 2);
        }
        return trees;
    }

//    public int completed = 0;

    @Override
    protected void enumerateRecursive(Node<S> current, int size) {
        if (size == labelledNodes.size()) {
//            System.out.println(++completed);
//            trees.add(root.clone());
        } else {
            case1(current, size, false);
            case2(current, size, false);
            case3(current, size, false);
            case4(current, size, false);
        }
    }

    public Set<Node<S>> hartiganEnumerate() {
        initializeTree();
        if (labelledNodes.size() <= 2) {
            trees.add(root.clone());
        } else {
            hartiganEnumerateRecursive(root, 2);
        }
        return trees;
    }

    //TODO: Branch and bound
    @Override
    protected void hartiganEnumerateRecursive(Node<S> current, int size) {
        if (size == labelledNodes.size()) {
            int score = Hartigan.bottomUp(root, worldSet);
            updateMPlist(score);
        } else {
            case1(current, size, true);
            case2(current, size, true);
            case3(current, size, true);
            case4(current, size, true);
        }
    }

    /***************************************************************************
     * Case 1: Enumerates bifurcating trees.
     **************************************************************************/
    private <T> void case1(Node<S> current, int size, boolean isScored) {
        for (int index = 0; index < current.children.size(); index++) {
            case1(current.children.get(0), size, isScored);
        }
        if (current != root) {
            Node<S> internal = new Node<>("");
            Node<S> leaf = labelledNodes.get(size);
            Node<S> parent = current.parent;

            addNodeToEdge(current, parent, internal, leaf);

            if (isScored) {
                hartiganEnumerateRecursive(root, size + 1);
            } else {
                enumerateRecursive(root, size + 1);
            }

            removeNodeFromEdge(current, parent, internal, leaf);
        }
    }

    /***************************************************************************
     * Case 2: Enumerates multifurcating trees. Inserts a labelled node into any
     * edge.
     **************************************************************************/
    private <T> void case2(Node<S> current, int size, boolean isScored) {
        for (int index = 0; index < current.children.size(); index++) {
            case2(current.children.get(0), size, isScored);
        }
        if (current != root) {
            Node<S> internal = labelledNodes.get(size);
            Node<S> parent = current.parent;

            Node.unlinkNodes(parent, current);
            Node.linkNodes(parent, internal);
            Node.linkNodes(internal, current);

            if (isScored) {
                hartiganEnumerateRecursive(root, size + 1);
            } else {
                enumerateRecursive(root, size + 1);
            }

            Node.unlinkNodes(internal, current);
            Node.unlinkNodes(parent, internal);
            Node.linkNodes(parent, current);
        }
    }

    /***************************************************************************
     * Case 3: Enumerates multifurcating trees. Inserts a child into any
     * labelled or unlabelled node (including the root).
     **************************************************************************/

    private <T> void case3(Node<S> current, int size, boolean isScored) {
        for (int index = 0; index < current.children.size(); index++) {
            case3(current.children.get(index), size, isScored);
        }
        Node<S> leaf = labelledNodes.get(size);

        Node.linkNodes(current, leaf);

        if (isScored) {
            hartiganEnumerateRecursive(root, size + 1);
        } else {
            enumerateRecursive(root, size + 1);
        }

        Node.unlinkNodes(current, leaf);

    }

    /***************************************************************************
     * Enumerates multifurcating trees. Labels any unlabelled node.
     **************************************************************************/
    private <T> void case4(Node<S> current, int size, boolean isScored) {
        for (int index = 0; index < current.children.size(); index++) {
            case4(current.children.get(index), size, isScored);
        }
        if (!current.labelled) {
            Node<S> newNode = labelledNodes.get(size);

            current.label = newNode.label;
            current.labelled = true;
            current.root = newNode.root;

            if (isScored) {
                hartiganEnumerateRecursive(root, size + 1);
            } else {
                enumerateRecursive(root, size + 1);
            }

            current.root = Node.sets();
            current.labelled = false;
            current.label = "";
        }
    }
}
