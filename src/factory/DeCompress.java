package factory;

import FileOperations.LoadAndReadFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DeCompress implements CompDecomFactory {
    LoadAndReadFile loadAndReadFile = new LoadAndReadFile();
    private Map<String, Character>  getHuffManCode(DataInputStream dis) throws IOException {
        Map<String, Character> huffmanCodes = new HashMap<>();

        int entries = dis.readInt();
        // Read each entry
        for (int i = 0; i < entries; i++) {
            // Read the character
            char character = dis.readChar();
            // Read the code length
            int codeLength = dis.readByte() & 0xFF; // to avoid sign extension for bytes above 127
            // Read the Huffman code as bits
            String huffmanCode = readHuffmanCodeAsBits(dis, codeLength);
            huffmanCodes.put(huffmanCode, character);
        }
        return huffmanCodes;
    }

    private String readHuffmanCodeAsBits(DataInputStream dis, int codeLength) throws IOException {
        StringBuilder huffmanCode = new StringBuilder(codeLength);
        int bitsToRead = codeLength;
        while (bitsToRead > 0) {
            byte readByte = dis.readByte();
            for (int i = 7; i >= 0 && bitsToRead > 0; i--) {
                boolean bit = ((readByte >> i) & 1) == 1;
                huffmanCode.append(bit ? '1' : '0');
                bitsToRead--;
            }
        }
        return huffmanCode.toString();
    }

    private void deCompress(String fileName) throws IOException {

        StringBuilder encodedText = new StringBuilder();
        Map<String, Character> huffmanCodes=null;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            huffmanCodes = getHuffManCode(dis);
            int padding = dis.readInt();  // Read padding size
            // Read the rest of the file as encoded data
            byte[] byteInput = new byte[dis.available()];
            dis.read(byteInput);
            for (byte b : byteInput) {
                encodedText.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
            }
            // Remove padding if necessary
            if (padding > 0) {
                encodedText.setLength(encodedText.length() - padding);
            }

        }
        generateDecompressFile(fileName, encodedText, huffmanCodes);

    }


    /**
     *
     * @param fileName
     * @param encodedText
     * @param huffmanCodes
     * @throws IOException
     *
     * this method is create the new decompressed file with the name of the file give by user
     */
    public void generateDecompressFile(String fileName, StringBuilder encodedText, Map<String, Character> huffmanCodes) throws IOException {
        String outputFileName = loadAndReadFile.getFileNameFromGivenPath(fileName, "-decompress") ;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName))) {
            String tempCode = "";
            for (int i = 0; i < encodedText.length(); i++) {
                tempCode += encodedText.charAt(i);
                if (huffmanCodes.containsKey(tempCode)) {
                    bw.write(huffmanCodes.get(tempCode));
                    tempCode = "";  // Reset temp code
                }
            }
        }
    }

    /**
     *
     * @param path
     * @throws IOException
     *
     * Main De compress functions
     */
    public void decompressFile(String path) throws IOException {
        deCompress(path);
    }

}
