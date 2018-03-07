package logic;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {
    private static final String PATH = "output/";

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
    public static String encode(HashMap<Character, String> keys, String string) {
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
    public static String decode(HashMap<Character, String> keys, String encodedString) {
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
    private static Node rebuildTree(HashMap<Character, String> keys) {
        Node root = new Node();
        Node currNode;

        for (Map.Entry<Character, String> entry : keys.entrySet()) {
            currNode = root;
            for (int i = 0; i <= entry.getValue().length(); i++) {
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

    /**
     * Writes the given keys and encoded string to a bin file.
     *
     * @param keys    The keys that should be saved.
     * @param encoded The corresponding string that should be saved.
     * @throws IOException
     */
    public static void writeToFile(HashMap<Character, String> keys, String encoded) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH + "encoded.bin"))) {
            oos.writeObject(keys);
            oos.writeObject(encoded);
        }
    }

    /**
     * Reads the given bin file.
     *
     * @return HashMap with the characters and their codes. Also includes a character ('\uE088') rom the Private Use Area as defined by the Unicode standard (http://www.unicode.org/faq/private_use.html)
     * This character is used to transport the encoded string down to the caller. The caller should take the pair from the HashMap and remove it before continuing working with it.
     * Technically there should be no issue, as long as the text doesn't contain that exact character (which is an exception in itself), but to prevent any unforeseen issues it is advisable to do.
     * The character doesn't seem to be used publicly anywhere now: http://www.alanwood.net/unicode/private_use_area.html
     * @throws IOException
     */
    public static HashMap<Character, String> readFromFile() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH + "encoded.bin"))) {
            HashMap<Character, String> keys = (HashMap<Character, String>) ois.readObject();
            String encoded = (String) ois.readObject();
            System.out.println("Encoded: " + encoded);
            keys.put('\uE088', encoded); //http://www.utf8-chartable.de/unicode-utf8-table.pl | U+E000 ... U+F8FF: Private Use Area
            return keys;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
