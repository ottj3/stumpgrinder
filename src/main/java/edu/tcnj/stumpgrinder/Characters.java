package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**************************************************************************
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 **************************************************************************/
public class Characters<E>
{
  /**
   * The root set of character states 
   */
  private List<Set<E>> rootSet;

  /**
   * The upper set of character states 
   */
  private List<Set<E>> upperSet;

  /**
   * The lower set of character states 
   */
  private List<Set<E>> lowerSet;

  /** The length of all three sets of character states **/
  private int length;

  public Characters()
    {
      this.rootSet  = new ArrayList<Set<E>>();
      this.upperSet = new ArrayList<Set<E>>();
      this.lowerSet = new ArrayList<Set<E>>();
      this.length   = 10;
    }

  public Characters(int initialCapacity)
    {
      this.rootSet  = new ArrayList<Set<E>>(initialCapacity);
      this.upperSet = new ArrayList<Set<E>>(initialCapacity);
      this.lowerSet = new ArrayList<Set<E>>(initialCapacity);
      this.length   = initialCapacity;
    }

  /**************************************************************************
   **************************************************************************/
  public List<Set<E>> getRootSet()
    {
      return rootSet;
  }

  public List<Set<E>> getUpperSet()
    {
      return upperSet;
    }

  public List<Set<E>> getLowerSet()
    {
      return lowerSet;
    }

  public void setRootSet(List<Set<E>> rootSet)
    {
      this.rootSet = rootSet;
    }

  public void setUpperSet(List<Set<E>> upperSet)
    {
      this.upperSet = upperSet;
    }

  public void setLowerSet(List<Set<E>> lowerSet)
    {
      this.lowerSet = lowerSet;
    }

  public Set<E> getFromRootSet(int index)
    {
      return rootSet.get(index);
    }

  public Set<E> getFromUpperSet(int index)
    {
      return upperSet.get(index);
    }

  public Set<E> getFromLowerSet(int index)
    {
      return upperSet.get(index);
    }

  public void setToRootSet(int index, E element)
    {
      Set<E> set = new HashSet<E>();
      set.add(element);

      rootSet.set(index, set);
    }

  public void setToRootSet(int index, Set<E> set)
    {
      rootSet.set(index, set);
    }

  public void setToUpperSet(int index, E element)
    {
      Set<E> set = new HashSet<E>();
      set.add(element);

      upperSet.set(index, set);
    }

  public void setToUpperSet(int index, Set<E> set)
    {
      upperSet.set(index, set);
    }

  public void setToLowerSet(int index, E element)
    {
      Set<E> set = new HashSet<E>();
      set.add(element);

      lowerSet.set(index, set);
    }

  public void setToLowerSet(int index, Set<E> set)
    {
      lowerSet.set(index, set);
    }
  
  public void addToRootSet(int index, E element)
    {
      Set<E> set = new HashSet<E>();
      set.add(element);
      
      this.rootSet.add(set);
    }

  public void addToRootSet(int index, Set<E> set)
    {
      this.upperSet.add(set);
    }

  public void addToUpperSet(int index, E element)
    {
      Set<E> set = new HashSet<E>();
      set.add(element);
      
      this.upperSet.add(set);
    }

  public void addToUpperSet(int index, Set<E> set)
    {
      this.upperSet.add(set);
    }

  public void addToLowerSet(int index, E element)
    {
      Set<E> set = new HashSet<E>();
      set.add(element);
      
      this.lowerSet.add(set);
    }

  public void addToLowerSet(int index, Set<E> set)
    {
      this.lowerSet.add(set);
    }

  public int size()
    {
      return this.length;
    }

  public String toString()
    {
      return "Root:  " + this.rootSet.toString() + 
             "Upper: " + this.upperSet.toString() +
             "Lower: " + this.lowerSet.toString();
    }
}
