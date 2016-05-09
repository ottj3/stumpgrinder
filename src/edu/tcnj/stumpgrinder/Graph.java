package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * An undirected graph
 *
 * @author Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 * @since   0.1
 */
public class
Graph<T>
{
  /** Variables **/


  /**
   * A list of the vertices in the graph.
   */ 
  private  List<Vertex<T>> vertices;


  /** Constructors **/


  /**
   * Construct a graph with no vertices.
   */
  public Graph()
    {
      this.vertices = new ArrayList<Vertex<T>>();
    }

  /**
   * Constructs a graph with vertices taken from a collection.
   * 
   * @param vertices A collection of vertices to add to the graph.
   */
  public Graph(Collection<Vertex<T>> vertices)
    {
      this.vertices = new ArrayList<Vertex<T>>();
      for (Vertex<T> vertex : vertices)
        {
          vertices.add(vertex);
        }
    }


  /** Instance Methods **/


  /**
   * Determines if a vertex is in the graph.
   *
   * @param vertex The vertex to check the graph for.
   * @return true if the vertex is in the list of vertices, otherwise false.
   */
  public boolean inGraph(Vertex<T> vertex)
    {
      return vertices.contains(vertex);
    }

  /**
   * Adds a vertex to the graph.
   *
   * @param vertex The vertex to add to the graph.
   * @return true if the vertex is not in the graph, otherwise false.
   */
  public boolean addVertex(Vertex<T> vertex)
    {
      if (inGraph(vertex))
        {
          return false;
        }
      else
        {
          vertices.add(vertex);
          return true;
        }
    }

  /**
   * Removes a vertex from the graph.
   *
   * @param vertex The vertex to be removed from the graph.
   * @return true if the vertex is in the graph, otherwise false.
   */
  public boolean removeVertex(Vertex<T> vertex)
    {
      if (inGraph(vertex))
        {
          vertices.remove(vertex);
          return true;
        }
      else
        {
          return false;
        }
    }
}
