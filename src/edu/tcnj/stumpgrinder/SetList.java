package edu.tcnj.stumpgrinder;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**************************************************************************
 * A wrapper around a list, for use with sets.
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 0.1
 **************************************************************************/
public class SetList<E>
// extends AbstractList<E>
// implements List <E>
{
    /** The List the SetList wraps **/
    private List<Set<E>>
    list;

    /**************************************************************************
     * Constructs an empty list with an initial capacity of ten.
     **************************************************************************/
    public
    SetList()
    {
        this.list = new ArrayList<Set<E>>();
    }

    /**************************************************************************
     * Constructs a list containing the elements of the specified collection, 
     * in the order they are returned by the collection iterator.
     * @param c the collection whose elements are to be placed into this list.
     **************************************************************************/
    public
    SetList(Collection<? extends E> c)
    {
        this.list = new ArrayList<Set<E>>(c.size());

        for (E element : c) {
            add(element);
        }
    }

    /**************************************************************************
     * Constructs an empty list with the specified initial capacity.
     * @param initialCapacity the initial capacity of the list.
     **************************************************************************/
    public
    SetList(int initialCapacity)
    {
        this.list = new ArrayList<Set<E>>(initialCapacity);
    }

    /**************************************************************************
     * Append the specified element to the end of this list.
     * @param e element to be appended to this list
     **************************************************************************/
    public void
    add(E e)
    {
        Set<E> s = new HashSet<E>();
        s.add(e);
        this.list.add(s);

    }

    /**************************************************************************
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     **************************************************************************/
    public void
    add(int index, E element)
    {
        Set<E> set = new HashSet<E>();
        set.add(element);
        this.list.add(index, set);
    }

    /**************************************************************************
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's Iterator. The behavior of this operation is undefined if the
     * specified collection is modified while the operation is in progress.
     *
     * @param c collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     **************************************************************************/
    public boolean
    addAll(Collection<? extends E> c)
    {
        return this.list.addAll(c);
    }

    /**************************************************************************
     **************************************************************************/
    public boolean
    addAll(int index, Collection<? extends E> c)
    {
        return this.list.addAll(index, c);
    }

    /**************************************************************************
     * Removes all of the elements from this list. The list will be empty after
     * this call returns.
     **************************************************************************/
    public void
    clear()
    {
        this.list.clear();
    }

    /**************************************************************************
     **************************************************************************/
    public void
    contains(Object o)
    {
        this.list.contains(o);
    }

    /**************************************************************************
     **************************************************************************/
    public boolean
    containsAll(Collection<?> c)
    {
        this.list.containsAll(c);
    }

    /**************************************************************************
     **************************************************************************/
    public boolean
    equals(Object o)
    {
        return this.list.equals(o);
    }

    /**************************************************************************
     **************************************************************************/
    public Set<E>
    get(int index)
    {
        return list.get(index);
    }

    /**************************************************************************
     **************************************************************************/
    public int
    hashCode()
    {
        return this.list.hashCode();
    }

    /**************************************************************************
     **************************************************************************/
    public int
    indexOf(Object o)
    {
        return this.list.indexOf(o);
    }

    /**************************************************************************
     **************************************************************************/
    public int
    indexOf(Set<Object> so)
    {
        return this.list.indexOf(so);
    }

    /**************************************************************************
     * Returns true if this list contains no elements.
     * @return true if this list contains no elements
     **************************************************************************/
    public boolean
    isEmpty()
    {
        return this.list.isEmpty();
    }

    /**************************************************************************
     **************************************************************************/
    public Iterator<E>
    iterator()
    {

    }

    /**************************************************************************
     **************************************************************************/
    public int
    lastIndexOf(Object o)
    {
        return this.list.lastIndexOf(o);
    }

    /**************************************************************************
     **************************************************************************/
    public ListIterator<E>
    listIterator()
    {
        return this.list.listIterator();
    }

    /**************************************************************************
     **************************************************************************/
    public ListIterator<E>
    listIterator(int index)
    {
        return this.list.listIterator(index);
    }

    /**************************************************************************
     **************************************************************************/
    public E
    remove(int index)
    {
        return this.list.remove(index);
    }

    /**************************************************************************
     * Replaces the element at the specified position in this list with the
     * specified element.
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously stored at the specified position
     **************************************************************************/
    public Set<E>
    set(int index, E element)
    {
        HashSet<E> set = new HashSet<E>();
        set.add(element);
        return this.list.set(index, set);
    }

    /**************************************************************************
     * Returns the number of elements in this list.
     * @return the number of elements in this list
     **************************************************************************/
    public int
    size()
    {
        return this.list.size();
    }

    public List<E>
    subList(int fromIndex, int toIndex)
    {
        return new SetList<E>(this.list.subList(fromIndex, toIndex));
    }

    /**************************************************************************
     **************************************************************************/
    public String
    toString()
    {
        return this.list.toString();
    }
}
