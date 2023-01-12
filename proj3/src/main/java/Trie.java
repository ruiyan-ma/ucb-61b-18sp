import java.util.*;

/**
 * Trie class.
 * Add a location to this trie by given its id and name.
 * Search a location using its name.
 * Search all locations with a given prefix.
 */
public class Trie {
    Trie() {
        root = new TrieNode();
    }

    /**
     * Add the given location to this trie.
     *
     * @param name: the given location name.
     * @param location: the given location.
     */
    public void add(String name, Location location) {
        TrieNode node = root;
        for (char c : name.toCharArray()) {
            if (!node.next.containsKey(c)) {
                node.next.put(c, new TrieNode());
            }
            node = node.next.get(c);
        }
        node.locations.add(location);
    }

    /**
     * Search the given location name in this trie.
     *
     * @param name: the given name.
     * @return true if we have this location.
     */
    public boolean search(String name) {
        TrieNode node = root;
        for (char c : name.toCharArray()) {
            if (node.next.containsKey(c)) {
                node = node.next.get(c);
            } else {
                return false;
            }
        }

        return node.locations.size() > 0;
    }

    /**
     * Search all locations with a given prefix.
     * @param prefix: the given prefix.
     * @return a list of locations ids.
     */
    public List<Location> searchPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (node.next.containsKey(c)) {
                node = node.next.get(c);
            } else {
                return new ArrayList<>();
            }
        }

        return collectLocations(node);
    }

    /**
     * Collect all locations under a given root node.
     * @param root: the root node.
     * @return all location ids.
     */
    private List<Location> collectLocations(TrieNode root) {
        List<Location> locations = new ArrayList<>();
        Deque<TrieNode> queue = new LinkedList<>();
        queue.push(root);

        while (!queue.isEmpty()) {
            TrieNode node = queue.poll();
            locations.addAll(node.locations);
            queue.addAll(node.next.values());
        }

        return locations;
    }

    TrieNode root;
}

/**
 * Trie node class.
 * Use hashmap to store all next nodes.
 * Each node has a location list, containing all locations with this name.
 */
class TrieNode {
    TrieNode() {
        next = new HashMap<>();
        locations = new ArrayList<>();
    }

    Map<Character, TrieNode> next;
    List<Location> locations;
}
