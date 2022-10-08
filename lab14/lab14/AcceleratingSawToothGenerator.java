package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.state = 0;
        this.factor = factor;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        if (state == 0) {
            period = (int) (period * factor);
        }
        return normalize(state);
    }

    private double normalize(int val) {
        return ((double) state / (period - 1)) * 2 - 1;
    }

    private int period;
    private int state;
    private final double factor;
}
