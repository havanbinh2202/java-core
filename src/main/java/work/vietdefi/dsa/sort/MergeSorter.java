package work.vietdefi.dsa.sort;

public class MergeSorter implements Sorter {
    /**
     * Sorts the given array of integers using the Merge Sort algorithm.
     *
     * @param values The array of integers to be sorted.
     * @return A new sorted array.
     */
    @Override
    public int[] sort(int[] values) {
        if (values.length < 2) {
            return values.clone(); // Returns the given array if there are less than 2 elements

        }

        int mid = values.length / 2;
        int[] left = new int[mid];
        int[] right;

        if (values.length % 2 == 0) {
            right = new int[mid];
        } else {
            right = new int[mid + 1];
        }

        // Split the array into two halves
        System.arraycopy(values, 0, left, 0, mid);
        System.arraycopy(values, mid, right, 0, right.length);

        // Recursively sort the two halves
        left = sort(left);
        right = sort(right);

        // Combine and return the sorted array
        return merge(left, right);
    }

    /**
     * Merges two sorted arrays into a single sorted array.
     *
     * @param left The left sorted array.
     * @param right The right sorted array.
     * @return A merged sorted array.
     */
    private int[] merge(int[] left, int[] right) {
        int[] merged = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        // Merge two sorted arrays
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        // Copy the rest of the left array
        while (i < left.length) {
            merged[k++] = left[i++];
        }

        // Copy the rest of the right array
        while (j < right.length) {
            merged[k++] = right[j++];
        }

        return merged;
    }
}
