package work.vietdefi.dsa.sort;
/**
 * An implementation of the {@link Sorter} interface using the Bubble Sort algorithm.
 * Bubble Sort repeatedly steps through the list, compares adjacent elements,
 * and swaps them if they are in the wrong order. The process is repeated
 * until the list is sorted.
 *
 * <p>This implementation returns an empty array for now. In a real implementation,
 * it would return the sorted array.</p>
 */

public class BubbleSorter implements Sorter {
    /**
     * Sorts the given array of integers using the Bubble Sort algorithm.
     *
     * <p>Note: This method currently returns an empty array. You will need to implement
     * the sorting logic to make it functional.</p>
     *
     * @param values The array of integers to be sorted.
     * @return A new sorted array (currently returns an empty array).
     */
    @Override
    public int[] sort(int[] values) {
        int n = values.length;
        int[] sortedValues = values.clone();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedValues[j] > sortedValues[j + 1]) {
                    int temp = sortedValues[j];
                    sortedValues[j] = sortedValues[j + 1];
                    sortedValues[j + 1] = temp;
                }
            }
        }
        return sortedValues;
    }
}
