public class LinkedListDeque<T> {

    public LinkedListDeque() {
        size = 0;
        head = new DNode(null);
        tail = new DNode(null);
        head.next = tail;
        tail.prev = head;
    }


    public void addFirst(T item) {
        DNode node = new DNode(item, head, head.next);
        head.next.prev = node;
        head.next = node;
        size += 1;
    }

    public void addLast(T item) {
        DNode node = new DNode(item, tail.prev, tail);
        tail.prev.next = node;
        tail.prev = node;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        DNode node = head.next;
        while (node != tail) {
            System.out.print(node.item + " ");
        }
        System.out.println();
    }

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

    public T get(int index) {
        DNode node = head.next;

        for (int i = 0; i < index && node != tail; i++) {
            node = node.next;
        }

        if (node != tail) {
            return node.item;
        } else {
            return null;
        }
    }

    /**
     * The sentinel node
     */
    DNode head, tail;

    /**
     * The number of items in the deque.
     */
    int size;

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
