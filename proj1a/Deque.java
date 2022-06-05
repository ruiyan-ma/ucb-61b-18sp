public interface Deque<T> {

    /**
     * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item);

    /**
     * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item);

    /**
     * Returns true if the deque is empty.
     */
    public boolean isEmpty();

    /**
     * Return the number of items in the deque.
     */
    public int size();

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    public void printDeque();

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    public T removeFirst();

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    public T removeLast();

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next
     * item, and so forth. If no such item exists, returns null.
     */
    public T get(int index);
}
