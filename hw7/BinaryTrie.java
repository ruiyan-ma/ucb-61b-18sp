import java.io.Serializable;
import java.util.*;

public class BinaryTrie implements Serializable {

    /**
     * Huffman trie node.
     */
    private static class TrieNode implements Comparable<TrieNode>, Serializable {

        /**
         * Constructor for leaf node.
         */
        TrieNode(char c, int weight, TrieNode left, TrieNode right) {
            this.symbol = c;
            this.weight = weight;
            this.left = left;
            this.right = right;
            this.isLeaf = true;
        }

        /**
         * Constructor for non-leaf node.
         */
        TrieNode(int weight, TrieNode left, TrieNode right) {
            this.weight = weight;
            this.left = left;
            this.right = right;
            this.isLeaf = false;
        }

        @Override
        public int compareTo(TrieNode other) {
            return weight - other.weight;
        }

        char symbol;
        final int weight;
        final boolean isLeaf;
        final TrieNode left, right;
    }

    /**
     * Given a frequency table which maps symbols to their relative
     * frequencies, build a Huffman decoding trie according to the
     * procedure discussed in class.
     *
     * @param frequencyTable: the frequency table
     */
    public BinaryTrie(Map<Character, Integer> frequencyTable) {

        // turn all characters into trie leaf nodes
        Queue<TrieNode> queue = new PriorityQueue<>();
        for (Character c : frequencyTable.keySet()) {
            queue.add(new TrieNode(c, frequencyTable.get(c), null, null));
        }

        // merge nodes
        while (queue.size() > 1) {
            TrieNode left = queue.poll();
            TrieNode right = queue.poll();
            TrieNode parent = new TrieNode(left.weight + right.weight, left, right);
            queue.add(parent);
        }

        root = queue.poll();
    }

    /**
     * Find the longest prefix that matches the given query sequence.
     *
     * @param querySequence: the given query sequence.
     * @return a Match object for that Match.
     */
    public Match longestPrefixMatch(BitSequence querySequence) {
        TrieNode node = root;
        int index = 0;

        while (!node.isLeaf) {
            int bit = querySequence.bitAt(index);
            if (bit == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
            index += 1;
        }

        return new Match(querySequence.firstNBits(index), node.symbol);
    }

    /**
     * Convert this BinaryTrie to an encoding map (a map from characters
     * to their encoding bit sequence).
     *
     * @return the inverse of the coding trie.
     */
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> result = new HashMap<>();

        // dfs: preorder traverse
        Deque<TrieNode> nodes = new LinkedList<>();
        Deque<BitSequence> sequences = new LinkedList<>();
        nodes.push(root);
        sequences.push(new BitSequence());

        while (!nodes.isEmpty()) {
            TrieNode node = nodes.pop();
            BitSequence sequence = sequences.pop();

            if (node.isLeaf) {
                result.put(node.symbol, sequence);
            } else {
                nodes.push(node.right);
                sequences.push(sequence.appended(1));

                nodes.push(node.left);
                sequences.push(sequence.appended(0));
            }
        }

        return result;
    }

    /**
     * The root of this Huffman Trie.
     */
    TrieNode root;
}
