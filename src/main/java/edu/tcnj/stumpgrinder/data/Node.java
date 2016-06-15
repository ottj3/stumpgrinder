package edu.tcnj.stumpgrinder.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node implements Cloneable {

    /**
     * The number of characters in each species. This should always be kept the same
     * when operating on a single set of input data. All the algorithms assume that
     * all input species have the same number of Characters, and bad things will
     * happen if they do not.
     */
    public static int chars;

    /**
     * A "name" string for the node, used purely for humans to identify nodes.
     */
    public String label;

    //public DNABase[] data = new DNABase[chars];
    public CharacterList<DNABase> data = sets();
    /**
     * The cost of the node.
     */
    public List<double[]> costs;

    public List<Set<DNABase>[]> parentFits;
    /**
     * Whether this node has a known label. Sometimes useful when running algorithms
     * on nodes that have root sets, but may not be known labelled nodes.
     */
    public boolean labelled;

    /**
     * The parent of this node, or null if this node has no parent.
     */
    public Node parent;
    /**
     * A list of this node's children. This should never be null, although may be empty for leaves.
     */
    public List<Node> children = new ArrayList<>();

    /**
     * Construct a node with the given name.
     *
     * @param label a name for the node, or an empty string for an unlabelled node
     */
    public Node(String label) {
        this.costs = initializeCosts();
        initializeFits();
        this.label = label;
        this.labelled = !label.isEmpty();

    }

    /**
     * Construct a node with the given name and cost.
     *
     * @param label a name for the node, or an empty string for an unlabelled node
     */
    public Node(String label, String data) {
        this(label);
        this.setData(data);
    }

    public void setData(String data) {
        initializeCosts();
        for (int i = 0; i < Node.chars; i++) {
            for (DNABase dnaBase : DNABase.values()) {
                if (DNABase.valueOf(data.substring(i, i + 1)) == dnaBase) {
                    costs.get(i)[dnaBase.value] = 0;
                    this.data.get(i).add(dnaBase);
                } else {
                    costs.get(i)[dnaBase.value] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    public static List<double[]> initializeCosts() {
        List<double[]> costs = new ArrayList<>(chars);
        for (int i = 0; i < chars; i++) {
            costs.add(new double[DNABase.values().length]);
            for (int j = 0; j < DNABase.values().length; j++) {
                costs.get(i)[j] = 0;
            }
        }
        return costs;
    }

    public void initializeFits() {
        parentFits = new ArrayList<>(chars);
        for (int i = 0; i < chars; i++) {
            parentFits.add(new HashSet[DNABase.values().length]);
            //TODO: is this initialization necessary?
            for (int j = 0; j < parentFits.get(i).length; j++) {
                parentFits.get(i)[j] = new HashSet<>();
            }
        }
    }


    /**
     * Utility method to generate a list of sets, one set for each Character in a species.
     * <p>Callers should be responsible for ensuring that {@link #chars} is set before
     * calling this method.</p>
     *
     * @return an empty but initialized {@link CharacterList} for a node with sets for each character
     */
    public static <S> CharacterList<S> sets() {
        List<Set<S>> sets = new ArrayList<>(chars);
        for (int i = chars; i-- > 0; ) {
            sets.add(new HashSet<S>());
        }
        return new CharacterList<>(sets);
    }

    /**
     * Creates a parent-child relationship between two nodes in both directions.
     *
     * @param parent the node to be the parent
     * @param child  the node to be the child
     */
    public static <S> void linkNodes(Node parent, Node child) {
        parent.children.add(child);
        child.parent = parent;
    }

    /**
     * Removes a parent-child relationship between two nodes in both directions.
     *
     * @param parent the original parent node
     * @param child  the child node
     */
    public static <S> void unlinkNodes(Node parent, Node child) {
        parent.children.remove(child);
        if (parent == child.parent) {
            child.parent = null;
        }
    }

    /**
     * Clones a Node and all sub-tree nodes as new Objects, but maintains references to
     * the original costs to preserve space.
     * <p>This method preserves the entire structure of a tree (or subtree) in the new Node
     * objects by recursively cloning each child node and re-linking the new objects.</p>
     *
     * @return a copy of a node or tree structure with references to original data sets
     */
    @Override
    public Node clone() {
        Node newNode = new Node(this.label);
        for (Node child : children) {
            Node newChild = child.clone();
            newChild.parent = newNode;
            newNode.children.add(newChild);
        }

        //System.arraycopy(this.data, 0, newNode.data, 0, this.data.length);
        newNode.data = new CharacterList<>(this.data);

        newNode.costs = this.costs;
        return newNode;
    }

    /**
     * Utility method to calculate the size of a sub-tree, including the given node.
     * <p>The size is defined to be the total number of nodes in the tree.</p>
     *
     * @param node the root of the tree or subtree to find the size of
     * @return the number of nodes in the tree
     */
    public static int size(Node node) {
        int size = 1;
        for (Node child : node.children) {
            size += size(child);
        }
        return size;
    }

    /**
     * Calculates the {@link #size(Node)} of the current node object.
     *
     * @return size of the current node's subtree, including itself
     */
    public int size() {
        return size(this);
    }

}
