package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    public SawToothGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        return normalize(state);
    }

    private double normalize(int val) {
        return ((double) val / (period - 1)) * 2 - 1;
    }

    private final int period;
    private int state;
}
