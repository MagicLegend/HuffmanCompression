package main.com.magiclegend.huffman.logic;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {

    private Huffman() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Determines how often the letters occur in the string.
     *
     * @param string The string to consider.
     */
    public static HashMap<Character, Long> determineWeight(String string) {
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

        return characters;
    }

    /**
     * Takes the HashMap with <K,V> structure <Character, Count> and iterates over it. For each iteration a new Node is added to the queue.
     * The queue has a custom comparator, which will check if the weight of the nodes are above or below each other.
     * TODO: Check if a case for equal nodes is needed.
     *
     * @param characters The characters with their counts that should be interpreted.
     */
    public static PriorityQueue<Node> generateLeaves(Map<Character, Long> characters) {
        //Comparison from: https://gist.github.com/ahmedengu/aa8d85b12fccf0d08e895807edee7603
        PriorityQueue<Node> leaves = new PriorityQueue<>((o1, o2) -> (o1.weight < o2.weight) ? -1 : 1);

        //          O(n)                         O(n)
        characters.forEach((key, value) -> leaves.add(new Node(key, value)));

        return leaves;
    }

    /**
     * Takes the generated leaves, and iterates over them and combines to build the rest of the tree until one node remains.
     *
     * @param tree The bottom leaves with their weight.
     */
    public static Node buildTree(PriorityQueue<Node> tree) {
        while (tree.size() > 1) {                               //O(n) ????
            tree.add(new Node(tree.poll(), tree.poll()));       //O(n)
        }

        return tree.peek();
    }

    //BitSet ipv string
    //CheckNodes returnt de hashmap
    private static HashMap<Character, String> hits;

    /**
     * Generates the bits based on the given root node.
     *
     * @param node The root Node object which will contain the entire tree.
     * @return HashMap with the character and the corresponding code.
     */
    public static HashMap<Character, String> generateCode(Node node, boolean log) {
        hits = new HashMap<>();
        checkNodes(node, "", log);
        return hits;
    }

    /**
     * Internal recursion function to allow for pushing back to a hashMap
     *
     * @param node   The root Node object which will contain the entire tree.
     * @param string The code that is generated.
     */
    private static void checkNodes(Node node, String string, boolean log) {
        if (node != null) {
            if (node.right != null) {
                checkNodes(node.right, string + "1", log);
            }

            if (node.left != null) {
                checkNodes(node.left, string + "0", log);
            }

            if (node.right == null && node.left == null) {
                //Done generating code
                if (node.chararacter.length() == 1) {
                    if (log) {
                        System.out.println("Char: " + node.chararacter + " | Code: " + string);
                    }
                    hits.put(node.chararacter.charAt(0), string);
                } else {
                    System.out.println("Character (" + node.chararacter + ") was longer than 1; something went wrong!");
                    throw new IllegalArgumentException("Character (" + node.chararacter + ") was longer than 1; something went wrong!");
                }
            }
        }
    }

    public static void generateCodeV2(Node node) {
        //0100 0000 0000 0011 1111 1011 //max value (4195323)
        //https://math.stackexchange.com/a/664856 (amount of nodes = 4n - 1; where n = amount of leaves)
        //This means that the first three bytes of the bitSet should be reserved for counting. This would mean that it would support a tree with 16.777.215 nodes, but that is the point that with the current Unicode will cannot be reached. There are not enough characters (only 1.048.831)
    }

    /**
     * Updated version to integrate the variables. Checks if a node is a tree node, and recursivly runs the function again on that node.
     * Will add the code of the character to the bitSet until the leaf node is found. Then it will add it to the hashmap that is passed along.
     * It will return the HashMap back to the upper levels, and it will go down the tree again.
     *
     * @param node The root node on the initial call, after that it's the tree node until the leaf is reached.
     * @param hits The HashMap with the Character and it's corresponding code in a BitSet.
     * @param bitSet The code of the current character that's being built. Cleared when a leaf has been reached.
     * @param counter The counter to keep track of what location the bit should be flipped. Reset to 0 when a leaf has been reached.
     * @return
     */
    public static HashMap<Character, BitSet> checkNodesv2(Node node, HashMap<Character, BitSet> hits, BitSet bitSet, int counter) {
        if (node != null) {
            if (node.right != null) {
                bitSet.set(counter);
                counter++;
                int temp = counter;
                checkNodesv2(node.right, hits, bitSet, counter);
                counter = temp;
            }

            if (node.left != null) {
                counter++;
                checkNodesv2(node.left, hits, bitSet, counter);
            }

            if (node.right == null && node.left == null) {
                if (node.chararacter.length() == 1) {
                    System.out.println("Char: " + node.chararacter + " | Code: " + bitSet.toString());
                    hits.put(node.chararacter.charAt(0), bitSet);
                    bitSet.clear();
                    counter = 0;
                    return hits;
                } else {
                    System.out.println("Character (" + node.chararacter + ") was longer than 1; something went wrong!");
                    throw new IllegalArgumentException("Character (" + node.chararacter + ") was longer than 1; something went wrong!");
                }
            }
        }
        return hits;
    }

    /**
     * Encodes the given string with the keys.
     *
     * @param keys   The keys which which the string should be encoded.
     * @param string The string that should be encoded.
     * @return The encoded string.
     */
    public static String encode(Map<Character, String> keys, String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {     //O(n)
            sb.append(keys.get(string.charAt(i)));      //O(n)
        }
        //System.out.println("Code: " + sb.toString());
        return sb.toString();
    }

    /**
     * Decodes the given string with the keys.
     *
     * @param keys          The keys with which the string can be decoded.
     * @param encodedString The encoded string that should be decoded.
     * @return The decoded string.
     */
    public static String decode(Map<Character, String> keys, String encodedString) {
        StringBuilder sb = new StringBuilder();
        Node root = rebuildTree(keys);
        Node currNode = root;

        for (int i = 0; i < encodedString.length(); i++) {
            Character currChar = encodedString.charAt(i);
            if (currChar == '1') {
                currNode = currNode.getRight();
            } else if (currChar == '0') {
                currNode = currNode.getLeft();
            }

            if (currNode.chararacter != null) {
                sb.append(currNode.chararacter);
                currNode = root;
            }
        }
        return sb.toString();
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
                    currNode.chararacter = entry.getKey().toString();
                } else {
                    if (entry.getValue().charAt(i) == '1') {                //O(n)
                        //Should step right
                        if (currNode.right == null) {
                            currNode.right = new Node();
                        }
                        currNode = currNode.getRight();

                    } else if (entry.getValue().charAt(i) == '0') {         //O(n)
                        //Should step left
                        if (currNode.left == null) {
                            currNode.left = new Node();
                        }
                        currNode = currNode.getLeft();
                    }
                }
            }
        }

        return root;
    }
}
