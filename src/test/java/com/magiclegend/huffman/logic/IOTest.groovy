package test.java.com.magiclegend.huffman.logic

import com.google.common.hash.HashCode
import com.google.common.hash.Hashing
import com.google.common.io.Files
import main.com.magiclegend.huffman.logic.IO

class IOTest extends GroovyTestCase {
    static String ENCODED = "11110111001100000110100001101100000010100011000001110101"
    static String LIPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris sit."

    void testWriteHuffmanToFile() {
        IO.writeHuffmanToFile("unitTest.bin", getKeys(), ENCODED)
        File file = new File("output/unitTest.bin")
        if (file.exists() && !file.isDirectory()) {
            HashCode hc = Files.asByteSource(file).hash(Hashing.sha256())
            assertEquals("1d9e83d39268fa82fce26d0f7aff6ddffed73e544309042573fc7a9603a6b22e", hc.toString())
            assertEquals(true, file.exists())
        } else {
            fail("File was not found or was a directory!")
        }
    }

    void testWriteHuffmanAsBitsToFile() {
        IO.writeHuffmanAsBitsToFile("unitTestBits.bin", getKeys(), ENCODED)
        File file = new File("output/unitTestBits.bin")
        if (file.exists() && !file.isDirectory()) {
            HashCode hc = Files.asByteSource(file).hash(Hashing.sha256())
            assertEquals("1989e09c076508ee0e5a2e46a7de9bd29a23cf7d87e122b06160c6c3ad1527de", hc.toString())
            assertEquals(true, file.exists())
        } else {
            fail("File was not found or was a directory!")
        }
    }

    void testReadHuffmanAsBitsFromFile() {
        HashMap<Character, String> temp = IO.readHuffmanAsBitsFromFile("unitTestBits.bin")
        assertEquals(ENCODED, temp.get('\uE088' as Character))
        temp.remove('\uE088' as Character)
        assertEquals(getKeys(), temp)
    }

    void testReadHuffmanFromFile() {
        HashMap<Character, String> temp = IO.readHuffmanFromFile("unitTest.bin")
        assertEquals(ENCODED, temp.get('\uE088' as Character))
        temp.remove('\uE088' as Character)
        assertEquals(getKeys(), temp)
    }

    void testReadLoremFromFile() {
        File file = new File("output/unitTestBits.bin")
        if (file.exists() && !file.isDirectory()) {
            String lorem = IO.readLoremFromFile("TestLorem.txt")
            assertEquals(LIPSUM, lorem)
        } else {
            fail("File was not found or was a directory!")
        }

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
