package work.vietdefi.dsa.sort;

public class InsertionSorter implements Sorter {
    @Override
    public int[] sort(int[] values) {
        int n = values.length;
        int[] sortedValues = values.clone();
        for (int i = 1; i < n; i++) {

            int key = sortedValues[i];
            int j = i - 1;

            while (j >= 0 && sortedValues[j] > key) {
                sortedValues[j + 1] = sortedValues[j];
                j = j - 1;
            }
            sortedValues[j + 1] = key;
        }
        return sortedValues;
    }
}