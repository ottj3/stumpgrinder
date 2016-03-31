package edu.tcnj.stumpgrinder;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Test;

public class NodeTest
{
    @Test
    /**************************************************************************
     * Tests the correctness of all of the getters, setters, and constructors.
     **************************************************************************/
    public void constructorTest()
    {
        Node<String> a = new Node<String>("A");

        assertEquals("Fail: A's label should be 'A'",
                     a.getLabel(), "A");
        assertEquals("Fail: A's data should be null",
                     a.getData(), null);
        assertTrue("Fail: A shouldn't have adjacent vertices",
                   a.getAdjacent().isEmpty());

        Node<String> b = new Node<String>("B", "B's data");

        assertEquals("Fail: B's label should be 'B'",
                     b.getLabel(), "B");
        assertEquals("Fail: B's data should be 'B's data'",
                     b.getData(), "B's data");
        assertTrue("Fail: B shouldn't have adjacent vertices",
                   b.getAdjacent().isEmpty());

        ArrayList<Node<String>> list = new ArrayList<Node<String>>();
        list.add(a); list.add(b);

        Node<String> c = new Node<String>("C", list);

        assertEquals("Fail: C's label should be 'C'",
                     c.getLabel(), "C");
        assertEquals("Fail: C's data should be null",
                     c.getData(), null);
        assertEquals("Fail: C's adjacency list should contain A and B",
                     c.getAdjacent().toString(), "[" + a.toString() + ", " + b.toString() + "]");

        Node<String> d = new Node<String>("D", "D's data", list);

        assertEquals("Fail: D's label should be 'D'",
                     d.getLabel(), "D");
        assertEquals("Fail: D's data should be 'D's data'",
                     d.getData(), "D's data");
        assertEquals("Fail: D's adjacency list should contain A and B",
                     d.getAdjacent().toString(), "[" + a.toString() + ", " + b.toString() + "]");

        Node<String> e = new Node<String>();

        assertEquals("Fail: E's label should be ''",
                     e.getLabel(), "");
        assertEquals("Fail: E's data should be null",
                     e.getData(), null);
        assertTrue("Fail: E shouldn't have adjacent vertices",
                   e.getAdjacent().isEmpty());
    }
}
