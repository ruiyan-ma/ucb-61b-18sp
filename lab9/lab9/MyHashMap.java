package lab9;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 *
 * @author ruiyan ma
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private Set<K> keySet;

    private int loadFactor() {
        return keySet().size() / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /**
     * Removes all mappings from this map.
     */
    @Override
    public void clear() {
        keySet = new HashSet<>();
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /**
     * Computes the hash function of the given key. Consists of
     * computing the hashcode, followed by modding by the number of buckets.
     * To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /**
     * Resize the hash table.
     */
    private void resize() {
        ArrayMap<K, V>[] oldBuckets = buckets;
        buckets = new ArrayMap[buckets.length * 2];

        for (ArrayMap<K, V> oldBucket : oldBuckets) {
            if (oldBucket != null) {
                for (K key : oldBucket.keySet()) {
                    V value = oldBucket.get(key);
                    int hashCode = hash(key);
                    if (buckets[hashCode] == null) {
                        buckets[hashCode] = new ArrayMap<>();
                    }
                    buckets[hashCode].put(key, value);
                }
            }
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int hashCode = hash(key);
        if (buckets[hashCode] == null) {
            return null;
        } else {
            return buckets[hashCode].get(key);
        }
    }

    /**
     * Associates the specified value with the specified key in this map.
     */
    @Override
    public void put(K key, V value) {
        int hashCode = hash(key);
        if (buckets[hashCode] == null) {
            buckets[hashCode] = new ArrayMap<>();
        }

        buckets[hashCode].put(key, value);
        keySet.add(key);

        if (loadFactor() > MAX_LF) {
            resize();
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
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
     * Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        if (keySet.contains(key)) {
            int hashCode = hash(key);
            ArrayMap<K, V> bucket = buckets[hashCode];
            V removed = bucket.get(key);
            bucket.remove(key);
            keySet.remove(key);
            return removed;
        } else {
            return null;
        }
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        if (keySet.contains(key) && get(key) == value) {
            return remove(key);
        } else {
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }
}
