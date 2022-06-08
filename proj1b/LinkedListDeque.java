public class LinkedListDeque<T> implements Deque<T> {

    public LinkedListDeque() {
        size = 0;
        head = new DNode(null);
        tail = new DNode(null);
        head.next = tail;
        tail.prev = head;
    }


    @Override
    public void addFirst(T item) {
        DNode node = new DNode(item, head, head.next);
        head.next.prev = node;
        head.next = node;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        DNode node = new DNode(item, tail.prev, tail);
        tail.prev.next = node;
        tail.prev = node;
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
        DNode node = head.next;
        while (node != tail) {
            System.out.print(node.item + " ");
            node = node.next;
        }
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        DNode removed = head.next;
        head.next = head.next.next;
        head.next.prev = head;
        size -= 1;
        return removed.item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        DNode removed = tail.prev;
        tail.prev = tail.prev.prev;
        tail.prev.next = tail;
        size -= 1;
        return removed.item;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }

        DNode node = head;
        for (int i = 0; i <= index; i++) {
            node = node.next;
        }
        return node.item;
    }

    /**
     * Same as get, but uses recursion.
     */
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(head.next, index);
    }

    private T getRecursiveHelper(DNode node, int index) {
        if (index == 0) {
            return node.item;
        } else {
            return getRecursiveHelper(node.next, index - 1);
        }
    }

    /**
     * The sentinel node
     */
    private final DNode head;
    private final DNode tail;

    /**
     * The number of items in the deque.
     */
    private int size;

    /**
     * The nested class Node.
     */
    private class DNode {

        DNode(T item) {
            this.item = item;
        }

        DNode(T item, DNode prev, DNode next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        T item;
        DNode prev, next;
    }
}
