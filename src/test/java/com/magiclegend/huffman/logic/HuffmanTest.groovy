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
    }

    void testEncode() {
    }

    void testDecode() {
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
}
