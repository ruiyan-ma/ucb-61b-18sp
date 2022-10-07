/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 *
 * @author Akhil Batra, Alexander Hwang
 *
 **/
public class CountingSort {
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     * Does not touch original array (non-destructive method).
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
     */
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            max = Math.max(max, i);
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        int[] sorted = new int[arr.length];
        int index = 0;
        for (int num = 0; num < counts.length; num += 1) {
            for (int j = 0; j < counts[num]; j += 1) {
                sorted[index] = num;
                index += 1;
            }
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        int[] sorted2 = new int[arr.length];
        for (int num : arr) {
            int place = starts[num];
            sorted2[place] = num;
            starts[num] += 1;
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     * Does not touch original array (non-destructive method).
     *
     * @param arr int array that will be sorted
     */
    public static int[] betterCountingSort(int[] arr) {
        // TODO make counting sort work with arrays containing negative numbers.
        int max = max(arr), min = min(arr);
        int range = max - min + 1;
        int[] count = new int[range];
        int[] indices = new int[range];

        // count 用来对 nums 中的数字进行统计
        for (int num : arr) {
            count[num - min] += 1;
        }

        // indices 用来记录每个数字的起始索引
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            indices[i] = index;
            index += count[i];
        }

        int[] result = new int[arr.length];
        for (int num : arr) {
            result[indices[num - min]] = num;
            indices[num - min] += 1;
        }

        return result;
    }

    /**
     * Find the max value of an array.
     */
    private static int max(int[] nums) {
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
        }
        return max;
    }

    /**
     * Find the min value of an array.
     */
    private static int min(int[] nums) {
        int min = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < min) {
                min = nums[i];
            }
        }
        return min;
    }
}
