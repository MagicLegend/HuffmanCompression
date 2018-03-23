package main.com.magiclegend.huffman.logic.depricated;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;

@SuppressWarnings("Duplicates")
public class IO {
    //private static final String PATH = "C:/output/";
    private static final String PATH = "output/";

    private IO() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Writes the given keys and encoded string to a bin file.
     *
     * @param keys    The keys that should be saved.
     * @param encoded The corresponding string that should be saved.
     * @throws IOException
     */
    public static void writeHuffmanToFile(String file, HashMap<Character, String> keys, String encoded) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH + file))) {
            oos.writeObject(keys);
            oos.writeObject(encoded);
        }
    }

    /**
     * https://stackoverflow.com/a/39684467/7193940
     *
     * @param keys    The keys that should be saved.
     * @param encoded The corresponding string that should be saved.
     */
    public static void writeHuffmanAsBitsToFile(String file, HashMap<Character, String> keys, String encoded) {
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
            oos.writeObject(keys);
            oos.writeObject(bitSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Character, String> readHuffmanAsBitsFromFile(String file) {
        String encoded = "";
        BitSet bitSet = null;
        HashMap<Character, String> keys = null;
        StringBuilder sb = new StringBuilder();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH + file))) {
            keys = (HashMap<Character, String>) ois.readObject();
            bitSet = (BitSet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (bitSet != null) {
            System.out.println("Size: " + bitSet.size());
            System.out.println("Length: " + bitSet.length());
            for(int i = 0; i < bitSet.length() - 1; i++) { //Because the length() returns the length + 1; and the final bit needs to be ignored a -1 in combination with the < (instead of <=) is needed.
                if(bitSet.get(i)) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
        }

        encoded = sb.toString();

        if (keys != null) {
            System.out.println("Encoded: " + encoded);
            keys.put('\uE088', encoded); //http://www.utf8-chartable.de/unicode-utf8-table.pl | U+E000 ... U+F8FF: Private Use Area
        }

        return keys;
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
    public static HashMap<Character, String> readHuffmanFromFile(String file) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH + file))) {
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

    /**
     * Reads the text file in the output folder.
     *
     * @return A load of lorem ipsum.
     */
    public static String readLoremFromFile(String file) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(PATH + file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
