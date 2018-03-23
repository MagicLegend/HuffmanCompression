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

    @Command
    public void testBitsReadFromFileChaining(String file) {
        String output = create().readBitsFromFile(file).decode().writeTextToConsole().execute();
    }

    @Command
    public void testTextBitsWriteToFileChaining(String text, String file) {
        String output = create().enterString(text).determineWeight().generateLeaves().buildTree().generateCode(false).encode().writeBitsToFile(file).execute();
    }

    @Command
    public void testLoremBitsWriteToFileChaining(String loremFile, String file) {
        create().readLorem(loremFile).determineWeight().generateLeaves().buildTree().generateCode(false).encode().writeBitsToFile(file).execute();
    }

    @Command
    public void testStringBitsWriteToFileChaining(String file, String content) {
        create().enterString(content).determineWeight().generateLeaves().buildTree().generateCode(false).encode().writeBitsToFile(file).execute();
    }

    @Command
    public void testReadBitsFromFileToFileChaining(String source, String destination) {
        create().readBitsFromFile(source).decode().writeTextToFile(destination).execute();
    }
}
