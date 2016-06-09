package edu.tcnj.stumpgrinder.algo;

import edu.tcnj.stumpgrinder.data.CharacterList;
import edu.tcnj.stumpgrinder.data.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class EnumeratorTest {
    public ArrayList<String> labels = new ArrayList<>();
    public ArrayList<String> data = new ArrayList<>();
    public CharacterList<Character> worldSet = new CharacterList<>();
    public List<Node<Character>> species = new ArrayList<>();

    public void getData(int dataSize) {
        List<String> lines = new ArrayList<>();
        lines.add("A:GAGGACCCCAGATATTACGCGGGTCGAACA");
        lines.add("B:GAAGATCCCAGATACTTTGCCGGAGAACAA");
        lines.add("C:GAGGATCCGCGTTACTTTAGCGGTATTCAA");
        lines.add("D:GAGGACCCCCGTTACTTTGCCGGCGAGGCC");
        lines.add("E:GAGGATCCCAGATATTTTGCGGGTGAGGCT");
        lines.add("F:GAAGACCCGCGCTACTTTGCCGGCACCGGC");
        lines.add("G:GAAGATCCACGTTTCTTCGCAGGAGAAGCA");
//      lines.add("G:GAAGATCC{AG}CGTTTCTTCGCAGGAGAA");
        lines.add("H:GAAGATCCACGCTACTATGCAGGACCTCAA");
        lines.add("I:GAAGACCCTCGCTATTACGCCGGTCCGCAA");
        lines.add("J:GAGGACCCACGATATTACGCGGGAGAAGGA");
        lines.add("K:GAGGATCCGCGCTACTTTGCCGGCCCGCAG");
        labels = new ArrayList<>();
        data = new ArrayList<>();
        worldSet = new CharacterList<>();
        species = new ArrayList<>();

        /* Processes the data. */
        for (int i = 0; i < dataSize; i++) {
            String line = lines.get(i);
            if (line != null && line.length() > 0) {
                labels.add(line.split(":", 2)[0]);
                data.add(line.split(":", 2)[1]);
            }
        }
        makeNodes();
    }


    public void makeNodes() {
        Node.chars = data.get(0).length();
        for (int index = 0; index < data.get(0).length(); index++) {
            worldSet.add(new HashSet<Character>());
        }

        for (int index = 0; index < labels.size(); index++) {
            Node<Character> node = new Node<>(labels.get(index));
            node.labelled = true;
            CharacterList<Character> characters = new CharacterList<>();
            //System.out.print(labels.get(index) + ": ");
            for (int index_ = 0; index_ < data.get(index).length(); index_++) {
                //System.out.println(data.get(index).length());
                characters.add(new HashSet<Character>());
                characters.get(index_).add(data.get(index).charAt(index_));
                while (index_ >= worldSet.size()) {
                    worldSet.add(new HashSet<Character>());
                }
                worldSet.get(index_).add(data.get(index).charAt(index_));
                //System.out.print(data.get(index).charAt(index_));
            }
            node.root = characters;
            // System.out.print(" root.size(): " + node.root.size());

            // Node<List<SetList<Character>>> node = new Node<List<SetList<Character>>>(
            // 		labels.get(index), sets);
            species.add(node);
            //System.out.println();
        }
    }
}
