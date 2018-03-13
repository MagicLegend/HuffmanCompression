package logic;

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

        for (int i = 0; i < string.length(); i++) {
            if (characters.containsKey(string.charAt(i))) {
                Long l = characters.get(string.charAt(i));
                l++;
                characters.put(string.charAt(i), l);
            } else {
                characters.put(string.charAt(i), 1L);
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

        characters.forEach((key, value) -> leaves.add(new Node(key, value)));

        return leaves;
    }

    /**
     * Takes the generated leaves, and iterates over them and combines to build the rest of the tree until one node remains.
     *
     * @param tree The bottom leaves with their weight.
     */
    public static Node buildTree(PriorityQueue<Node> tree) {
        while (tree.size() > 1) {
            tree.add(new Node(tree.poll(), tree.poll()));
        }

        return tree.peek();
    }

    private static HashMap<Character, String> hits;

    /**
     * Generates the bits based on the given root node.
     *
     * @param node The root Node object which will contain the entire tree.
     * @return HashMap with the character and the corresponding code.
     */
    public static HashMap<Character, String> generateCode(Node node) {
        hits = new HashMap<>();
        checkNodes(node, "");
        return hits;
    }

    /**
     * Internal recursion function to allow for pushing back to a hashMap
     *
     * @param node   The root Node object which will contain the entire tree.
     * @param string The code that is generated.
     */
    private static void checkNodes(Node node, String string) {
        if (node != null) {
            if (node.right != null) {
                checkNodes(node.right, string + "1");
            }

            if (node.left != null) {
                checkNodes(node.left, string + "0");
            }

            if (node.right == null && node.left == null) {
                //Done generating code
                if (node.chararacter.length() == 1) {
                    System.out.println("Char: " + node.chararacter + " | Code: " + string);
                    hits.put(node.chararacter.charAt(0), string);
                } else {
                    System.out.println("Character (" + node.chararacter + ") was longer than 1; something went wrong!");
                    throw new IllegalArgumentException("Character (" + node.chararacter + ") was longer than 1; something went wrong!");
                }
            }
        }
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
        for (int i = 0; i < string.length(); i++) {
            sb.append(keys.get(string.charAt(i)));
        }
        System.out.println("Code: " + sb.toString());
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

        for (Map.Entry<Character, String> entry : keys.entrySet()) {
            //Reset the current node to the root to start fresh again
            currNode = root;

            //For each bit in the entry
            for (int i = 0; i <= entry.getValue().length(); i++) {
                //If they are equal, all the bits have been processed to nodes, and the final leaf should get the character
                if (i == entry.getValue().length()) {
                    //At the end of the code; the character should be inserted here
                    currNode.chararacter = entry.getKey().toString();
                } else {
                    if (entry.getValue().charAt(i) == '1') {
                        //Should step right
                        if (currNode.right == null) {
                            currNode.right = new Node();
                        }
                        currNode = currNode.getRight();

                    } else if (entry.getValue().charAt(i) == '0') {
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
