import asg.cliche.Command;
import asg.cliche.ShellFactory;
import logic.Huffman;
import logic.IO;
import logic.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {
    static String string;
    static HashMap<Character, Long> characters;
    static PriorityQueue<Node> leaves;
    static Node root;
    static HashMap<Character, String> hits;
    static String encoded;

    /**
     * For lib usage: https://code.google.com/archive/p/cliche/wikis/Manual.wiki
     * UTF-8 table for testing: http://www.utf8-chartable.de/unicode-utf8-table.pl
     */
    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("Shell@Hackerboyy", "", new Main())
                .commandLoop();
    }

    @Command
    public void addString(String s) {
        string = s;
        System.out.println("Added " + s);
    }

    @Command
    public void testBuildTree() {
        System.out.println("Using String " + string);
        characters = Huffman.determineWeight(string);
        System.out.println("Done calculating weights...");
        leaves = Huffman.generateLeaves(characters);
        System.out.println("Done building leaves...");
        root = Huffman.buildTree(leaves);
        System.out.println("Done building tree...");
        hits = Huffman.generateCode(root);
        System.out.println("Done generating codes...");
        encoded = Huffman.encode(hits, string);
        System.out.println("Done encoding...");
    }

    @Command
    public void readLoremFromFile(String s) {
        string = IO.readLoremFromFile(s);
    }

    @Command
    public void testDecode() {
        if (hits != null && encoded != null) {
            System.out.println(Huffman.decode(hits, encoded));
        } else {
            System.out.println("Oops! Missing arguments!");
        }
    }

    @Command
    public void testWriteToFile(String file) {
        if (hits != null && encoded != null) {
            try {
                IO.writeHuffmanToFile(file, hits, encoded);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Oops! Missing arguments!");
        }
    }

    @Command
    public void testBitsWriteToFile(String file) {
        if (hits != null && encoded != null) {
            IO.writeHuffmanAsBitsToFile(file, hits, encoded);
        }
    }

    @Command
    public void testReadFromFile(String file) {
        try {
            HashMap<Character, String> keys = IO.readHuffmanFromFile(file);
            if (keys == null) {
                throw new NullPointerException();
            }

            encoded = keys.get('\uE088'); //http://www.utf8-chartable.de/unicode-utf8-table.pl | U+E000 ... U+F8FF: Private Use Area
            keys.remove('\uE088');

            hits = keys;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command
    public void testBitsReadFromFile(String file) {
        HashMap<Character, String> keys = IO.readHuffmanAsBitsFromFile(file);
        if (keys == null) {
            throw new NullPointerException();
        }

        encoded = keys.get('\uE088'); //http://www.utf8-chartable.de/unicode-utf8-table.pl | U+E000 ... U+F8FF: Private Use Area
        keys.remove('\uE088');

        hits = keys;
    }
}
