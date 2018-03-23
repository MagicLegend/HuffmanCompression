package main.com.magiclegend.huffman.logic.interfaces.intermediates;

import main.com.magiclegend.huffman.logic.interfaces.IExecute;

public interface IIDecodedOutput {
    IExecute writeTextToFile(String file);
    IExecute writeTextToConsole();
}
