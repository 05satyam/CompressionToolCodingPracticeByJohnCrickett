import factory.CompDecomFactory;
import factory.CompressFile;
import factory.DeCompress;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args){
        //String path = "/Users/satyammittal/IdeaProjects/CompressionToolCodingPracticeByJohnCrickett/text.txt";
        try {
            CompDecomFactory cdObj  = null;

            String path = args[0];
            String action = args[1];

            switch(action){
                case "-c" : cdObj = new CompressFile();
                            ((CompressFile) cdObj).compressFile(path);
                    break;
                case "-d" : cdObj = new DeCompress();
                            ((DeCompress) cdObj).decompressFile(path);
                    break;
                default:
                    System.out.println("Choose a valid action : for compress : -c and for de-compress: -d");
                    break;
            }

            System.out.println("DONE!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

}
