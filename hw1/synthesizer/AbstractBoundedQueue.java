package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    protected int fillCount;

    protected int capacity;
}
