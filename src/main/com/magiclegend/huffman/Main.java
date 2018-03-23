package main.com.magiclegend.huffman;

import asg.cliche.Command;
import asg.cliche.ShellFactory;

import java.io.IOException;

import static main.com.magiclegend.huffman.logic.Huffman.create;

public class Main {
    /**
     * For lib usage: https://code.google.com/archive/p/cliche/wikis/Manual.wiki
     * UTF-8 table for testing: http://www.utf8-chartable.de/unicode-utf8-table.pl
     * BigO cheat sheet: https://stackoverflow.com/a/559862/7193940
     */
    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("Shell@Hackerboyy", "", new Main())
                .commandLoop();
    }

    /**
     * Encodes the given string into the given file.
     */
    @Command
    public void testEncodeTextAndWriteToFile(String text, String file) {
        String output = create().enterString(text).determineWeight().generateLeaves().buildTree().generateCode(false).encode().writeBitsToFile(file).execute();
    }

    /**
     * Reads the given (text) file and encodes it; and writes it to the second file.
     */
    @Command
    public void testReadTextEncodeAndWriteToFile(String loremFile, String file) {
        create().readLorem(loremFile).determineWeight().generateLeaves().buildTree().generateCode(false).encode().writeBitsToFile(file).execute();
    }

    /**
     * Reads the given file, and writes the decoded output to the given text file.
     */
    @Command
    public void testReadBitsFromFileToFile(String source, String destination) {
        create().readBitsFromFile(source).decode().writeTextToFile(destination).execute();
    }

    /**
     * Reads the given file, and logs the decoded output to the console.
     */
    @Command
    public void testReadBitsFromFileAndLog(String source) {
        create().readBitsFromFile(source).decode().writeTextToConsole().execute();
    }
}
