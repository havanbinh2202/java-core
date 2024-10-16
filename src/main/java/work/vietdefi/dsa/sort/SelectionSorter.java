package work.vietdefi.dsa.sort;

public class SelectionSorter implements Sorter{
    @Override
    public int[] sort(int[] values) {
        int n = values.length;
        int[] sortedValues = values.clone();
        for (int i = 0; i < n - 1; i++) {
            //
            //Find the index of the smallest element in the unsorted section
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (sortedValues[j] < sortedValues[minIndex]) {
                        minIndex = j; // Update the index of the smallest element

                }
            }
            // Swap the smallest element with the first element of the unsorted section
            int temp = sortedValues[minIndex];
            sortedValues[minIndex] = sortedValues[i];
            sortedValues[i] = temp;
        }
        return sortedValues;
    }
}
