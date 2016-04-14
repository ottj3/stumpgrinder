package edu.tcnj.stumpgrinder;

/**************************************************************************
 * A 2-tuple.
 * An immutable data type used to group data of different types.
 *
 * @author  Andrew Miller <millea18@tcnj.edu>
 * @version 1.0
 **************************************************************************/
public class
Pair <T1, T2>
{
    /** The first element of the pair **/
    private final
    T1 first;

    /** The second element of the pair **/
    private final
    T2 second;

    /**************************************************************************
     * Constructs a pair.
     * @param first  The first element of the pair.
     * @param second The second element of the pair.
     **************************************************************************/
    public
    Pair(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
    }

    /**************************************************************************
     * Returns the first element of this pair.
     * @return The first element of this pair.
     **************************************************************************/
    public T1
    fst()
    {
        return first;
    }

    /**************************************************************************
     * Returns the second element of this pair.
     * @return The second element of this pair.
     **************************************************************************/
    public T2
    snd()
    {
        return second;
    }
}
