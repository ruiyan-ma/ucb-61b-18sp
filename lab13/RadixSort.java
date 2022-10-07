import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxLen = 0;
        for (String str : asciis) {
            maxLen = Math.max(maxLen, str.length());
        }

        String[] result = Arrays.copyOf(asciis, asciis.length);

        // Strings are sorted in dictionary order, i.e. "2" is after "100" because
        // "2" is considered as "2__" where "_" is a placeholder.
        // LSD starts from the rightmost character.
        for (int i = maxLen - 1; i >= 0; --i) {
            sortHelperLSD(result, i);
        }

        return result;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        String[] result = new String[asciis.length];
        int[] count = new int[256];
        int[] indices = new int[256];

        for (String str : asciis) {
            int c = charAt(str, index);
            count[c] += 1;
        }

        int start = 0;
        for (int i = 0; i < count.length; ++i) {
            indices[i] = start;
            start += count[i];
        }

        for (String str : asciis) {
            int c = charAt(str, index);
            result[indices[c]] = str;
            indices[c] += 1;
        }

        System.arraycopy(result, 0, asciis, 0, asciis.length);
    }

    private static int charAt(String s, int index) {
        if (index < s.length()) {
            return s.charAt(index);
        }
        return 0;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
