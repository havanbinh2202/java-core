package work.vietdefi.dsa.sort;

public class HeapSorter implements Sorter {
    /**
     * Sorts the given array of integers using the Heap Sort algorithm.
     *
     * @param values The array of integers to be sorted.
     * @return A new sorted array.
     */
    @Override
    public int[] sort(int[] values) {
        int n = values.length;
        int[] sortedValues = values.clone(); // Create a copy of the array so that the original array is not changed

        // Build the heap (for heap max)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(sortedValues, n, i);
        }

        // One element, array aggregation and sorting
        for (int i = n - 1; i > 0; i--) {
            //Move the root element (max) to the end of the array
            int temp = sortedValues[0];
            sortedValues[0] = sortedValues[i];
            sortedValues[i] = temp;

            // Call heapify on the reduced array
            heapify(sortedValues, i, 0);
        }

        return sortedValues;
    }

    /**
     * Ensures that an element at index `i` in the tree is heap.
     *
     * @param array The array to be heapified.
     * @param n Heap size.
     * @param i Index of the root element.
     */
    private void heapify(int[] array, int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1; // left subscript
        int right = 2 * i + 2; //right subscript


        // If the fruit is larger than the root
        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        //If the child must be larger than the known largest
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        // If largest is not the root
        if (largest != i) {
            int swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;

            // Call heapify recursively on the changed tree
            heapify(array, n, largest);
        }
    }
}
