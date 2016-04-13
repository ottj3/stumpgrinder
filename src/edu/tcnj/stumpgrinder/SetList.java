package edu.tcnj.stumpgrinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**************************************************************************
 * A list of sets.
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 **************************************************************************/
public class 
SetList<E>
{
    /** The underlying list storing all the sets **/
    private
    ArrayList<HashSet<E>> list;

    /**************************************************************************
     * Constructs an empty list with an initial capacity of ten.
     **************************************************************************/
    public 
    SetList()
    {
        this.list = new ArrayList<HashSet<E>>();
    }

    /**************************************************************************
     * Constructs a list containing the elements of the specified collection, 
     * in the order they are returned by the collection iterator.
     * @param c The collection whose elements are to be placed into this list.
     **************************************************************************/
    public
    SetList(Collection<? extends E> c)
    {
        this.list = new ArrayList<HashSet<E>>(c.size());

        int index = 0;
        for (E element : c) {
            this.list.get(index++).add(element);
        }
    }

    public 
    SetList(SetList<E> c)
    {
        this.list = new ArrayList<HashSet<E>>(c.list);
    }

    /**************************************************************************
     * Constructs an empty list with the specified initial capacity.
     * @param initialCapacity The initial capacity of the list.
     **************************************************************************/
    public
    SetList(int initialCapacity)
    {
        this.list = new ArrayList<HashSet<E>>(initialCapacity);

        for (int index = 0; index < initialCapacity; index++) {
            list.add(index, new HashSet<E>());
        }
    }

    public void
    add(HashSet<E> e)
    {
        this.list.add(e);
    }

    public void
    add(E e)
    {
        HashSet<E> s = new HashSet<E>();
        s.add(e);
        this.list.add(s);
    }

    public void
    add(int index, E e)
    {
        HashSet<E> s = new HashSet<E>();
        s.add(e);
        this.list.add(index, s);
    }

    public boolean
    addAll(SetList<E> c)
    {
        return this.list.addAll(c.list);
    }

    public boolean
    containsAll(SetList<E> c)
    {
        return this.list.containsAll(c.list);
    }

    public HashSet<E>
    get(int index)
    {
        return list.get(index);
    }

    public boolean
    isEmpty()
    {
        return list.isEmpty();
    }

    public boolean
    retainAll(SetList<E> c)
    {
        return this.list.retainAll(c.list);
    }

    public void
    set(int index, E element)
    {
        HashSet<E> set = new HashSet<E>();
        set.add(element);
        this.list.set(index, set);
    }

    public void
    set(int index, HashSet<E> element)
    {
        this.list.set(index, element);
    }

    public int
    size()
    {
        return this.list.size();
    }

    public String
    toString()
    {
        return this.list.toString();
    }
}
