package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    public StrangeBitwiseGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState);
    }

    private double normalize(int val) {
        return ((double) val / (period - 1)) * 2 - 1;
    }

    private final int period;
    private int state;
}
