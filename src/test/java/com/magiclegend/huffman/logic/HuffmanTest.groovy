package test.java.com.magiclegend.huffman.logic

import main.com.magiclegend.huffman.logic.Huffman
import main.com.magiclegend.huffman.logic.Node

class HuffmanTest extends GroovyTestCase {
    static String STRING = "Just lemme freak"

    void testDetermineWeight() {
        assertEquals(getCharacters(), Huffman.determineWeight(STRING))
    }

    void testGenerateLeaves() {
        assertEquals(getQueue().poll().getChararacter(), Huffman.generateLeaves(getCharacters()).poll().getChararacter())
        assertEquals(getQueue().poll().getChararacter(), Huffman.generateLeaves(getCharacters()).poll().getChararacter())
    }

    void testBuildTree() {
        Node root = Huffman.buildTree(getQueue())
        assertEquals('J', root.getRight().getRight().getRight().getChararacter())
        assertEquals('m', root.getRight().getRight().getLeft().getChararacter())
        assertEquals('e', root.getLeft().getLeft().getLeft().getChararacter())
    }

    void testGenerateCode() {
        HashMap<Character, String> test = Huffman.generateCode(getRoot())
        assertEquals(true, test.containsKey('e' as Character))
        assertEquals("000", test.get('e' as Character))
        assertEquals("0110", test.get('r' as Character))
        assertEquals("001", test.get(' ' as Character))
    }

    void testEncode() {
        assertEquals("11110111001100000110100001101100000010100011000001110101", Huffman.encode(getKeys(), STRING))
    }

    void testDecode() {
        assertEquals("Just lemme freak", Huffman.decode(getKeys(), "11110111001100000110100001101100000010100011000001110101"))
    }

    static private HashMap<Character, Long> getCharacters() {
        HashMap<Character, Long> characters = new HashMap<>()
        characters.put(' ' as Character, 2L)
        characters.put('a' as Character, 1L)
        characters.put('r' as Character, 1L)
        characters.put('s' as Character, 1L)
        characters.put('t' as Character, 1L)
        characters.put('u' as Character, 1L)
        characters.put('e' as Character, 3L)
        characters.put('f' as Character, 1L)
        characters.put('J' as Character, 1L)
        characters.put('k' as Character, 1L)
        characters.put('l' as Character, 1L)
        characters.put('m' as Character, 2L)
        return characters
    }

    static private PriorityQueue<Node> getQueue() {
        PriorityQueue<Node> queue = new PriorityQueue<>()

        HashMap<Character, Long> characters = getCharacters()

        for (Map.Entry<Character, Long> entry : characters.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()))
        }

        return queue
    }

    private static Node getRoot() {
        PriorityQueue<Node> tree = getQueue()
        while (tree.size() > 1) {
            tree.add(new Node(tree.poll(), tree.poll()))
        }

        return tree.peek()
    }

    private static HashMap<Character, String> getKeys() {
        HashMap<Character, String> keys = new HashMap<>()
        keys.put(' ' as Character, "001")
        keys.put('a' as Character, "0111")
        keys.put('r' as Character, "0110")
        keys.put('s' as Character, "1001")
        keys.put('t' as Character, "1000")
        keys.put('u' as Character, "1011")
        keys.put('e' as Character, "000")
        keys.put('f' as Character, "0100")
        keys.put('J' as Character, "111")
        keys.put('k' as Character, "0101")
        keys.put('l' as Character, "1010")
        keys.put('m' as Character, "110")
        return keys
    }
}
