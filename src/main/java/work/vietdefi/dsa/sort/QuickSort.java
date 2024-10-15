package work.vietdefi.dsa.sort;

public class QuickSort implements Sorter{
    /**
     * Sorts the given array of integers using the Quick Sort algorithm.
     *
     * @param values The array of integers to be sorted.
     * @return A new sorted array.
     */
    @Override
    public int[] sort(int[] values) {
        int[] sortedValues = values.clone(); //
        quickSort(sortedValues, 0, sortedValues.length - 1);
        return sortedValues;
    }

    /**
     * Recursive quick sort method.
     *
     * @param array The array to be sorted.
     * @param low The starting index.
     * @param high The ending index.
     */
    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            // Pi is the partition index, array[pi] is already in the correct position
            int pi = partition(array, low, high);

            // Arrange elements before and after the partition
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    /**
     * Partitions the array around the pivot.
     *
     * @param array The array to partition.
     * @param low The starting index.
     * @param high The ending index.
     * @return The index of the pivot.
     */
    private int partition(int[] array, int low, int high) {
        int pivot = array[high]; // Select the last element as the pivot

        int i = low - 1; // Index of the smaller element


        for (int j = low; j < high; j++) {
            // If the current element is less than or equal to pivot
            if (array[j] <= pivot) {
                i++;

                // Swap array[i] and array[j]
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Swap array[i+1] and array[high] (or pivot)
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }
}
