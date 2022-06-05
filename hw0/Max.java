class Max {
    /** Find the maximum value of an int array, all numbers are greater
     *  than or equal to 0. */
    public static int max(int[] m) {
        int max = 0;
        for (int i : m) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }


    public static void main (String[] args) {
        int[] numbers = new int[] {9, 2, 15, 2, 22, 10, 6};
        System.out.println(max(numbers));
    }
}
