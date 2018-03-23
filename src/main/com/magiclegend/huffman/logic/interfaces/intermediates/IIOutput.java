package main.com.magiclegend.huffman.logic.interfaces.intermediates;

import main.com.magiclegend.huffman.logic.interfaces.IExecute;

public interface IIOutput {
    IExecute writeBitsToFile(String file);
    IExecute writeToFile(String file);
}
