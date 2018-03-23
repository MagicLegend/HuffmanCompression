package main.com.magiclegend.huffman.logic;

import main.com.magiclegend.huffman.logic.interfaces.IExecute;
import main.com.magiclegend.huffman.logic.interfaces.IHuffman;
import main.com.magiclegend.huffman.logic.interfaces.intermediates.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@SuppressWarnings("Duplicates")
public class Huffman implements IHuffman, IExecute, IICharacters, IIEncoded, IIHits, IILeaves, IIRoot, IIString, IIDecoded, IIDecodedOutput {
    //private static final String PATH = "C:/output/";
    private static final String PATH = "output/";

    private String string;
    private HashMap<Character, Long> characters;
    private PriorityQueue<Node> leaves;
    private Node root;
    private HashMap<Character, String> hits;
    private HashMap<Character, BitSet> hitSet;
    private String encoded;

    private Huffman() {

    }

    public static IHuffman create() {
        return new Huffman();
    }

    /**
     * Sets the string to the given string.
     *
     * @param s The string that should be used further down the chain.
     * @return An intermediate interface.
     */
    @Override
    public IIString enterString(String s) {
        this.string = s;
        return this;
    }

    /**
     * Determines how often the letters occur in the string.
     *
     * @return An intermediate interface.
     */
    @Override
    public IICharacters determineWeight() {
        HashMap<Character, Long> characters = new HashMap<>();
        //Mergemap lambda?

        for (int i = 0; i < string.length(); i++) {             //O(n)
            if (characters.containsKey(string.charAt(i))) {     //O(1)
                Long l = characters.get(string.charAt(i));      //O(n)
                l++;
                characters.put(string.charAt(i), l);            //O(n)
            } else {
                characters.put(string.charAt(i), 1L);           //O(n)
            }
        }
        this.characters = characters;
        return this;
    }

    /**
     * Takes the HashMap with <K,V> structure <Character, Count> and iterates over it. For each iteration a new Node is added to the queue.
     * The queue has a custom comparator, which will check if the weight of the nodes are above or below each other.
     */
    @Override
    public IILeaves generateLeaves() {
        //Comparison from: https://gist.github.com/ahmedengu/aa8d85b12fccf0d08e895807edee7603
        PriorityQueue<Node> leaves = new PriorityQueue<>((o1, o2) -> (o1.getWeight() < o2.getWeight()) ? -1 : 1);

        //          O(n)                         O(n)
        characters.forEach((key, value) -> leaves.add(new Node(key, value)));
        this.leaves = leaves;
        return this;
    }

    /**
     * Takes the generated leaves, and iterates over them and combines to build the rest of the tree until one node remains.
     */
    @Override
    public IIRoot buildTree() {
        PriorityQueue<Node> tree = leaves;
        while (tree.size() > 1) {                               //O(n) ????
            tree.add(new Node(tree.poll(), tree.poll()));       //O(n)
        }
        root = tree.peek();
        return this;
    }

    /**
     * Generates the bits based on the given root node.
     */
    @Override
    public IIHits generateCode(boolean log) {
        hits = new HashMap<>();
        checkNodes(root, "", log);
        return this;
    }

    /**
     * Internal recursion function to allow for pushing back to a hashMap
     *
     * @param node   The root Node object which will contain the entire tree.
     * @param string The code that is generated.
     */
    private void checkNodes(Node node, String string, boolean log) {
        if (node != null) {
            if (node.getRight() != null) {
                checkNodes(node.getRight(), string + "1", log);
            }

            if (node.getLeft() != null) {
                checkNodes(node.getLeft(), string + "0", log);
            }

            if (node.getRight() == null && node.getLeft() == null) {
                //Done generating code
                if (node.getChararacter().length() == 1) {
                    if (log) {
                        System.out.println("Char: " + node.getChararacter() + " | Code: " + string);
                    }
                    hits.put(node.getChararacter().charAt(0), string);
                } else {
                    System.out.println("Character (" + node.getChararacter() + ") was longer than 1; something went wrong!");
                    throw new IllegalArgumentException("Character (" + node.getChararacter() + ") was longer than 1; something went wrong!");
                }
            }
        }
    }

    /**
     * Encodes the given string with the keys.
     */
    @Override
    public IIOutput encode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {     //O(n)
            sb.append(hits.get(string.charAt(i)));      //O(n)
        }
        encoded = sb.toString();
        return this;
    }

    /**
     * Decodes the given string with the keys.
     */
    @Override
    public IIDecodedOutput decode() {
        StringBuilder sb = new StringBuilder();
        Node root = rebuildTree(hits);
        Node currNode = root;

        for (int i = 0; i < encoded.length(); i++) {
            Character currChar = encoded.charAt(i);
            if (currChar == '1') {
                currNode = currNode.getRight();
            } else if (currChar == '0') {
                currNode = currNode.getLeft();
            }

            if (currNode.getChararacter() != null) {
                sb.append(currNode.getChararacter());
                currNode = root;
            }
        }
        string = sb.toString();
        return this;
    }

    /**
     * Steps through the given HashMap to rebuild the tree.
     *
     * @param keys The keys with which the tree can be rebuilt.
     * @return The rebuilt node tree.
     */
    private static Node rebuildTree(Map<Character, String> keys) {
        Node root = new Node();
        Node currNode;

        for (Map.Entry<Character, String> entry : keys.entrySet()) {        //O(n)
            //Reset the current node to the root to start fresh again
            currNode = root;

            //For each bit in the entry
            for (int i = 0; i <= entry.getValue().length(); i++) {          //O(n)
                //If they are equal, all the bits have been processed to nodes, and the final leaf should get the character
                if (i == entry.getValue().length()) {                       //O(n)
                    //At the end of the code; the character should be inserted here
                    currNode.setChararacter(entry.getKey().toString());
                } else {
                    if (entry.getValue().charAt(i) == '1') {                //O(n)
                        //Should step right
                        if (currNode.getRight() == null) {
                            currNode.setRight(new Node());
                        }
                        currNode = currNode.getRight();

                    } else if (entry.getValue().charAt(i) == '0') {         //O(n)
                        //Should step left
                        if (currNode.getLeft() == null) {
                            currNode.setLeft(new Node());
                        }
                        currNode = currNode.getLeft();
                    }
                }
            }
        }

        return root;
    }

    /**
     * Final function that executes in the chain.
     *
     * @return Either the decoded string or the encoded string.
     */
    @Override
    public String execute() {
        if (string.length() > 0) {
            return string;
        } else {
            return encoded;
        }
    }

    /**
     * https://stackoverflow.com/a/39684467/7193940
     *
     * @param file The file to where should be written.
     */
    @Override
    public IExecute writeBitsToFile(String file) {
        BitSet bitSet = new BitSet(encoded.length());

        int bitCounter = 0;
        for (Character c : encoded.toCharArray()) {
            if (c == '1') {
                bitSet.set(bitCounter);
            }

            bitCounter++;
        }
        bitSet.set(bitCounter); //Set the latest bit to true to prevent data loss in case the last bits were false.

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH + file))) {
            oos.writeObject(hits);
            oos.writeObject(bitSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Takes the hits and the encoded code and writes it to a file as objects.
     *
     * @param file The file that it should be written towards.
     */
    @Override
    public IExecute writeToFile(String file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH + file))) {
            oos.writeObject(hits);
            oos.writeObject(encoded);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return this;
    }

    /**
     * Reads the expected HashMap with keys and BitSet with bits from the given file.
     *
     * @param file The file that it should be read.
     */
    @Override
    public IHuffman readBitsFromFile(String file) {
        BitSet bitSet = null;
        StringBuilder sb = new StringBuilder();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH + file))) {
            hits = (HashMap<Character, String>) ois.readObject();
            bitSet = (BitSet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (bitSet != null) {
            System.out.println("Size: " + bitSet.size());
            System.out.println("Length: " + bitSet.length());
            for (int i = 0; i < bitSet.length() - 1; i++) { //Because the length() returns the length + 1; and the final bit needs to be ignored a -1 in combination with the < (instead of <=) is needed.
                if (bitSet.get(i)) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
        }

        encoded = sb.toString();
        return this;
    }

    /**
     * Reads the given bin file.
     *
     * @return HashMap with the characters and their codes.
     */
    @Override
    public IHuffman readFromFile(String file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH + file))) {
            hits = (HashMap<Character, String>) ois.readObject();
            encoded = (String) ois.readObject();
            System.out.println("Encoded: " + encoded);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Reads the text file in the output folder.
     *
     * @return A load of lorem ipsum.
     */
    @Override
    public IIString readLorem(String file) {
        try {
            string = new String(Files.readAllBytes(Paths.get(PATH + file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Writes the given text to a txt file. Used to output the decoded text.
     *
     * @param file The file to which the given text should be written.
     */
    @Override
    public IExecute writeTextToFile(String file) {
        try (PrintStream out = new PrintStream(new FileOutputStream(PATH + file))) {
            out.print(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Logs the current string to the console.
     */
    @Override
    public IExecute writeTextToConsole() {
        System.out.println(string);
        return this;
    }
}
