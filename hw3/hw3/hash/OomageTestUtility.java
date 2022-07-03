package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int N = oomages.size();
        int[] count = new int[M];

        for (Oomage o : oomages) {
            int index = (o.hashCode() & 0x7FFFFFFF) % M;
            count[index] += 1;
        }

        for (int num : count) {
            if (num <= N / 50 || num >= N / 2.5) {
                return false;
            }
        }

        return true;
    }
}
