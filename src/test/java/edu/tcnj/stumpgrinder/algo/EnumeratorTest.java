package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.Parser;
import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EnumeratorTest {
    public static List<String> testData = new ArrayList<>();
    static {
        testData.add("A:GAGGACCCCAGATATTACGCGGGTCGAACA");
        testData.add("B:GAAGATCCCAGATACTTTGCCGGAGAACAA");
        testData.add("C:GAGGATCCGCGTTACTTTAGCGGTATTCAA");
        testData.add("D:GAGGACCCCCGTTACTTTGCCGGCGAGGCC");
        testData.add("E:GAGGATCCCAGATATTTTGCGGGTGAGGCT");
        testData.add("F:GAAGACCCGCGCTACTTTGCCGGCACCGGC");
//      testData.add("G:GAAGATCC?CGTTTCTTCGCAGGAGAAGCA"); // true sequence with ? = A or G
//      testData.add("G:GAAGATCC{AG}CGTTTCTTCGCAGGAGAA"); // original sequence without last three chars
        testData.add("G:GAAGATCCCAGACGTTTCTTCGCAGGAGAA"); // Angela's replaced { with C and } with A
        testData.add("H:GAAGATCCACGCTACTATGCAGGACCTCAA");
        testData.add("I:GAAGACCCTCGCTATTACGCCGGTCCGCAA");
        testData.add("J:GAGGACCCACGATATTACGCGGGAGAAGGA");
        testData.add("K:GAGGATCCGCGCTACTTTGCCGGCCCGCAG");
        testData.add("L:GAAGACCCGCGATATTTTGCCGGAGAATCA");
        testData.add("M:GAAGATCCTCGATATTTTGCCGGTCCGCAA");
        testData.add("N:GAAGATCCTCGATATTTTGCCGGTCCGCAA");
        testData.add("O:GAAGACCCGCGTTATTTTGCCGGTACCAGC");
        testData.add("P:GAGGACCCGAGAATGTTCGCTGGCGTTGCC");
        testData.add("Q:GAGGATCCTAGGTTTTATGCGGGCGAGGGC");
        testData.add("R:GAAGACCCACGTTATTTCGCCGGCACCAGC");
        testData.add("S:GAGGACCCCAGATATTTTGCGGGTGAGGCT");
        testData.add("T:GAAGACCCGCGTTACTATGCGGGCACAGAT");
        testData.add("U:GAGGACCCGCGTTACTATGCGGGCACAGAC");
        testData.add("V:GAAGACCCGCGTTACTATGCGGGCACAGAT");
        testData.add("W:GAAGACCCGCGCTACTTTGCCGGCACCGGC");
        testData.add("X:AAGGACCCTTGTTATATTTCCGGCCCGCGT");
        testData.add("Y:GAGGACCCGCGCTACTTCGCGGGCGAAGGA");
        testData.add("Z:GAGGACCCGCGTTACTATGCGGGCACAGAT");
    }

    public CharacterList<Character> worldSet;
    public List<Node<Character>> species = new ArrayList<>();

    public void getData(int dataSize) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            lines.add(testData.get(i));
        }

        Parser parser = new Parser();
        List<Set<Character>> worldSet0 = new ArrayList<>();
        parser.speciesList(lines, species, worldSet0);
        worldSet = new CharacterList<>(worldSet0);
    }

}
