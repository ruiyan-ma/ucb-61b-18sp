import java.lang.reflect.Array;

/**
 * A deque implemented using circular array.
 *
 * @author ruiyan ma
 */

public class ArrayDeque<T> {

    /**
     * Constructor
     */
    public ArrayDeque() {
        this.arr = (T[]) new Object[10];
        this.size = 0;
    }

    ArrayDeque(int size) {
        this.arr = (T[]) new Object[size];
        this.size = 0;
    }

    public void addFirst(T item) {
        if (size == 0) {
            arr[0] = item;
            first = 0;
        } else {
            first = (first - 1 + arr.length) % arr.length;
            arr[first] = item;
        }

        size += 1;
        if (size == arr.length - 1) {
            doubleSize();
        }
    }

    public void addLast(T item) {
        if (size == 0) {
            arr[0] = item;
            first = 0;
        } else {
            int index = (first + size) % arr.length;
            arr[index] = item;
        }

        size += 1;
        if (size == arr.length - 1) {
            doubleSize();
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            int index = (first + i) % arr.length;
            System.out.print(arr[index] + " ");
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T removed = arr[first];
        arr[first] = null;
        first = (first + 1) % arr.length;
        size -= 1;

        double loadFactor = (double) size / arr.length;
        if (loadFactor < 0.25 && arr.length > 10) {
            halfSize();
        }

        return removed;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        int last = (first + size - 1) % arr.length;
        T removed = arr[last];
        arr[last] = null;
        size -= 1;

        double loadFactor = (double) size / arr.length;
        if (loadFactor < 0.25 && arr.length > 10) {
            halfSize();
        }

        return removed;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return arr[(first + index) % arr.length];
    }

    /**
     * Return true if two ArrayDeques are equal.
     */
    public boolean equal(ArrayDeque<T> deque) {
        if (size != deque.size) {
            return false;
        }

        for (int i = 0; i < size; ++i) {
            if (!get(i).equals(deque.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Double the size of the circular array.
     */
    private void doubleSize() {
        arr = copy((T[]) new Object[size * 2]);
        first = 0;
    }

    /**
     * Half the size of the circular array.
     */
    private void halfSize() {
        arr = copy((T[]) new Object[size / 2]);
        first = 0;
    }

    /**
     * Copy arr into the given new array.
     */
    private T[] copy(T[] newArr) {
        for (int i = 0; i < size; i++) {
            int index = (first + i) % arr.length;
            newArr[i] = arr[index];
        }
        return newArr;
    }

    /**
     * The circular array.
     */
    private T[] arr;

    /**
     * The first index of the circular array.
     */
    private int first;

    /**
     * The number of items in this deque.
     */
    private int size;
}
