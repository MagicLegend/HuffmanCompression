import asg.cliche.Command;
import asg.cliche.ShellFactory;
import logic.Huffman;
import logic.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Main {
    static String string;
    static HashMap<Character, Long> characters;
    static PriorityQueue<Node> leaves;
    static Node root;
    static HashMap<Character, String> hits;

    /**
     * For lib usage: https://code.google.com/archive/p/cliche/wikis/Manual.wiki
     *
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
    public void testDetermineCharacters(String s) {
        characters = Huffman.determineWeight(s);
    }

    @Command
    public void testBuildTree(String s) {
        System.out.println("Using String " + s);
        characters = Huffman.determineWeight(s);
        System.out.println("Done calculating weights...");
        leaves = Huffman.generateLeaves(characters);
        System.out.println("Done building leaves...");
        root = Huffman.buildTree(leaves);
        System.out.println("Done building tree...");
        hits = Huffman.generateCode(root);
        System.out.println("Done generating codes...");

    }
}
