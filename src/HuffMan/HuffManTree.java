package HuffMan;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffManTree {

    public  HuffManNode createHuffManTree(Map<Character, Long> mapOfChar) {
        PriorityQueue<HuffManNode> pq = new PriorityQueue<HuffManNode>(mapOfChar.size(), (HuffManNode a, HuffManNode b) -> {
            if (a.freq < b.freq)
                return -1;
            if (a.freq == b.freq)
                return 0;
            else
                return 1;
        });

        for (Map.Entry<Character, Long> m : mapOfChar.entrySet()) {
            pq.add(new HuffManNode(m.getValue(), m.getKey()));
        }

        HuffManNode root = null;
        while (pq.size() > 1) {
            HuffManNode hf1 = pq.poll();
            HuffManNode hf2 = pq.poll();

            HuffManNode hf3 = new HuffManNode(hf1.freq + hf2.freq, '-', hf1, hf2);

            pq.add(hf3);
            root = hf3;
        }
        return root;
    }

    public  void printCode(HuffManNode root, String s)
    {

       if (root.left == null && root.right == null) {

            //System.out.println(root.c + ":" + s);
            return;
        }

        printCode(root.left, s + "0");
        printCode(root.right, s + "1");
    }

     public Map<Character, String> huffManPrefixTable(HuffManNode root){
        Map<Character, String> huffManCode = new HashMap<>();
        prefixCodeTable(root,"", huffManCode);
        return huffManCode;
    }

     void prefixCodeTable(HuffManNode root, String s, Map<Character, String> prefixTable) {

        if (root.left == null && root.right == null) {

            prefixTable.put(root.c, s);
            return;
        }

        prefixCodeTable(root.left, s + "0", prefixTable);
        prefixCodeTable(root.right, s + "1", prefixTable);
    }
}
