package logic;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {

    private Huffman() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if the string is in the ASCII table.
     *
     * @param string The string to be checked.
     * @return If all the characters in the string are in the ASCII table.
     */
    public static Boolean isASCII(String string) {
        //Highest possible UTF-8 character: 洖 | 55422 //http://www.utf8-chartable.de/unicode-utf8-table.pl
        //1048831
//        for (int i = 0; i < string.length(); i++) {
//            if (string.charAt(i) > 1048830) {
//                return false;
//            }
//        }

        //<charAt(), count>

        return true;
    }

    /**
     * Determines how often the letters occur in the string.
     *
     * @param string The string to consider.
     */
    public static HashMap<Character, Long> determineWeight(String string) {
        HashMap<Character, Long> characters = new HashMap<>();

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
     * Takes the hashmap with <K,V> structure <Character, Count> and iterates over it. For each iteration a new Node is added to the queue.
     * The queue has a custom comparator, which will check if the weight of the nodes are above or below each other.
     * TODO: Check if a case for equal nodes is needed.
     *
     * @param characters The characters with their counts that should be interpreted.
     */
    public static PriorityQueue<Node> generateLeaves(HashMap<Character, Long> characters) {
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
     * @param node The root Node object which will contain the entire tree.
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
     * @param keys The keys which which the string should be encoded.
     * @param string The string that should be encoded.
     * @return The encoded string.
     */
    public static String encode(HashMap<Character, String> keys, String string) {

    }
}
