package factory;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import FileOperations.LoadAndReadFile;
import HuffMan.HuffManNode;
import HuffMan.HuffManTree;


public class CompressFile implements CompDecomFactory {

    LoadAndReadFile loadAndReadFileObj = new LoadAndReadFile();
    HuffManTree huffManTreeObj = new HuffManTree();

    public void compressFile(String path) throws IOException {
        String data = loadAndReadFileObj.readAsString(path);

        Map<Character, Long> mapOfCharFreqFromFile = loadAndReadFileObj.charFreqFromFile(data);

        HuffManNode root = huffManTreeObj.createHuffManTree(mapOfCharFreqFromFile);

        Map<Character, String> huffManCode = huffManTreeObj.huffManPrefixTable(root);
        System.out.println("compress huffManCode size "+ huffManCode.size());

        String outputFileName = loadAndReadFileObj.getFileNameFromGivenPath(path, "-compress");
        CompressFile.encoded(data, outputFileName, huffManCode);
    }

    /**
     *
     * @param huffmanCodes
     * @param dos
     * @throws IOException
     *
     * this method writes header content to the compressed file
     * Header content contains- the size of huffman code, charcter - huffcode size for a character and value of huffman code for character
     */
    public static void writeHeader(Map<Character, String> huffmanCodes, DataOutputStream dos) throws IOException {
        dos.writeInt(huffmanCodes.size());

        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            // Write the character
            dos.writeChar(entry.getKey());
            // Write the Huffman code as bits
            dos.writeByte(entry.getValue().length());
            writeHuffmanCodeAsBits(dos, entry.getValue());
        }

    }

    /**
     *
     * @param dos
     * @param huffmanCode
     * @throws IOException
     *
     * generate the bit sequence for compressed file for each huffman code
     */
    private static void writeHuffmanCodeAsBits(DataOutputStream dos, String huffmanCode) throws IOException {
        int bitsToWrite = huffmanCode.length();
        int bitPosition = 0;
        while (bitsToWrite > 0) {
            byte toWrite = 0;
            for (int i = 0; i < 8 && bitPosition < huffmanCode.length(); i++) {
                toWrite |= (huffmanCode.charAt(bitPosition) == '1' ? 1 : 0) << (7 - i);
                bitPosition++;
                bitsToWrite--;
            }
            dos.writeByte(toWrite);
        }
    }

    public static void encoded(String fileData, String outputFilename, Map<Character, String> huffmanCodes) throws IOException {
        StringBuilder encodedText = new StringBuilder();

        for (int i = 0; i < fileData.length(); i++) {
            char c = fileData.charAt(i);
            encodedText.append(huffmanCodes.get(c));
        }
        // Convert encoded string to byte array and keep track of padding
        byte[] byteOutput = encodeToByteArray(encodedText.toString());
        int padding = 8 - encodedText.length() % 8;
        if (padding == 8) padding = 0; // No padding needed if length is a multiple of 8

        compressedDateToTheFile(outputFilename, huffmanCodes, byteOutput, padding);
        ;
    }

    private static void compressedDateToTheFile(String outputFilename, Map<Character, String> huffmanCodes, byte[] byteOutput, int padding) throws IOException {
        // Write header (for simplicity, let's just write the size of padding)
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFilename))) {
            dos.writeInt(padding);  // Write padding size
            writeHeader(huffmanCodes, dos);
            dos.write(byteOutput);  // Write compressed data
        }
    }

    /**
     *
     * @param encodedText
     * @return
     *
     * generate byte array for the content of the given file
     */
    private static byte[] encodeToByteArray(String encodedText) {
        int byteSize = (int) Math.ceil(encodedText.length() / 8.0);
        byte[] output = new byte[byteSize];
        for (int i = 0; i < encodedText.length(); i += 8) {
            String byteStr = encodedText.substring(i, Math.min(i + 8, encodedText.length()));
            output[i / 8] = (byte) Integer.parseInt(byteStr, 2);
        }

        return output;
    }


}
