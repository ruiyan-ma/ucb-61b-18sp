package lab9;

import java.util.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author ruiyan ma
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Root node of the tree. */
    private Node root;
    /* Key set. */
    private Set<K> keySet;

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all mappings from this map. */
    @Override
    public void clear() {
        root = null;
        keySet = new HashSet<>();
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node tree) {
        if (tree == null) {
            return null;
        } else if (tree.key.compareTo(key) == 0) {
            return tree.value;
        } else {
            V left = getHelper(key, tree.left);
            if (left != null) {
                return left;
            } else {
                return getHelper(key, tree.right);
            }
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node tree) {
        if (tree == null) {
            return new Node(key, value);
        } else {
            int compare = key.compareTo(tree.key);
            if (compare == 0) {
                tree.value = value;
            } else if (compare < 0) {
                tree.left = putHelper(key, value, tree.left);
            } else {
                tree.right = putHelper(key, value, tree.right);
            }
            return tree;
        }
    }

    /**
     * Inserts the mapping.
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
        keySet.add(key);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return keySet.size();
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return new HashSet<>(keySet);
    }

    /**
     * Return the min node of a subtree.
     */
    private Node findMin(Node tree) {
        while (tree.left != null) {
            tree = tree.left;
        }
        return tree;
    }

    /**
     * Remove the given key from the given subtree.
     * Return the new tree.
     */
    private Node removeHelper(K key, Node tree) {
        if (tree == null) {
            return null;
        }

        if (key.compareTo(tree.key) == 0) {
            if (tree.left == null) {
                return tree.right;
            } else if (tree.right == null) {
                return tree.left;
            } else {
                Node smallest = findMin(tree.right);
                tree.right = removeHelper(smallest.key, tree.right);
                tree.key = smallest.key;
                tree.value = smallest.value;
            }
        } else if (key.compareTo(tree.key) < 0) {
            tree.left = removeHelper(key, tree.left);
        } else {
            tree.right = removeHelper(key, tree.right);
        }

        return tree;
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        V removed = get(key);
        if (removed == null) {
            return null;
        } else {
            root = removeHelper(key, root);
            keySet.remove(key);
            return removed;
        }
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V removed = get(key);
        if (removed == null || removed != value) {
            return null;
        } else {
            return remove(key);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    /**
     * Iterator for BSTMap.
     */
    private class BSTMapIterator implements Iterator<K> {

        BSTMapIterator() {
            orderedKeys = new LinkedList<>();
            inorder(root);
        }

        private void inorder(Node tree) {
            if (tree == null) return;
            inorder(tree.left);
            orderedKeys.add(tree.key);
            inorder(tree.right);
        }

        @Override
        public boolean hasNext() {
            return orderedKeys.size() != 0;
        }

        @Override
        public K next() {
            return orderedKeys.poll();
        }

        private final Queue<K> orderedKeys;
    }
}
