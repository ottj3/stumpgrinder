package edu.tcnj.stumpgrinder;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Test;

public class VertexTest
{
    @Test
    /**************************************************************************
     * Tests the correctness of all of the getters, setters, and constructors.
     **************************************************************************/
    public void constructorTest()
    {
        Vertex<String> a = new Vertex<String>("A");

        assertEquals("Fail: A's label should be 'A'",
                     a.getLabel(), "A");
        assertEquals("Fail: A's data should be null",
                     a.getData(), null);
        assertTrue("Fail: A shouldn't have adjacent vertices",
                   a.getAdjacent().isEmpty());

        Vertex<String> b = new Vertex<String>("B", "B's data");

        assertEquals("Fail: B's label should be 'B'",
                     b.getLabel(), "B");
        assertEquals("Fail: B's data should be 'B's data'",
                     b.getData(), "B's data");
        assertTrue("Fail: B shouldn't have adjacent vertices",
                   b.getAdjacent().isEmpty());

        ArrayList<Vertex<String>> list = new ArrayList<Vertex<String>>();
        list.add(a); list.add(b);

        Vertex<String> c = new Vertex<String>("C", list);

        assertEquals("Fail: C's label should be 'C'",
                     c.getLabel(), "C");
        assertEquals("Fail: C's data should be null",
                     c.getData(), null);
        assertEquals("Fail: C's adjacency list should contain A and B",
                     c.getAdjacent().toString(), "[" + a.toString() + ", " + b.toString() + "]");

        Vertex<String> d = new Vertex<String>("D", "D's data", list);

        assertEquals("Fail: D's label should be 'D'",
                     d.getLabel(), "D");
        assertEquals("Fail: D's data should be 'D's data'",
                     d.getData(), "D's data");
        assertEquals("Fail: D's adjacency list should contain A and B",
                     d.getAdjacent().toString(), "[" + a.toString() + ", " + b.toString() + "]");

        Vertex<String> e = new Vertex<String>();

        assertEquals("Fail: E's label should be ''",
                     e.getLabel(), "");
        assertEquals("Fail: E's data should be null",
                     e.getData(), null);
        assertTrue("Fail: E shouldn't have adjacent vertices",
                   e.getAdjacent().isEmpty());
    }
    
    @Test
    /**************************************************************************
     * Tests the correctness of the isAdjacent() function.
     **************************************************************************/
    public void isAdjacentTest()
    {
        Vertex<String> a = new Vertex<String>("A");
        Vertex<String> b = new Vertex<String>("B");

        assertFalse("Fail: A should not be adjacent to itself",
                    a.isAdjacent(b));
        assertFalse("Fail: A should not be adjacent to B", 
                    a.isAdjacent(b));
    }

    @Test
    /**************************************************************************
     * Tests the correctness of the makeAdjacent() function.
     **************************************************************************/
    public void makeAdjacentTest()
    {
        Vertex<String> a = new Vertex<String>("A");
        Vertex<String> b = new Vertex<String>("B");

        assertTrue("Fail: A should not already be adjacent to B",
                   a.makeAdjacent(b));
        /** Also tests isAdjacent for cases where it should return true **/
        assertTrue("Fail: A should be adjacent to B after makeAdjacent",
                   a.isAdjacent(b));
    }

    @Test
    /**************************************************************************
     * Tests the correctness of the makeNotAdjacent() function.
     **************************************************************************/
    public void makeNotAdjacentTest()
    {
        Vertex<String> a = new Vertex<String>("A");
        Vertex<String> b = new Vertex<String>("B");

        assertTrue("Fail: A should not already be adjacent to B",
                   a.makeAdjacent(b));
        assertTrue("Fail: A should be adjacent to B after makeAdjacent",
                   a.makeNotAdjacent(b));
        assertFalse("Fail: A shouldn't be adjacent to B after makeNotAdjacent",
                    a.isAdjacent(b));
    }
}
