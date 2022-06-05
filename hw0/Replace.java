class Replace {
    /** Replace each element a[i] with the sum of a[i] through a[i + n],
     *  but only if a[i] is positive valued. */
    public static void windowPosSum(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 0) {
                a[i] = sumAtIndex(a, i, n);
            }
        }
    }

    /** Helper method to calculate the sum of a[index] to a[index + n]. */
    public static int sumAtIndex(int[] a, int index, int n) {
        int sum = 0;
        for (int i = 0; i <= n && index + i < a.length; i++) {
            sum += a[index + i];
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);

        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
    }
}
