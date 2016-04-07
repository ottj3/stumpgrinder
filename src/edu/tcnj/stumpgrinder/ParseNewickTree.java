package edu.tcnj.stumpgrinder;

/**************************************************************************
 * This class converts a string representation of a tree into
 * a tree structure by creating nodes and establishing relationships between them. 
 * 
 * It returns the root node of the tree,
 * which contains all children in an array list.
 * 
 * @param treeString A string of the form: "((A:1,G:1):1,T:1):1"
 * 
 **************************************************************************/
public class ParseNewickTree {

	/*Initializes the recursion*/
    public static Node parse(String treeString) {
    	 /*Find the root node and make it the parent*/
    	 int last = treeString.lastIndexOf(':');
    	 
         return parseRecursive(treeString, new Node(treeString.substring(last+1)), 0, last);
     }

    /*Recursively build the tree*/
     public static Node parseRecursive(String s, Node parent, int start, int end) {
          if (s.charAt(start) != '(') {
               parent.setLabel(s.substring(start, end));
               return parent;
          }

          int brackets = 0; //counts parenthesis
          int colon= 0; // marks the position of the colon
          int marker = start; // marks the position of string

          for (int i = start; i < end; i++) {
               char c = s.charAt(i);

               if (c == '(')
                    brackets++;
               else if (c == ')')
                    brackets--;
               else if (c == ':')
                    colon = i;

               if (brackets == 0 || brackets == 1 && c == ',') {
                    parent.makeChild(parseRecursive(s, new Node(s.substring(colon+1, i)), marker + 1, colon));
                    marker = i;
               }
          }

          return parent;
     }
}
