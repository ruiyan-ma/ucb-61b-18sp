public class ArrayDeque<T> implements Deque<T> {

    /**
     * Constructor
     */
    ArrayDeque() {
        arr = (T[]) new Object[10];
        size = 0;
    }

    @Override
    public void addFirst(T item) {

    }

    @Override
    public void addLast(T item) {

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {

    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public T get(int index) {
        if (index >= size) return null;
        return arr[index];
    }

    /**
     * The array used to represent queue
     */
    T[] arr;

    /**
     * The number of items in this deque
     */
    int size;
}
