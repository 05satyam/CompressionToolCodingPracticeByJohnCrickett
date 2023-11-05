# CompressionToolCodingPracticeByJohnCrickett
This is an implementation of Compression tool system design problem

##Problem statement: https://codingchallenges.fyi/challenges/challenge-huffman#step-4

### Entry point: Main.java 
### CompressFile -> contains logic of compression
### DeCompress   -> contians logic of de-compression
### FileOperations --> contians logics of loading file, getting file name, creating frequencey table from the file after reading it

### HuffMan --> consist logic of building a huffman tree and generating prefix table for compression

### Command line args are used to take file path input and action 
### Action can be "-c" for compression and "-d" for decopression
### ex: path-to-file -c

### interesting part is converting huffman code to bits to write as header and then reading it back 

```
Writing a huffman code for a character to compressed file
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
```
```
Reading a huffman code for a character from a compressed file
private String readHuffmanCodeAsBits(DataInputStream dis, int codeLength) throws IOException {
  StringBuilder huffmanCode = new StringBuilder(codeLength);
  int bitsToRead = codeLength;  # length of huffman code stored to compressed file
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
```
