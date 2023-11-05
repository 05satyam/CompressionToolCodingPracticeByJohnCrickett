package HuffMan;

public class HuffManNode {
    long freq;
    char c;
    HuffManNode left;
    HuffManNode right;

    public HuffManNode() {
    }

    HuffManNode(long freq, char c) {
        this.freq = freq;
        this.c = c;
        this.left = this.right = null;
    }

    HuffManNode(long freq, char c, HuffManNode left, HuffManNode right) {
        this.freq = freq;
        this.c = c;
        this.left = left;
        this.right = right;
    }

}
