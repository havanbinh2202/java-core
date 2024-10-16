package work.vietdefi.dsa.sort;

public class SwapSort implements Sorter{

    @Override
    public int[] sort(int[] values){
        int n = values.length;
        int[] sortedValues = values.clone();
        for (int i = 0; i < n; i++ ){
            while (sortedValues[i] != i + 1){
                int correctIndex = sortedValues[i] - 1;
                int temp = sortedValues[i];
                    sortedValues[i] = sortedValues[correctIndex];
                    sortedValues[correctIndex] = temp;
            }
        }
        return sortedValues;
    }
}
