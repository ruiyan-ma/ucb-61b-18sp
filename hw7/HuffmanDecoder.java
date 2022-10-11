public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Please input the huf file name and new txt file name.");
        }

        ObjectReader or = new ObjectReader(args[0]);

        BinaryTrie trie = (BinaryTrie) or.readObject();
        int num = (int) or.readObject();
        BitSequence sequence = (BitSequence) or.readObject();

        char[] symbols = new char[num];
        for (int i = 0; i < num; ++i) {
            Match match = trie.longestPrefixMatch(sequence);
            symbols[i] = match.getSymbol();
            sequence = sequence.allButFirstNBits(match.getSequence().length());
        }

        FileUtils.writeCharArray(args[1], symbols);
    }
}
