package test.java.com.magiclegend.huffman.logic

import main.com.magiclegend.huffman.logic.depricated.Huffman
import main.com.magiclegend.huffman.logic.depricated.IO
import main.com.magiclegend.huffman.logic.Node

class HuffmanPerformanceTest extends GroovyTestCase {
    static String FILE = "1MLorem.txt"
    static int TIMES = 1000 //Amount of times the extreme tests should be run

    void testDetermineWeight() {
        long curr = System.nanoTime()
        Huffman.determineWeight(IO.readLoremFromFile(FILE))
        println "DetermineWeight: " + ((System.nanoTime() - curr) / 1000000) + "ms"
    }

    void testGenerateLeaves() {
        HashMap<Character, Long> characters = Huffman.determineWeight(IO.readLoremFromFile(FILE))
        long curr = System.nanoTime()
        PriorityQueue<Node> leaves = Huffman.generateLeaves(characters)
        println "GenerateLeaves: " + ((System.nanoTime() - curr) / 1000000) + "ms"
    }

    void testBuildTree() {
        PriorityQueue<Node> leaves = Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE)))
        long curr = System.nanoTime()
        Node root = Huffman.buildTree(leaves)
        println "BuildTree: " + ((System.nanoTime() - curr) / 1000000) + "ms"
    }

    void testGenerateCode() {
        Node root = Huffman.buildTree(Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE))))
        long curr = System.nanoTime()
        HashMap<Character, String> keys = Huffman.generateCode(root, false)
        println "GenerateCode: " + ((System.nanoTime() - curr) / 1000000) + "ms"
    }

    void testEncode() {
        HashMap<Character, String> keys = Huffman.generateCode(Huffman.buildTree(Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE)))), false)
        String lorem = IO.readLoremFromFile(FILE)
        long curr = System.nanoTime()
        String encoded = Huffman.encode(keys, lorem)
        println "Encode: " + ((System.nanoTime() - curr) / 1000000) + "ms"
    }

    void testDecode() {
        HashMap<Character, String> keys = Huffman.generateCode(Huffman.buildTree(Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE)))), false)
        String encoded = Huffman.encode(keys, IO.readLoremFromFile(FILE))
        long curr = System.nanoTime()
        String decoded = Huffman.decode(keys, encoded)
        println "Decode: " + ((System.nanoTime() - curr) / 1000000) + "ms"
    }

    //------------------------------------------------------

    void testDetermineWeightExtreme() {
        long timing = 0
        for (int i = 0; i < TIMES; i++) {
            long curr = System.nanoTime()
            Huffman.determineWeight(IO.readLoremFromFile(FILE))
            long n = System.nanoTime() - curr
            timing += n
        }

        println "DetermineWeightExtreme: " + (timing / 1000000) + "ms"
    }

    void testGenerateLeavesExtreme() {
        long timing = 0
        HashMap<Character, Long> characters = Huffman.determineWeight(IO.readLoremFromFile(FILE))
        for (int i = 0; i < TIMES; i++) {
            long curr = System.nanoTime()
            PriorityQueue<Node> leaves = Huffman.generateLeaves(characters)
            long n = System.nanoTime() - curr
            timing += n
        }

        println "GenerateLeavesExtreme: " + (timing / 1000000) + "ms"
    }

    void testBuildTreeExtreme() {
        long timing = 0
        PriorityQueue<Node> leaves = Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE)))
        for (int i = 0; i < TIMES; i++) {
            long curr = System.nanoTime()
            Node root = Huffman.buildTree(leaves)
            long n = System.nanoTime() - curr
            timing += n
        }
        println "BuildTreeExtreme: " + (timing / 1000000) + "ms"
    }

    void testGenerateCodeExtreme() {
        long timing = 0
        Node root = Huffman.buildTree(Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE))))
        for (int i = 0; i < TIMES; i++) {
            long curr = System.nanoTime()
            HashMap<Character, String> keys = Huffman.generateCode(root, false)
            long n = System.nanoTime() - curr
            timing += n
        }
        println "GenerateCodeExtreme: " + (timing / 1000000) + "ms"
    }

    void testEncodeExtreme() {
        long timing = 0
        HashMap<Character, String> keys = Huffman.generateCode(Huffman.buildTree(Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE)))), false)
        String lorem = IO.readLoremFromFile(FILE)
        for (int i = 0; i < TIMES; i++) {
            long curr = System.nanoTime()
            String encoded = Huffman.encode(keys, lorem)
            long n = System.nanoTime() - curr
            timing += n
        }
        println "EncodeExtreme: " + (timing / 1000000) + "ms"
    }

    void testDecodeExtreme() {
        long timing = 0
        HashMap<Character, String> keys = Huffman.generateCode(Huffman.buildTree(Huffman.generateLeaves(Huffman.determineWeight(IO.readLoremFromFile(FILE)))), false)
        String encoded = Huffman.encode(keys, IO.readLoremFromFile(FILE))
        for (int i = 0; i < TIMES; i++) {
            long curr = System.nanoTime()
            String decoded = Huffman.decode(keys, encoded)
            long n = System.nanoTime() - curr
            timing += n
        }
        println "DecodeExtreme: " + (timing / 1000000) + "ms"
    }
}
