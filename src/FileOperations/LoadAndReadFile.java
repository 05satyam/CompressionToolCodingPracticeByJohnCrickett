package FileOperations;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadAndReadFile {

     void readUsingFileReader(String path) throws IOException {

        FileReader fr = new FileReader(path);

        int i;
        while ((i = fr.read()) != -1)
            System.out.print((char)i);
    }

     public String readAsString(String path) throws IOException {
        String data = new String(Files.readAllBytes(Paths.get(path)));
        if(data==null)
            throw new FileNotFoundException("Please provide the correct file and Try again!!!");
        return data;
    }

     List<String> readAllLines(String path) throws IOException {
        List<String> dataList = Collections.emptyList();
        dataList = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        return dataList;
    }

    /**
     *
     * @param data --> string of data after reading a file
     * @return
     * @throws IOException
     * generate frequency map of character after reading  file
     */
    public  Map<Character, Long> charFreqFromFile(String data) throws IOException {
        Map<Character, Long> mapOfCharFreqFromFile =  new HashMap<>();

        for(int i=0;i<data.length();i++){
            char c = data.charAt(i);
            mapOfCharFreqFromFile.put(c, mapOfCharFreqFromFile.getOrDefault(c, Long.valueOf(0))+1);
        }

        return mapOfCharFreqFromFile;
    }


    public String getFileNameFromGivenPath(String pathName, String action) {
        Path path = Paths.get(pathName);
        return path.getFileName().toString().split("\\.")[0] + action + ".txt";
    }
}
