import java.util.HashMap;
import java.util.Map;


public class Trie {

    private static class TrieNode {

        TrieNode() {
            next = new HashMap<>();
            isWord = false;
        }

        /**
         * Next nodes.
         */
        Map<Character, TrieNode> next;

        /**
         * Whether the current node is a word.
         */
        boolean isWord;
    }

    Trie() {
        root = new TrieNode();
    }

    public void add(String word) {
        TrieNode node = root;

        for (char c : word.toCharArray()) {
            // add this char if it doesn't exist
            if (!node.next.containsKey(c)) {
                node.next.put(c, new TrieNode());
            }
            // go one step down
            node = node.next.get(c);
        }

        // mark the last node as a word
        node.isWord = true;
    }

    public boolean findWord(String word) {
        TrieNode last = search(word);
        return last != null && last.isWord;
    }

    public boolean findPrefix(String prefix) {
        return search(prefix) != null;
    }

    /**
     * Try to search the given string in this Trie.
     *
     * @param str: the string to search.
     * @return the last trie node if we find this string in our Trie,
     * or null if we don't.
     */
    private TrieNode search(String str) {
        TrieNode node = root;

        for (char c : str.toCharArray()) {
            if (!node.next.containsKey(c)) {
                return null;
            }
            node = node.next.get(c);
        }

        return node;
    }

    /**
     * The root of trie.
     */
    TrieNode root;
}
