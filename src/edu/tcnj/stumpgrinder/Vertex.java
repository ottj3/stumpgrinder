package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;

/**************************************************************************
 * A vertex in a graph.
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 **************************************************************************/
public class
Vertex<T> 
{
    /** A unique label identifying this vertex **/
    private String
    label;

    /** Some data associated with this vertex **/
    private T
    data;

    /** A list containing vertices adjacent to this vertex **/
    private ArrayList<Vertex<T>>
    adjacent;

    /**************************************************************************
     * Constructs a vertex with no label, no data, and no adjacent vertices.
     **************************************************************************/
    public
    Vertex()
    {
        this.label    = "";
        this.data     = null;
        this.adjacent = new ArrayList<Vertex<T>>();
    }

    /**************************************************************************
     * Constructs a vertex with a label, but no data and no adjacent vertices.
     * @param label The unique label to be used to identify this vertex.
     **************************************************************************/
    public
    Vertex(String label)
    {
        this.label    = label;
        this.data     = null;
        this.adjacent = new ArrayList<Vertex<T>>();
    }

    /**************************************************************************
     * Constructs a vertex with a label and data, but no adjacent vertices.
     * @param label The unique label to be used to identify this vertex.
     * @param data  The data to be associated with this vertex.
     **************************************************************************/
    public
    Vertex(String label, T data)
    {
        this.label    = label;
        this.data     = data;
        this.adjacent = new ArrayList<Vertex<T>>();
    }

    /**************************************************************************
     * Constructs a vertex with a label, and adjacent vertices, but no data.
     * @param label    The unique label to be used to identify this vertex.
     * @param adjacent A list of vertices to be made adjacent to this vertex.
     **************************************************************************/
    public
    Vertex(String label, Collection<? extends Vertex<T>> adjacent)
    {
        this.label    = label;
        this.data     = null;
        this.adjacent = new ArrayList<Vertex<T>>();
        for (Vertex<T> vertex : adjacent) {
            this.makeAdjacent(vertex);
        }

    }

    /**************************************************************************
     * Constructs a vertex with a label, data, and adjacent vertices.
     * @param label    The unique label to be used to identify this vertex.
     * @param data     The data to be associated with this vertex.
     * @param adjacent A list of vertices to be made adjacent to this vertex.
     **************************************************************************/
    public
    Vertex(String label, T data, Collection<? extends Vertex<T>> adjacent)
    {
        this.label    = label;
        this.data     = data;
        this.adjacent = new ArrayList<Vertex<T>>();
        for (Vertex<T> vertex : adjacent) {
            this.makeAdjacent(vertex);
        }
    }

    /**************************************************************************
     * Returns the label of this vertex.
     * @return The label of this vertex.
     **************************************************************************/
    public String
    getLabel()
    {
        return label;
    }

    /**************************************************************************
     * Returns the data of this vertex.
     * @return The data of this vertex.
     **************************************************************************/
    public T
    getData()
    {
        return data;
    }

    /**************************************************************************
     * Returns the list of vertices adjacent to this vertex.
     * @return The list of vertices adjacent to this vertex.
     **************************************************************************/
    public ArrayList<Vertex<T>>
    getAdjacent()
    {
        return adjacent;
    }

    /**************************************************************************
     * Replaces the label of this vertex.
     * @param label The new label for this vertex.
     **************************************************************************/
    public void
    setLabel(String label)
    {
        this.label = label;
    }

    /**************************************************************************
     * Replaces the data of this vertex.
     * @param data The new data for this vertex.
     **************************************************************************/
    public void
    setData(T data)
    {
        this.data = data;
    }

    /**************************************************************************
     * Replaces the list of vertice adjacent to this vertex.
     * @param adjacent The new list of vertices adjacent to this vertex.
     **************************************************************************/
    public void
    setAdjacent(Collection<? extends Vertex<T>> adjacent)
    {
        for (Vertex<T> vertex : this.adjacent) {
            this.makeNotAdjacent(vertex);
        }

        this.adjacent = new ArrayList<Vertex<T>>();

        for (Vertex<T> vertex : adjacent) {
            this.makeAdjacent(vertex);
        }
    }

    public String
    toString()
    {
        ArrayList<String> adjacentLabels = new ArrayList<String>();
        for (Vertex<T> vertex : adjacent) {
            adjacentLabels.add(vertex.getLabel());
        }

        return "Label: " + this.label
               + " Data: " + this.data
               + " Adjacent: " + adjacentLabels;
    }

    /**************************************************************************
     * Checks if a vertex is adjacent to this vertex.
     * @param vertex The vertex to search the adjacency list for.
     * @return true if vertex is adjacent to this vertex.
     **************************************************************************/
    public boolean
    isAdjacent(Vertex<T> vertex)
    {
        return adjacent.contains(vertex) && vertex.getAdjacent().contains(this);
    }

    /**************************************************************************
     * Makes a vertex adjacent to this vertex
     * @param vertex The vertex to make adjacent to this vertex.
     * @return true if the vertex is not already adjacent to this vertex.
     **************************************************************************/
    public boolean
    makeAdjacent(Vertex<T> vertex)
    {
        if (isAdjacent(vertex)) {
            return false;
        } else {
            adjacent.add(vertex);
            vertex.adjacent.add(this);
            return true;
        }
    }

    /**************************************************************************
     * Makes a vertex not adjacent to this vertex
     * @param vertex The vertex to make not adjacent to this vertex.
     * @return true if the vertex is already adjacent to this vertex.
     **************************************************************************/
    public boolean
    makeNotAdjacent(Vertex<T> vertex)
    {
        if (isAdjacent(vertex)) {
            adjacent.remove(vertex);
            vertex.adjacent.remove(this);
            return true;
        } else {
            return false;
        }
    }
}
