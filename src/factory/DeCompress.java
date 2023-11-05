package factory;

import FileOperations.LoadAndReadFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DeCompress implements CompDecomFactory {
    LoadAndReadFile loadAndReadFile = new LoadAndReadFile();

    private Map<String, Character> getHuffManCode(DataInputStream dis) throws IOException {
        Map<String, Character> huffmanCodes = new HashMap<>();

        int entries = dis.readInt();
        System.out.println("entries " + entries);
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
        Map<String, Character> huffmanCodes = null;
        //System.out.println("fileName "+fileName);
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {

            int padding = dis.readInt();  // Read padding size
            huffmanCodes = getHuffManCode(dis);

            // Read the rest of the file as encoded data
            byte[] byteInput = new byte[dis.available()];
            dis.read(byteInput);

            generatedEncodedStringWithProperFormat(encodedText, byteInput);
            // Remove padding if necessary
            if (padding > 0) {
                encodedText.setLength(encodedText.length() - padding);
            }
        }
        generateDecompressFile(fileName, encodedText, huffmanCodes);

    }


    /**
     * @param fileName
     * @param encodedText
     * @param huffmanCodes
     * @throws IOException this method is create the new decompressed file with the name of the file give by user
     */
    public void generateDecompressFile(String fileName, StringBuilder encodedText, Map<String, Character> huffmanCodes) throws IOException {
        String outputFileName = loadAndReadFile.getFileNameFromGivenPath(fileName, "-decompress");
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
     * @param encodedText
     * @param byteInput   Working of below functions---
     *                    <p>
     *                    Suppose byte b is -127. In binary, -127 is represented as 10000001.
     *                    However, Java uses two's complement to represent negative numbers, which would affect the output when converting directly to a binary string.
     *                    To prevent this and ensure we get the unsigned version (11111111 for -1, for example), we use b & 0xFF. So the steps would be:
     *                    <p>
     *                    -127 & 0xFF becomes 129 (because -127 is represented as 11111111 10000001 in two's complement and 0xFF is 00000000 11111111, so the AND operation results in 00000000 10000001).
     *                    Integer.toBinaryString(129) returns "10000001" (note there are no leading zeros since the number is greater than 0x80 or 128 in decimal).
     *                    String.format("%8s", "10000001") doesn't need to pad the string since it's already 8 characters.
     *                    No spaces to replace with zeros, so the string remains "10000001".
     *                    This string is appended to encodedText.
     *                    So, if the byteInput array was simply { -127 }, after this loop, encodedText would contain the string "10000001". If there were more bytes, their binary representations would be concatenated directly after this, creating a long binary string.
     */
    private void generatedEncodedStringWithProperFormat(StringBuilder encodedText, byte[] byteInput) {
        for (byte b : byteInput) {
            encodedText.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
    }

    /**
     * @param path
     * @throws IOException Main De compress functions
     */
    public void decompressFile(String path) throws IOException {
        deCompress(path);
    }

}
