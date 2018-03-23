package main.com.magiclegend.huffman.logic.interfaces;

import main.com.magiclegend.huffman.logic.interfaces.intermediates.*;

public interface IHuffman extends IExecute {
    IIString enterString(String s);
    IIDecodedOutput decode();
    IIString readLorem(String file);

    IHuffman readBitsFromFile(String file);
    IHuffman readFromFile(String file);
}