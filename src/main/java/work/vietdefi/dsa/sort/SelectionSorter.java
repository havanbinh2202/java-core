package work.vietdefi.dsa.sort;

public class SelectionSorter implements Sorter{
    @Override
    public int[] sort(int[] values) {
        int n = values.length;
        int[] sortedValues = values.clone(); // Tạo bản sao của mảng để sắp xếp
        for (int i = 0; i < n - 1; i++) {
            // Tìm chỉ số của phần tử nhỏ nhất trong phần chưa sắp xếp
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (sortedValues[j] < sortedValues[minIndex]) {
                    minIndex = j; // Cập nhật chỉ số của phần tử nhỏ nhất
                }
            }
            // Hoán đổi phần tử nhỏ nhất với phần tử đầu tiên của phần chưa sắp xếp
            int temp = sortedValues[minIndex];
            sortedValues[minIndex] = sortedValues[i];
            sortedValues[i] = temp;
        }
        return sortedValues; // Trả về mảng đã sắp xếp
    }
}
