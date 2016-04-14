package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;

public class
Graph<T>
{
    /** A list of the vertices in the graph **/
    private 
    ArrayList<Vertex<T>> vertices;

    public
    Graph()
    {
        this.vertices = new ArrayList<Vertex<T>>();
    }

    public
    Graph(Collection<Vertex<T>> vertices)
    {
        this.vertices = new ArrayList<Vertex<T>>();
        for (Vertex<T> vertex : vertices) {
            vertices.add(vertex);
        }
    }

    public boolean
    inGraph(Vertex<T> vertex)
    {
        return vertices.contains(vertex);
    }

    public boolean
    addVertex(Vertex<T> vertex)
    {
        if (inGraph(vertex)) {
            return false;
        } else {
            vertices.add(vertex);
            return true;
        }
    }

    public boolean
    removeVertex(Vertex<T> vertex)
    {
        if (inGraph(vertex)) {
            vertices.remove(vertex);
            return true;
        } else {
            return false;
        }
    }
}
