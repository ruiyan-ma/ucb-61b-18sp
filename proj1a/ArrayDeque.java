import java.lang.reflect.Array;

/**
 * A deque implemented using circular array.
 *
 * @author ruiyan ma
 */

public class ArrayDeque<T> implements Deque<T> {

    /**
     * Constructor
     */
    ArrayDeque() {
        arr = (T[]) new Object[10];
    }

    ArrayDeque(int size) {
        arr = (T[]) new Object[size];
    }

    @Override
    public void addFirst(T item) {
        if (size == arr.length - 1) {
            doubleSize();
        }

        first = (first - 1 + arr.length) % arr.length;
        arr[first] = item;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == arr.length - 1) {
            doubleSize();
        }

        last = (last + 1) % arr.length;
        arr[last] = item;
        size += 1;
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
        for (int i = 0; i < size; i++) {
            int index = (first + i) % arr.length;
            System.out.print(arr[index] + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T removed = arr[first];
        arr[first] = null;
        first = (first + 1) % arr.length;
        size -= 1;

        if ((double) size / arr.length < 0.25) {
            halfSize();
        }

        return removed;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T removed = arr[last];
        arr[last] = null;
        last = (last - 1 + arr.length) % arr.length;
        size -= 1;

        if ((double) size / arr.length < 0.25) {
            halfSize();
        }

        return removed;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return arr[(first + index) % arr.length];
    }

    /**
     * Double the size of the circular array.
     */
    private void doubleSize() {
        arr = copy((T[]) new Object[size * 2]);
        first = 0;
        last = size - 1;
    }

    /**
     * Half the size of the circular array.
     */
    private void halfSize() {
        arr = copy((T[]) new Object[size / 2]);
        first = 0;
        last = size - 1;
    }

    /** Copy arr into the given new array. */
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
    T[] arr;

    /**
     * The first index of the circular array.
     */
    int first = 1;

    /**
     * The last index of the circular array.
     */
    int last = -1;

    /**
     * The number of items in this deque.
     */
    int size = 0;
}
