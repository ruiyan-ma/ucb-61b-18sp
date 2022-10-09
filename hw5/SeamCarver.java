import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    /**
     * Current picture.
     */
    public Picture picture() {
        return new Picture(picture);
    }

    /**
     * Width of current picture.
     */
    public int width() {
        return picture.width();
    }

    /**
     * Height of current picture.
     */
    public int height() {
        return picture.height();
    }

    /**
     * Energy of pixel at column x and row y.
     */
    public double energy(int x, int y) {
        checkRange(x, y);

        Color right = picture.get((x + 1) % width(), y);
        Color left = picture.get((x - 1 + width()) % width(), y);
        int xGrad = calculateGrad(left, right);

        Color above = picture.get(x, (y - 1 + height()) % height());
        Color below = picture.get(x, (y + 1) % height());
        int yGrad = calculateGrad(above, below);

        return xGrad + yGrad;
    }

    private void checkRange(int col, int row) {
        if (col < 0 || col >= width() || row < 0 || row >= height()) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int calculateGrad(Color prev, Color next) {
        int red = next.getRed() - prev.getRed();
        int green = next.getGreen() - prev.getGreen();
        int blue = next.getBlue() - prev.getBlue();
        return red * red + green * green + blue * blue;
    }

    /**
     * Sequence of indices for horizontal seam.
     */
    public int[] findHorizontalSeam() {
        return findSeam(transpose(calcEnergies()));
    }

    private double[][] transpose(double[][] arr) {
        int m = arr.length, n = arr[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                result[j][i] = arr[i][j];
            }
        }
        return result;
    }

    /**
     * Sequence of indices for vertical seam.
     */
    public int[] findVerticalSeam() {
        return findSeam(calcEnergies());
    }

    private int[] findSeam(double[][] energies) {
        int m = energies.length, n = energies[0].length;
        for (int i = 1; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                double minEnergy = energies[i - 1][j];

                if (j > 0) {
                    minEnergy = Math.min(minEnergy, energies[i - 1][j - 1]);
                }

                if (j < n - 1) {
                    minEnergy = Math.min(minEnergy, energies[i - 1][j + 1]);
                }

                energies[i][j] += minEnergy;
            }
        }

        int[] columns = new int[m];
        for (int i = m - 1; i >= 0; --i) {
            if (i == m - 1) {
                columns[i] = minIndex(energies[i], 0, n - 1);
            } else {
                columns[i] = minIndex(energies[i],
                        Math.max(0, columns[i + 1] - 1),
                        Math.min(n - 1, columns[i + 1] + 1));
            }
        }

        return columns;
    }

    private int minIndex(double[] nums, int start, int end) {
        int minIndex = start;
        for (int i = start + 1; i <= end; ++i) {
            if (nums[i] < nums[minIndex]) {
                minIndex = i;
            }
        }

        return minIndex;
    }

    private double[][] calcEnergies() {
        double[][] result = new double[height()][width()];
        for (int i = 0; i < height(); ++i) {
            for (int j = 0; j < width(); ++j) {
                result[i][j] = energy(j, i);
            }
        }
        return result;
    }

    /**
     * Remove horizontal seam from picture.
     */
    public void removeHorizontalSeam(int[] seam) {
        SeamRemover.removeHorizontalSeam(picture, findHorizontalSeam());
    }

    /**
     * Remove vertical seam from picture.
     */
    public void removeVerticalSeam(int[] seam) {
        SeamRemover.removeVerticalSeam(picture, findVerticalSeam());
    }

    private Picture picture;
}

